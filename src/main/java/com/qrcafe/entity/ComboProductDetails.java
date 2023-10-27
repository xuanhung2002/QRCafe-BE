package com.qrcafe.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "combo_product_details")
public class ComboProductDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
