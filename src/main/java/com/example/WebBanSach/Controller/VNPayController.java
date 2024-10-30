package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.services.CartService;
import com.example.WebBanSach.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/payment")
public class VNPayController {

    // Cấu hình VNPay
    private String vnp_TmnCode = "SLZXDFIV";
    private String vnp_HashSecret = "O14HMLQZV801WFXX8S100OK2W7L18WDA";
    private String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private String vnp_Returnurl = "http://localhost:8888/order/vnpay_return";

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    @GetMapping("/vnpay_payment")
    public String submitOrder(RedirectAttributes redirectAttributes) {
        try {
            // Lấy danh sách tất cả các đơn hàng
            List<Order> allOrders = orderService.getAllOrders();

            // Kiểm tra xem danh sách có rỗng không
            if (allOrders.isEmpty()) {
                return "redirect:/error";
            }

            // Lấy đơn hàng đang được đặt hàng (ví dụ: lấy đơn hàng cuối cùng)
            Order currentOrder = getCurrentOrderBeingProcessed(allOrders);

            // Kiểm tra xem currentOrder có null không
            if (currentOrder == null) {
                return "redirect:/error";
            }

            List<CartItem> cartItems = cartService.getCartItems();
            double totalPrice = cartService.calculateTotalPrice(cartItems);

            // Tạo URL thanh toán VNPay với giá trị totalPrice và orderId của đơn hàng hiện tại
            String paymentUrl = generateVNPayUrl(totalPrice, currentOrder.getId());

            // Log URL thanh toán
            System.out.println("Payment URL: " + paymentUrl);

            // Lưu đơn hàng vào cơ sở dữ liệu
//            orderService.saveOrder(currentOrder);
            orderService.createOrder(currentOrder, cartItems);

            // Redirect đến URL thanh toán
            return "redirect:" + paymentUrl;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "error";
        }
    }
    private Order getCurrentOrderBeingProcessed(List<Order> orders) {
        // Lấy đơn hàng đang được xử lý (ví dụ: lấy đơn hàng cuối cùng trong danh sách có thể được cập nhật về logic phù hợp)
        for (Order order : orders) {
            // Xử lý logic để lấy đơn hàng đang xử lý phù hợp với yêu cầu của bạn
          return order;
        }
        return null;
    }
    private String generateVNPayUrl(double totalPrice, Long orderId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        int vnp_Amount = (int) (totalPrice * 100);
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toan don hang " + orderId;
        String vnp_OrderType = "other";
        String vnp_TxnRef = getRandomNumber(8);
        String vnp_IpAddr = "192.168.8.1";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_BankCode", "NCB");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
                query.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
            }
        }

        if (hashData.length() > 0 && query.length() > 0) {
            hashData.setLength(hashData.length() - 1);
            query.setLength(query.length() - 1);
        }

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        cartService.clearCart();
        return vnp_Url + "?" + query.toString();
    }

    private static String hmacSHA512(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        hmac512.init(secretKeySpec);
        byte[] bytes = hmac512.doFinal(data.getBytes());
        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String getRandomNumber(int length) {
        String characters = "0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
}
