package com.suminex.erp.service;

import com.suminex.erp.entity.OtpVerification;
import com.suminex.erp.entity.User;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.OtpVerificationRepository;
import com.suminex.erp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class PasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;
    private static final int MAX_OTP_ATTEMPTS = 3;

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordService(UserRepository userRepository, OtpVerificationRepository otpVerificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.otpVerificationRepository = otpVerificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public String initiateForgotPassword(String email) {
        if (!userRepository.existsByEmail(email)) {
            // Deliberately do not reveal whether the email exists.
            // Caller (controller) will always return the same generic message.
            return null;
        }

        String otp = generateOtp();

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(email);
        otpVerification.setOtpCode(otp);
        otpVerification.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        otpVerification.setUsed(false);
        otpVerification.setAttemptsUsed(0);
        otpVerificationRepository.save(otpVerification);

        // TEMPORARY: log instead of emailing. Replace with real email service later.
        log.info("Password reset OTP for {}: {}", email, otp);

        return otp;
    }

    @Transactional
    public void resetPassword(String email, String submittedOtp, String newPassword) {
        OtpVerification otpVerification = otpVerificationRepository
                .findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new BadRequestException("No active OTP request found for this email"));

        if (otpVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }

        if (otpVerification.getAttemptsUsed() >= MAX_OTP_ATTEMPTS) {
            throw new BadRequestException("Maximum OTP attempts exceeded. Please request a new one.");
        }

        if (!otpVerification.getOtpCode().equals(submittedOtp)) {
            otpVerification.setAttemptsUsed(otpVerification.getAttemptsUsed() + 1);
            otpVerificationRepository.save(otpVerification);
            throw new BadRequestException("Invalid OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpVerification.setUsed(true);
        otpVerificationRepository.save(otpVerification);
    }

    private String generateOtp() {
        int otp = secureRandom.nextInt(900000) + 100000; // always 6 digits
        return String.valueOf(otp);
    }
}