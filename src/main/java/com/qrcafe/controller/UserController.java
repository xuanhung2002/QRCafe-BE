package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.dto.UserLocationDTO;
import com.qrcafe.entity.User;
import com.qrcafe.entity.UserLocation;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserService userService;
  @Autowired
  Converter converter;


  @GetMapping("/allUsers")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
  public ResponseEntity<?> getAllUsers(){
    List<User> users = userService.getAllUsers();
    if(users != null){
      return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(converter::toUserDTO).toList());
    }else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

  @GetMapping("/userInformation")
  public ResponseEntity<?> getUserInformation(Authentication authentication){
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();
    User user = userService.getUserByUsername(username);
    if (user != null) {
      return ResponseEntity.status(HttpStatus.OK).body(converter.toUserDTO(user));
    }
    else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username is not existed!!");
    }
  }

  @GetMapping("/userLocations")
  public ResponseEntity<?> getAllUserLocations(Authentication authentication){
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    List<UserLocation> userInformations = userService.getAllUserLocationsOfUser(username);
    if(userInformations != null){
      return ResponseEntity.status(HttpStatus.OK).body(userInformations.stream().map(converter::toUserLocationDTO));
    }else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

  @GetMapping("/userLocation/{id}")
  public ResponseEntity<?> getUserLocationById(@PathVariable Long id){
    UserLocation userLocation = userService.getUserLocationByIdLocation(id);
    if(userLocation != null){
      return ResponseEntity.status(HttpStatus.OK).body(converter.toUserLocationDTO(userLocation));
    }else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

  @PostMapping("/addUserLocation")
  public ResponseEntity<?> addUserLocation(Authentication authentication, @RequestBody UserLocationDTO userLocationDTO){
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();
    UserLocation userLocation = userService.addUserLocation(username, userLocationDTO);
    if(userLocation != null){
      return ResponseEntity.status(HttpStatus.OK).body(converter.toUserLocationDTO(userLocation));
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add failed!");
    }
  }

  @PutMapping ("/updateUserLocation")
  public ResponseEntity<?> updateUserLocation(Authentication authentication, @RequestBody UserLocationDTO userLocationDTO){
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    boolean updated = userService.updateUserLocation(username, userLocationDTO);
    if(updated){
      return ResponseEntity.status(HttpStatus.OK).body("Update success");
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed!");
    }
  }
}
