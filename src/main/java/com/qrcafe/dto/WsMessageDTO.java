package com.qrcafe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WsMessageDTO {
    private String message;
    private Object data;
}
