package com.qrcafe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.qrcafe.enums.OrderStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@jakarta.persistence.Table(name = "order_offline")
public class OrderOffline {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String note;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Table table;

    @OneToMany(mappedBy = "orderOffline")
    private List<OrderDetailOffline> orderDetailOfflines;

}
