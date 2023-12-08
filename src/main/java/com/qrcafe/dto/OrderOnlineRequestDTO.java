package com.qrcafe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderOnlineRequestDTO {
  private Long id;
  private String note;
  private String location;
  private String phoneNumber;
  private String paymentMethod;
  @JsonProperty("isPaid")
  private boolean isPaid;
  @JsonProperty("cartItemIds")
  private List<Long> cartItemIds;
}
