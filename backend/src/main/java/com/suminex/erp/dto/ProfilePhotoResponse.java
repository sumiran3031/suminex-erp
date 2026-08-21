package com.suminex.erp.dto;

public class ProfilePhotoResponse {

    private String profilePhotoPath;

    public ProfilePhotoResponse(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }
}