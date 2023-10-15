package com.qrcafe.controller;

import com.qrcafe.config.JwtTokenProvider;
import com.qrcafe.dto.AuthResponseDTO;
import com.qrcafe.dto.LoginDTO;
import com.qrcafe.dto.RegisterDTO;
import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.enums.RolesEnum;
import com.qrcafe.service.RoleService;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserService userService;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          RoleService roleService,
                          UserService userService) {

        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = JwtTokenProvider.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto) {
        if (userService.existedByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.CONFLICT);
        }
        else {
            User user = new com.qrcafe.entity.User();

            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
            user.setEmail(registerDto.getEmail());

            Role role = roleService.getRoleByRoleName(RolesEnum.CUSTOMER);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            userService.save(user);

            return new ResponseEntity<>("User registered success!", HttpStatus.OK);
        }
    }
}
