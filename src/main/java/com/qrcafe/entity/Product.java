package com.qrcafe.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Comment> comments;

    @OneToMany(mappedBy = "product")
    private List<OrderDetailOnline> orderDetailOnlines;

    @OneToMany(mappedBy = "product")
    private List<OrderDetailOffline> orderDetailOfflines;

    @ManyToMany(mappedBy = "products")
    private Set<Combo> combos;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

}
