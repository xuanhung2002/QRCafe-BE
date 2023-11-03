package com.qrcafe.dto;

import com.qrcafe.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentResponseDTO {
    private Long id;
    private String description;
    private String username;

}
