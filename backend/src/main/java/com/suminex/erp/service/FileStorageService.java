package com.suminex.erp.service;

import com.suminex.erp.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp"
    );

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024; // 5MB

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public String storeProfilePhoto(Long userId, MultipartFile file) {
        validateFile(file);

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String extension = getSafeExtension(file.getContentType());
            String generatedFilename = "user-" + userId + "-" + UUID.randomUUID() + extension;

            Path targetPath = uploadPath.resolve(generatedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return uploadDir + "/" + generatedFilename;

        } catch (IOException ex) {
            throw new BadRequestException("Failed to store file: " + ex.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException("File size must not exceed 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Only JPEG, PNG, and WEBP images are allowed");
        }
    }

    private String getSafeExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new BadRequestException("Unsupported file type");
        };
    }
}