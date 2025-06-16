package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Rental;
import com.example.demo.repository.RentalRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.example.demo.model.User;  
import com.example.demo.repository.UserRepository; 

@Service
public class RentalService {

     private final RentalRepository rentalRepository;
     private final UserRepository userRepository;
     

    private static final String UPLOAD_DIR = "uploads/";

   public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    public void createRental(String name, String surface, String price, String description, MultipartFile picture, Long ownerId) {

           try {
        // Conversion
        int surfaceInt = Integer.parseInt(surface);
        double priceDouble = Double.parseDouble(price);

        // Sauvegarde de l'image (fichier)
        String picturePath = savePicture(picture);

        // Recherche de l'utilisateur propriétaire
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + ownerId));

        // Création de l’entité Rental
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surfaceInt);
        rental.setPrice(priceDouble);
        rental.setDescription(description);
        rental.setPicturePath(picturePath);
        rental.setOwner(owner);

        // Enregistrement en base
        rentalRepository.save(rental);

    } catch (NumberFormatException e) {
        throw new RuntimeException("Erreur de conversion : surface ou price invalide", e);
    }  catch (Exception e) {
        throw new RuntimeException("Erreur lors de la création de la location", e);
    }

    }

    private String savePicture(MultipartFile picture) {
        if (picture.isEmpty()) return null;

        try {
            byte[] bytes = picture.getBytes();
            String fileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // Crée le dossier s’il n’existe pas
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            Files.write(path, bytes);

            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
        }
    }
}
