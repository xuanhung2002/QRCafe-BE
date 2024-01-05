package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChangePasswordRequestDTO {
    private String userName;
    private String oldPassword;
    private String newPassword;
}
