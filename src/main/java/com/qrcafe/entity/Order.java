package com.qrcafe.entity;

import com.qrcafe.enums.OrderStatus;
import com.qrcafe.enums.OrderType;
import com.qrcafe.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private com.qrcafe.entity.Table table;

    @Column(nullable = false)
    private Double totalPrice;

    private String location;

    private String phoneNumber;

    @Column
    private String note;

    @Column(name = "order_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderTime;


    @Column(name = "payment_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column
    private boolean isPaid;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
