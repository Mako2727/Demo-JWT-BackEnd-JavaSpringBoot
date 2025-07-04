package com.example.demo.controller;

import com.example.demo.dto.RentalDetailDTO;
import com.example.demo.dto.RentalRequestDTO;
import com.example.demo.dto.RentalUpdateDTO;
import com.example.demo.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class RentalController {

  private final RentalService rentalService;

  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

   @Operation(summary = "Creation d'un rental")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String, String>> createRental(
      @Valid @ModelAttribute RentalRequestDTO rentalDto) {
    rentalService.createRental(rentalDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Rental created !"));
  }

   @Operation(summary = "Obtient tout les rentals")
  @GetMapping
  public ResponseEntity<Map<String, List<RentalDetailDTO>>> getAllRentals() {
    List<RentalDetailDTO> rentals = rentalService.getAllRentals();
    return ResponseEntity.ok(Map.of("rentals", rentals));
  }

 
  @Operation(summary = "Recuperation d un erntal par ID")
  @GetMapping("/{id}")
  public ResponseEntity<RentalDetailDTO> getRentalById(@PathVariable Long id) {
    RentalDetailDTO rental=rentalService.getRentalDtoById(id);
   return ResponseEntity.ok(rental);
  }

   @Operation(summary = "Mise a jour d'un rental")
  @PutMapping("/{id}")
  public ResponseEntity<Map<String, String>> updateRental(
      @PathVariable Long id,
      @Valid @ModelAttribute RentalUpdateDTO rentalDto,
      BindingResult bindingResult) {

    rentalService.updateRental(id, rentalDto);
    return ResponseEntity.ok(Map.of("message", "Rental updated !"));
  }
}
