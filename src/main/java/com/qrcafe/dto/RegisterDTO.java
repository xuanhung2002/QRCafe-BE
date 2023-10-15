package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {
    private String email;
    private String name;
    private String username;
    private String password;

}
