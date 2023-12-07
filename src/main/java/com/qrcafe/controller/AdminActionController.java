package com.qrcafe.controller;

import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminAction")
public class AdminActionController {

  @Autowired
  UserService userService;

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
}
