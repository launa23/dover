package com.laun.dove.controller.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthenticateDto {
    private String email;
    private String password;
}
