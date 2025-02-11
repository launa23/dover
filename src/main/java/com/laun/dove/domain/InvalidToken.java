package com.laun.dove.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "invalid_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidToken {
    @Id
    private String id;
    private Date expirationDate;
}
