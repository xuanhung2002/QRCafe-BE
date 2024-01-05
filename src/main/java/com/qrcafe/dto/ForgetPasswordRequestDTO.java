package com.qrcafe.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ForgetPasswordRequestDTO {
    private String email;
    private String resetPasswordCode;
    private Timestamp codeExpiration;
    private String newPassword;
}
