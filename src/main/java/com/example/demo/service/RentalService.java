package com.example.demo.service;

import java.util.List;



import com.example.demo.dto.RentalDetailDTO;
import com.example.demo.dto.RentalRequestDTO;
import com.example.demo.dto.RentalUpdateDTO;
import com.example.demo.model.Rental;
import com.example.demo.model.User;

public interface RentalService {

   User getUserByUsername(String username) ;
   RentalDetailDTO  getRentalDtoById(Long id) ;

   Rental getRentalById(Long id) ;

   Rental updateRental(Long id, RentalUpdateDTO rentalDto) ;

   void createRental(RentalRequestDTO dto) ;

 
   List<RentalDetailDTO> getAllRentals() ;
}

