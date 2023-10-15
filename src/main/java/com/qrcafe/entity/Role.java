package com.qrcafe.entity;

import com.qrcafe.enums.RolesEnum;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false)
    private RolesEnum name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;


    public Role(RolesEnum name) {
        this.name = name;
    }
}
