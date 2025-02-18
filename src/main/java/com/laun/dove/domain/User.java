package com.laun.dove.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laun.dove.domain.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullName;

    @JsonIgnore
    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "email", length = 200, nullable = false)
    @Email
    private String email;

    @Column(name = "status", length = 10)
    private Status status;

//    @Column(name = "avatar", length = 200)
//    private String avatar;

    @Column(name = "role", length = 10)
    private Set<String> roles;


}
