package com.example.WebBanSach.Controller;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            // If the file already exists, delete it before copying
            Files.deleteIfExists(filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Could not save image file: " + fileName, ex);
        }
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            Files.delete(path);
            System.out.println("File deleted successfully: " + filePath);
        } else {
            System.err.println("File does not exist: " + filePath);
        }
    }
}
