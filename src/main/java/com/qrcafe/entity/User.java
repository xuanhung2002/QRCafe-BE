package com.qrcafe.entity;

import com.qrcafe.oauth2.OAuth2Provider;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column()
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private String fullName;

    @Column
    private Date dateOfBirth;

    private OAuth2Provider oAuth2Provider;

    @Column
    private String resetPasswordCode;

    @Column
    private Timestamp codeExpiration;

    @OneToMany(mappedBy = "user")
    private List<UserLocation> userLocations;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<Vourcher> vourchers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
