package com.example.demo.service;

import com.example.demo.dto.MessageDTO;
import com.example.demo.model.Message;
import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          RentalRepository rentalRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    public MessageDTO createMessage(MessageDTO dto) {
        Rental rental = rentalRepository.findById(dto.getRentalId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Message message = new Message();
        message.setRental(rental);
        message.setUser(user);
        message.setMessage(dto.getMessage());

        Message saved = messageRepository.save(message);

        return convertToDTO(saved);
    }

    private MessageDTO convertToDTO(Message message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setRentalId(message.getRental().getId());
        dto.setUserId(message.getUser().getId());
        dto.setMessage(message.getMessage());
        dto.setCreatedAt(message.getCreatedAt() != null ? message.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(message.getUpdatedAt() != null ? message.getUpdatedAt().format(formatter) : null);
        return dto;
    }
}
