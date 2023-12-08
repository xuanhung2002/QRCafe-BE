package com.qrcafe.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
  private String username;
  private String fullName;
  private String email;
  private Date dateOfBirth;
}
