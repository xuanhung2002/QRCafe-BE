package com.qrcafe.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDTO {
    private Long id;
    //private LocalDateTime orderTime;
    private LocalDateTime paymentTime;
    private Double totalPrice;


}
