package com.suminex.erp.controller;

import com.suminex.erp.dto.ProfilePhotoResponse;
import com.suminex.erp.entity.User;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.UserRepository;
import com.suminex.erp.security.CustomUserDetails;
import com.suminex.erp.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    public ProfileController(FileStorageService fileStorageService, UserRepository userRepository) {
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/photo", consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<ProfilePhotoResponse> uploadProfilePhoto(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        Long userId = userDetails.getUserId();

        String storedPath = fileStorageService.storeProfilePhoto(userId, file);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setProfilePhoto(storedPath);
        userRepository.save(user);

        return ResponseEntity.ok(new ProfilePhotoResponse(storedPath));
    }
}