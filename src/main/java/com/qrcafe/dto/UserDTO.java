package com.qrcafe.dto;

import com.qrcafe.enums.RolesEnum;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
  private Long id;
  private String username;
  private String fullName;
  private String email;
  private Date dateOfBirth;
  private List<RolesEnum> roles;
}
