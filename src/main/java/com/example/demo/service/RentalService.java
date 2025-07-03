package com.example.demo.service;

import com.example.demo.dto.RentalDetailDTO;
import com.example.demo.dto.RentalRequestDTO;
import com.example.demo.dto.RentalUpdateDTO;
import com.example.demo.model.CustomUserDetails;
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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

  public RentalDetailDTO  getRentalDtoById(Long id) {
  Rental rental = rentalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Location non trouvée avec l'id : " + id));

    RentalDetailDTO dto = new RentalDetailDTO();
    dto.setId(rental.getId());
    dto.setName(rental.getName());
    dto.setSurface(rental.getSurface());
    dto.setPrice(rental.getPrice());
    dto.setDescription(rental.getDescription());

    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    dto.setPicture(rental.getPicturePath() != null ? baseUrl + "/" + rental.getPicturePath() : null);

    dto.setOwnerId(rental.getOwner() != null ? rental.getOwner().getId() : null);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    if (rental.getCreatedAt() != null) {
        dto.setCreated_at(rental.getCreatedAt().format(formatter));
    }
    if (rental.getUpdatedAt() != null) {
        dto.setUpdated_at(rental.getUpdatedAt().format(formatter));
    }

    return dto;
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

  public void createRental(RentalRequestDTO dto) {
    int surfaceInt;
    double priceDouble;
 

        String email;
       Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      
        email = ((CustomUserDetails) principal).getEmail();
       User owner = userRepository.findByEmail(email).get();

      surfaceInt =dto.getSurface();
      priceDouble =dto.getPrice();
    String picturePath = savePicture(dto.getPicture());

   

    Rental rental = new Rental();
    rental.setName(dto.getName());
    rental.setSurface(surfaceInt);
    rental.setPrice(priceDouble);
    rental.setDescription(dto.getDescription());
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
String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
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
              dto.setCreated_at(r.getCreatedAt() != null ? r.getCreatedAt().format(formatter) : null);
              dto.setUpdated_at(r.getUpdatedAt() != null ? r.getUpdatedAt().format(formatter) : null);
              dto.setOwnerId(r.getOwner() != null ? r.getOwner().getId() : null);


               if (dto.getPicture() != null && !dto.getPicture().isEmpty()) {
            dto.setPicture(baseUrl + "/" + dto.getPicture());
        }


              return dto;
            })
        .collect(Collectors.toList());
  }
}
