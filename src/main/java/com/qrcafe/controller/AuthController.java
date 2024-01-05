package com.qrcafe.controller;

import com.qrcafe.config.JwtTokenProvider;
import com.qrcafe.dto.*;
import com.qrcafe.entity.EmailDetails;
import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.enums.RolesEnum;
import com.qrcafe.service.EmailService;
import com.qrcafe.service.RoleService;
import com.qrcafe.service.UserService;
import com.qrcafe.utils.ForgetPasswordUtils;
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

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserService userService;
    private final EmailService emailService;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          RoleService roleService,
                          UserService userService,
                          EmailService emailService) {

        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userService = userService;
        this.emailService = emailService;
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

    @PostMapping("/send-email-forget-password")
    public ResponseEntity<String> sendEmailForgetPassword(@RequestBody ForgetPasswordRequestDTO forgetPasswordRequestDTO) {
        User user = userService.getUserByEmail(forgetPasswordRequestDTO.getEmail());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        // send mail
        try {
            String code = ForgetPasswordUtils.getNumericString();
            user.setResetPasswordCode(code);
            user.setCodeExpiration(ForgetPasswordUtils.getCodeExpiration());
            userService.save(user);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Reset password");
            emailDetails.setRecipient(user.getEmail());
            emailDetails.setMsgBody(ForgetPasswordUtils.getEmailBody(code));
            String emailResult = emailService.sendMimeMail(emailDetails);
            return new ResponseEntity<>(emailResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send email reset password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordRequestDTO forgetPasswordRequestDTO) {
        User user = userService.getUserByEmail(forgetPasswordRequestDTO.getEmail());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        try {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (currentTime.before(user.getCodeExpiration())) {
                return new ResponseEntity<>("The verification code has been expired", HttpStatus.CONFLICT);
            }
            if (!user.getResetPasswordCode().equals(forgetPasswordRequestDTO.getResetPasswordCode())) {
                return new ResponseEntity<>("The verification code is not match", HttpStatus.CONFLICT);
            }
            user.setPassword(passwordEncoder.encode(forgetPasswordRequestDTO.getNewPassword()));
            user.setCodeExpiration(null);
            user.setResetPasswordCode(null);
            userService.save(user);
            return new ResponseEntity<>("Your password is updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send email reset password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("change-password")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByUsername(changePasswordRequestDTO.getUserName());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if(!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
            System.out.println("not match: " + !passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword()));
            return new ResponseEntity<>("Old password not true", HttpStatus.CONFLICT);
        }
        try {
            user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
            userService.save(user);
            return new ResponseEntity<>("Your password is updated", HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>("Failed to change password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
