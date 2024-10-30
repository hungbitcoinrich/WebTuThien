package com.example.WebBanSach.Controller;

import aj.org.objectweb.asm.ConstantDynamic;
import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.services.CartService;
import com.example.WebBanSach.services.EmailService;
import com.example.WebBanSach.services.OrderService;
import com.example.WebBanSach.services.UserServices;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserServices userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart/cart";
    }

    @PostMapping("/update")
    public String updateCartQuantities(@RequestParam List<Long> ids, @RequestParam List<Integer> quantities) {
        for (int i = 0; i < ids.size(); i++) {
            Long itemId = ids.get(i);
            int quantity = quantities.get(i);
            cartService.updateCartItemQuantity(itemId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam int productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable int itemId) {
        cartService.removeFromCart(itemId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        double totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart/checkout";
    }

    @GetMapping("/home")
    public String goToHome() {
        return "redirect:/Admin";
    }


    @GetMapping("/history")
    public String viewOrderHistory(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);

        return "cart/history";
    }



    // Quản lý đơn hàng
    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam String status,
                                    Model model,
                                    Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent()) {
            Order existingOrder = order.get();

            // Ensure the order belongs to the current user
            if (existingOrder.getUser().getId().equals(user.getId())) {
                existingOrder.setStatus(status);
                orderService.saveOrder(existingOrder, user);
                return "redirect:/cart/history";
            } else {
                model.addAttribute("error", "You do not have permission to update this order");
                return "cart/history";
            }
        } else {
            model.addAttribute("error", "Order not found");
            return "cart/history";
        }
    }

    @GetMapping("/track/{orderId}")
    public String trackOrder(@PathVariable Long orderId, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "cart/track";
        } else {
            model.addAttribute("error", "Order not found");
            return "error/404";
        }
    }



    @GetMapping("/invoice/{id}")
    public String orderInvoice(@PathVariable Long id, Model model, Principal principal) throws MessagingException {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if ("delivered".equals(order.getStatus())) {

                String username = principal.getName();
                User user = userService.findByUsername(username);
                emailService.sendInvoiceEmail(order, user.getEmail());
                model.addAttribute("order", order);
                model.addAttribute("customer", order.getCustomerName());
                return "cart/invoice";
            } else {
                model.addAttribute("error", "Order is not delivered yet");
                return "cart/history";
            }
        }
        return "redirect:/Admin/status";
    }

    @GetMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, Model model) {
        orderService.cancelOrder(orderId);
        return "redirect:/cart/history"; // Redirect to order history
    }

}
