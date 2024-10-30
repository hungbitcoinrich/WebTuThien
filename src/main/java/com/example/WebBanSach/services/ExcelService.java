package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.Category;
import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExcelService {

    private static final Logger LOGGER = Logger.getLogger(ExcelService.class.getName());

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserServices userService;

    @Autowired
    private ProductService productService;

    public List<Product> parseExcelFile(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        // Skipping header row
        if (rows.hasNext()) {
            rows.next();
        }

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            try {
                Product product = new Product();

                product.setDetail(currentRow.getCell(0).getStringCellValue());
                product.setImg1(currentRow.getCell(1).getStringCellValue());
                product.setPrice(currentRow.getCell(2).getNumericCellValue());
                product.setQuantity((int) currentRow.getCell(3).getNumericCellValue());
                product.setTitle(currentRow.getCell(4).getStringCellValue());
                product.setAuthor(currentRow.getCell(5).getStringCellValue());
                int categoryId = (int) currentRow.getCell(6).getNumericCellValue();
                Category category = categoryService.getCategoryById((long) categoryId);
                product.setCategory(category);

                int userId = (int) currentRow.getCell(7).getNumericCellValue();
                User user = userService.getUserById((long) userId);
                product.setUser(user);

                // Check if product exists and update quantity if it does
                boolean productExists = false;
                List<Product> existingProducts = productService.getAllProducts();
                for (Product existingProduct : existingProducts) {
                    if (existingProduct.equalsExcludingQuantity(product)) {
                        existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
                        productService.updateProduct(existingProduct);
                        productExists = true;
                        break;
                    }
                }

                if (!productExists) {
                    products.add(product);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing row " + currentRow.getRowNum(), e);
            }
        }

        workbook.close();
        return products;
    }
    public void exportProductsToExcel(HttpServletResponse response) throws IOException {
        List<Product> products = productService.getAllProducts();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Products");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Detail");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Image");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Price");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Quantity");

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("Title");

        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("Author");

        headerCell = headerRow.createCell(6);
        headerCell.setCellValue("Category");

        headerCell = headerRow.createCell(7);
        headerCell.setCellValue("User");

        // Fill data rows
        int rowCount = 1;
        for (Product product : products) {
            Row dataRow = sheet.createRow(rowCount++);
            dataRow.createCell(0).setCellValue(product.getDetail());
            dataRow.createCell(1).setCellValue(product.getImg1());
            dataRow.createCell(2).setCellValue(product.getPrice());
            dataRow.createCell(3).setCellValue(product.getQuantity());
            dataRow.createCell(4).setCellValue(product.getTitle());
            dataRow.createCell(5).setCellValue(product.getAuthor());
            dataRow.createCell(6).setCellValue(product.getCategory().getName()); // Assuming category has a name field
            if (product.getUser() != null) {
                dataRow.createCell(7).setCellValue(product.getUser().getUsername()); // Assuming user has a username field
            } else {
                dataRow.createCell(7).setCellValue("N/A"); // Default value when user is null
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
