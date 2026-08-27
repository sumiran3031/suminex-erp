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

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp");
    private static final List<String> ALLOWED_DOCUMENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024; // 5MB

    @Value("${app.file.upload-dir}")
    private String baseUploadDir;

    public String storeProfilePhoto(Long userId, MultipartFile file) {
        return storeFile(file, "profile-photos", "user-" + userId, ALLOWED_IMAGE_TYPES);
    }

    public String storeAssignmentFile(Long assignmentId, MultipartFile file) {
        return storeFile(file, "assignments", "assignment-" + assignmentId, ALLOWED_DOCUMENT_TYPES);
    }

    public String storeSubmissionFile(Long submissionId, MultipartFile file) {
        return storeFile(file, "submissions", "submission-" + submissionId, ALLOWED_DOCUMENT_TYPES);
    }

    private String storeFile(MultipartFile file, String category, String prefix, List<String> allowedTypes) {
        validateFile(file, allowedTypes);

        try {
            String subDir = baseUploadDir + "/" + category;
            Path uploadPath = Paths.get(subDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String extension = getSafeExtension(file.getContentType());
            String generatedFilename = prefix + "-" + UUID.randomUUID() + extension;

            Path targetPath = uploadPath.resolve(generatedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return subDir + "/" + generatedFilename;

        } catch (IOException ex) {
            throw new BadRequestException("Failed to store file: " + ex.getMessage());
        }
    }

    private void validateFile(MultipartFile file, List<String> allowedTypes) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException("File size must not exceed 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new BadRequestException("Unsupported file type: " + contentType);
        }
    }

    private String getSafeExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "application/pdf" -> ".pdf";
            case "application/msword" -> ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx";
            default -> throw new BadRequestException("Unsupported file type");
        };
    }
}