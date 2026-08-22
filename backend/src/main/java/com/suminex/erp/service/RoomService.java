package com.suminex.erp.service;

import com.suminex.erp.dto.RoomRequest;
import com.suminex.erp.dto.RoomResponse;
import com.suminex.erp.entity.Room;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByName(request.getName())) {
            throw new ConflictException("A room with this name already exists");
        }

        Room room = new Room();
        room.setName(request.getName());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        Room saved = roomRepository.save(room);

        return toResponse(saved);
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(room.getId(), room.getName(), room.getRoomType(), room.getCapacity());
    }
}