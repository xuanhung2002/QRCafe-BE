package com.qrcafe.entity;

import com.qrcafe.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_online")
public class OrderOnline {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String destination;

    @Column
    private String description;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orderOnline")
    private List<OrderDetailOnline> orderDetailOnlines;
}
