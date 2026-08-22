package com.suminex.erp.dto;

import jakarta.validation.constraints.NotBlank;

public class RoomRequest {

    @NotBlank(message = "Room name is required")
    private String name;

    private String roomType;

    private Integer capacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}