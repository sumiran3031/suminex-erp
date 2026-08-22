package com.suminex.erp.dto;

public class RoomResponse {

    private Long id;
    private String name;
    private String roomType;
    private Integer capacity;

    public RoomResponse(Long id, String name, String roomType, Integer capacity) {
        this.id = id;
        this.name = name;
        this.roomType = roomType;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoomType() {
        return roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }
}