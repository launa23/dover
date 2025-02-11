package com.laun.dove.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1005, "User's email is already", HttpStatus.BAD_REQUEST),
    FULL_NAME_INVALID(1006, "Full name must be between 3 and 100 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1007, "Password must be between 6 and 200 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1008, "Email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1009, "Email not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1010, "User authentication failed", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1011, "You do not have permission", HttpStatus.FORBIDDEN),
    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
