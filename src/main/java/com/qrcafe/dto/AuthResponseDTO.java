package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
	public AuthResponseDTO(String accessToken) {
		this.accessToken = accessToken;
	}
}
