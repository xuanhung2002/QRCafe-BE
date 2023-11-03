package com.qrcafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qrcafe.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@jakarta.persistence.Table(name = "`table`")
public class Table {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "table")
    private List<Order> orders;
}
