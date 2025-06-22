package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.RentalDetailDTO;
import com.example.demo.dto.RentalUpdateDTO;
import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.service.RentalService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map; 


@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class RentalController {

     private final RentalService rentalService;
     private final UserRepository userRepository;     
     private RentalRepository rentalRepository;

    public RentalController(RentalService rentalService, UserRepository userRepository, RentalRepository rentalRepository) {
        this.rentalService = rentalService;
         this.userRepository = userRepository;
         this.rentalRepository = rentalRepository;
    }
    
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Map<String, String>> createRental(
        @RequestParam("name") String name,
        @RequestParam("surface") String surface,
        @RequestParam("price") String price,
        @RequestParam("description") String description,
        @RequestParam("picture") MultipartFile picture
) {
    try {
        // Récupère l'utilisateur authentifié
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Récupère l'utilisateur en base à partir du username
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        rentalService.createRental(name, surface, price, description, picture, user.getId());

        // JSON avec "message"
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Rental created !"));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erreur lors de la création de la location : " + e.getMessage()));
    }
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
public ResponseEntity<?> getRentalById(@PathVariable Long id) {
    try {

         String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Rental rental = rentalService.getRentalById(id);

        RentalDetailDTO dto = new RentalDetailDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setDescription(rental.getDescription());
        dto.setPicture(baseUrl + "/" + rental.getPicturePath());

        // ownerId
        dto.setOwnerId(rental.getOwner().getId());

        // Formatage des dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        if (rental.getCreatedAt() != null) {
            dto.setCreated_at(rental.getCreatedAt().format(formatter));
        }
        if (rental.getUpdatedAt() != null) {
            dto.setUpdated_at(rental.getUpdatedAt().format(formatter));
        }

        return ResponseEntity.ok(dto);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

@PutMapping("/{id}")
public ResponseEntity<Map<String, String>> updateRental(
        @PathVariable Long id,
        @ModelAttribute RentalUpdateDTO rentalDto) {

    Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

    // Mise à jour des champs
    rental.setName(rentalDto.getName());
    rental.setSurface(rentalDto.getSurface());
    rental.setPrice(rentalDto.getPrice());
    rental.setDescription(rentalDto.getDescription());

    if (rentalDto.getOwnerId() != null && 
        (rental.getOwner() == null || !rental.getOwner().getId().equals(rentalDto.getOwnerId()))) {
        
        User newOwner = userRepository.findById(rentalDto.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
        rental.setOwner(newOwner);
    }

    rentalRepository.save(rental);

    return ResponseEntity.ok(Map.of("message", "Rental updated !"));
}

private RentalDetailDTO convertToDTO(Rental rental) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    RentalDetailDTO dto = new RentalDetailDTO();
    dto.setId(rental.getId());
    dto.setName(rental.getName());
    dto.setSurface(rental.getSurface());
    dto.setPrice(rental.getPrice());
    dto.setDescription(rental.getDescription());
    dto.setCreated_at(rental.getCreatedAt() != null ? rental.getCreatedAt().format(formatter) : null);
    dto.setUpdated_at(rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(formatter) : null);
    dto.setOwnerId(rental.getOwner().getId());

    String baseUrl = "http://localhost:8081/";
    dto.setPicture(baseUrl + rental.getPicturePath().replace("\\", "/"));

    return dto;
}



}

