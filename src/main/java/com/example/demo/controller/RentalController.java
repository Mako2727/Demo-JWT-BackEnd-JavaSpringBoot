package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.demo.dto.RentalDetailDTO;
import com.example.demo.dto.RentalRequestDTO;
import com.example.demo.dto.RentalUpdateDTO;
import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.service.RentalService;
import jakarta.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class RentalController {

    private final RentalService rentalService;



public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
}

   @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Map<String, String>> createRental(
        @Valid @ModelAttribute RentalRequestDTO rentalDto,
        @RequestParam("picture") MultipartFile picture) {    
   
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        User user = rentalService.getUserByUsername(username);

        rentalService.createRental(
            rentalDto.getName(),
            rentalDto.getSurface(),
            rentalDto.getPrice(),
            rentalDto.getDescription(),
            picture,
            user.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Rental created !"));

   
}

    @GetMapping
    public ResponseEntity<Map<String, List<RentalDetailDTO>>> getAllRentals() {
        List<RentalDetailDTO> rentals = rentalService.getAllRentals();

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        rentals.forEach(r -> {
            if (r.getPicture() != null && !r.getPicture().isEmpty()) {
                r.setPicture(baseUrl + "/" + r.getPicture());
            }
        });

        return ResponseEntity.ok(Map.of("rentals", rentals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDetailDTO> getRentalById(@PathVariable Long id) {
    
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            Rental rental = rentalService.getRentalById(id);

            RentalDetailDTO dto = new RentalDetailDTO();
            dto.setId(rental.getId());
            dto.setName(rental.getName());
            dto.setSurface(rental.getSurface());
            dto.setPrice(rental.getPrice());
            dto.setDescription(rental.getDescription());
            dto.setPicture(baseUrl + "/" + rental.getPicturePath());
            dto.setOwnerId(rental.getOwner().getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            if (rental.getCreatedAt() != null) {
                dto.setCreated_at(rental.getCreatedAt().format(formatter));
            }
            if (rental.getUpdatedAt() != null) {
                dto.setUpdated_at(rental.getUpdatedAt().format(formatter));
            }

            return ResponseEntity.ok(dto);
       
    }

   @PutMapping("/{id}")
public ResponseEntity<Map<String, String>> updateRental(
        @PathVariable Long id,
        @Valid @ModelAttribute RentalUpdateDTO rentalDto,
        BindingResult bindingResult) {

            if (bindingResult.hasErrors()) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

        rentalService.updateRental(id, rentalDto);
        return ResponseEntity.ok(Map.of("message", "Rental updated !"));    
}

}