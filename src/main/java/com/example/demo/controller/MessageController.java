package com.example.demo.controller;

import com.example.demo.dto.MessageDTO;
import com.example.demo.service.impl.MessageServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

  private final MessageServiceImpl messageService;

  public MessageController(MessageServiceImpl messageService) {
    this.messageService = messageService;
  }

  @Operation(summary = "Creation d'un message")
  @PostMapping
  public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody MessageDTO messageDTO) {
    MessageDTO created = messageService.createMessage(messageDTO);
    return ResponseEntity.status(201).body(created);
  }
}
