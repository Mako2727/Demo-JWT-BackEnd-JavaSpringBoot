package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.RentalListDTO;
import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import com.example.demo.service.RentalService;
import java.util.List;


@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class RentalController {

     private final RentalService rentalService;
     private final UserRepository userRepository;

    public RentalController(RentalService rentalService, UserRepository userRepository) {
        this.rentalService = rentalService;
         this.userRepository = userRepository;
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture
    ) 
    {
        try {
                // 🔒 Récupère l'utilisateur authentifié
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username;

                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }

                // 🔍 Récupère l'utilisateur en base à partir du username
                User user = userRepository.findByName(username)
                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

                rentalService.createRental(name, surface, price, description, picture, user.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body("Location créée avec succès");

            } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erreur lors de la création de la location : " + e.getMessage());
    } 
    }

@GetMapping
public ResponseEntity<List<RentalListDTO>> getAllRentals() {
    List<RentalListDTO> rentals = rentalService.getAllRentals();
    return ResponseEntity.ok(rentals);
}


}
