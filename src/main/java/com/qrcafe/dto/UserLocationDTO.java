package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLocationDTO {
  private Long id;

  private String fullName;

  private String address;

  private String phoneNumber;

}
