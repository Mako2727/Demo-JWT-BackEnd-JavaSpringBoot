package com.example.demo.service;



import com.example.demo.dto.MessageDTO;
import com.example.demo.model.Message;


public interface MessageService {


  public MessageDTO createMessage(MessageDTO dto) ;

  public MessageDTO convertToDTO(Message message) ;
}

