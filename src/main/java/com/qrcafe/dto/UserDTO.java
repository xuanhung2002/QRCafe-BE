package com.qrcafe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM")
  private Date dateOfBirth;
  private List<RolesEnum> roles;
}
