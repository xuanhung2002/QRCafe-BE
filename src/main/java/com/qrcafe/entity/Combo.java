package com.qrcafe.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "combo")
public class Combo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    private String description;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    private Set<ComboProductDetails> comboProductDetails = new HashSet<>();

    @OneToMany(mappedBy = "combo")
    private List<ComboDetails> comboDetails = new ArrayList<>();
}
