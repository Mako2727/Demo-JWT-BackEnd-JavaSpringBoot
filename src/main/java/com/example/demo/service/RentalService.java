package com.example.demo.service;

import com.example.demo.dto.RentalDetailDTO;
import com.example.demo.dto.RentalUpdateDTO;
import com.example.demo.model.Rental;
import com.example.demo.model.User;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RentalService {

  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;

  private static final String UPLOAD_DIR = "uploads/";

  public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
    this.rentalRepository = rentalRepository;
    this.userRepository = userRepository;
  }

  public User getUserByUsername(String username) {
    return userRepository
        .findByName(username)
        .orElseThrow(
            () -> new RuntimeException("Utilisateur non trouvé avec le nom : " + username));
  }

  public Rental getRentalById(Long id) {
    return rentalRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Location non trouvée avec l'id : " + id));
  }

  public Rental updateRental(Long id, RentalUpdateDTO rentalDto) {
    Rental rental = getRentalById(id);

    rental.setName(rentalDto.getName());
    rental.setSurface(rentalDto.getSurface());
    rental.setPrice(rentalDto.getPrice());
    rental.setDescription(rentalDto.getDescription());

    if (rentalDto.getOwnerId() != null
        && (rental.getOwner() == null
            || !rental.getOwner().getId().equals(rentalDto.getOwnerId()))) {
      User newOwner =
          userRepository
              .findById(rentalDto.getOwnerId())
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "Owner non trouvé avec l'ID : " + rentalDto.getOwnerId()));
      rental.setOwner(newOwner);
    }

    return rentalRepository.save(rental);
  }

  public void createRental(
      String name,
      String surface,
      String price,
      String description,
      MultipartFile picture,
      Long ownerId) {
    int surfaceInt;
    double priceDouble;

    try {
      surfaceInt = Integer.parseInt(surface);
      priceDouble = Double.parseDouble(price);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Surface ou price invalide", e);
    }
    String picturePath = savePicture(picture);

    User owner =
        userRepository
            .findById(ownerId)
            .orElseThrow(
                () -> new RuntimeException("Utilisateur introuvable avec l'ID : " + ownerId));

    Rental rental = new Rental();
    rental.setName(name);
    rental.setSurface(surfaceInt);
    rental.setPrice(priceDouble);
    rental.setDescription(description);
    rental.setPicturePath(picturePath);
    rental.setOwner(owner);

    rentalRepository.save(rental);
  }

  private String savePicture(MultipartFile picture) {
    if (picture.isEmpty()) return null;

    try {
      byte[] bytes = picture.getBytes();

      LocalDate now = LocalDate.now();
      String year = String.valueOf(now.getYear());
      String month = String.format("%02d", now.getMonthValue());

      String dirPath = UPLOAD_DIR + year + "/" + month + "/";
      File dir = new File(dirPath);
      if (!dir.exists()) dir.mkdirs();

      String fileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();
      Path path = Paths.get(dirPath + fileName);

      Files.write(path, bytes);

      return path.toString().replace("\\", "/");
    } catch (IOException e) {
      throw new RuntimeException("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
    }
  }

  public List<RentalDetailDTO> getAllRentals() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    return rentalRepository.findAll().stream()
        .map(
            r -> {
              RentalDetailDTO dto = new RentalDetailDTO();
              dto.setId(r.getId());
              dto.setName(r.getName());
              dto.setSurface(r.getSurface());
              dto.setPrice(r.getPrice());
              dto.setDescription(r.getDescription());
              dto.setPicture(r.getPicturePath());
              dto.setCreated_at(
                  r.getCreatedAt() != null ? r.getCreatedAt().format(formatter) : null);
              dto.setUpdated_at(
                  r.getUpdatedAt() != null ? r.getUpdatedAt().format(formatter) : null);
              dto.setOwnerId(r.getOwner() != null ? r.getOwner().getId() : null);
              return dto;
            })
        .collect(Collectors.toList());
  }
}
