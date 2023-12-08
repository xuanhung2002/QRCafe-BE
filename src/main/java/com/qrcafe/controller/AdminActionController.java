package com.qrcafe.controller;

import com.qrcafe.converter.Converter;
import com.qrcafe.entity.User;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adminAction")
public class AdminActionController {

  @Autowired
  UserService userService;
  @Autowired
  Converter converter;


  @GetMapping("/allStaff")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
  public ResponseEntity<?> getAllStaff(){
    List<User> users = userService.getAllStaff();
    if(users != null){
      return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(converter::toUserDTO));
    }else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

  @PostMapping("/grantPermissionForStaff")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseEntity<?> grantPermissionForStaff(@RequestParam(value = "staffUsername", required = true) String staffUsername,
                                                   @RequestParam(value = "role", required = true) String role)
  {
    boolean res = userService.grantPermissionForStaff(staffUsername, role);
    if(res){
      return ResponseEntity.status(HttpStatus.OK).body("Success!!!");
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check inputtt");
    }
  }

  @PutMapping("/deletePermissionOfStaff")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseEntity<?> deletePermissionOfStaff(@RequestParam(value = "staffUsername", required = true) String staffUsername)
  {
    boolean res = userService.deletePermissionOfStaff(staffUsername);
    if(res){
      return ResponseEntity.status(HttpStatus.OK).body("Success!!!");
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check inputtt");
    }
  }

  //new password default: 12345678
  @PutMapping("/resetUserPassword")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseEntity<?> resetUserPassword(@RequestParam(value = "username", required = true) String username){
    userService.resetUserPassword(username);
    return ResponseEntity.status(HttpStatus.OK).body("OKKK");
  }
}
