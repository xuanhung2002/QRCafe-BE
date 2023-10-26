package com.qrcafe.dto;

import com.qrcafe.entity.Product;
import com.qrcafe.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentRequestDTO {
        private String description;

        private Long productId;
}
