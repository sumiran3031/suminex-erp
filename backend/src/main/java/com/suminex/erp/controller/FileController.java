package com.suminex.erp.controller;

import com.suminex.erp.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    /**
     * Serves any file whose relative path is stored in the database (profile photos,
     * assignment files, submission files). The "path" query param is exactly the string
     * already returned by other endpoints (e.g. "uploads/profile-photos/user-3-....jpg"),
     * so the frontend never has to reconstruct paths itself.
     *
     * Path traversal is prevented by normalizing and confirming the resolved path stays
     * within the uploads directory — the same defensive approach as FileStorageService.
     */
    @GetMapping("/api/files")
    public ResponseEntity<Resource> getFile(@RequestParam String path) {
        try {
            Path uploadsRoot = Paths.get("uploads").toAbsolutePath().normalize();
            Path requestedPath = Paths.get(path).toAbsolutePath().normalize();

            if (!requestedPath.startsWith(uploadsRoot)) {
                throw new ResourceNotFoundException("File not found");
            }

            Resource resource = new UrlResource(requestedPath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found");
            }

            String contentType = MediaTypeFactory.getMediaType(resource)
                    .map(MediaType::toString)
                    .orElse("application/octet-stream");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found");
        }
    }
}