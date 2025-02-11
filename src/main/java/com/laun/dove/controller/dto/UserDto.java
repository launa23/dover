package com.laun.dove.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @Size(min = 3, max = 100, message = "FULL_NAME_INVALID")
    private String fullName;

    @Size(min = 6, max = 200, message = "PASSWORD_INVALID")
    private String password;

    @Email(message = "EMAIL_INVALID")
    private String email;

    private String status;
}
