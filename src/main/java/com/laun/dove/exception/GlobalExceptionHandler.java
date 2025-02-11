package com.laun.dove.exception;

import com.laun.dove.controller.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse apiResponse = new ApiResponse();
        log.error("Error", e);
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode(e.getErrorCode().getCode());
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
        apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        return ResponseEntity.status(ErrorCode.UNAUTHENTICATED.getStatusCode()).body(apiResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            ErrorCode errorCode = ErrorCode.valueOf(e.getFieldError().getDefaultMessage());
            apiResponse.setMessage(errorCode.getMessage());
            apiResponse.setCode(errorCode.getCode());
            return ResponseEntity.badRequest().body(apiResponse);
        }
        catch (IllegalArgumentException ex){
            // Log error here, because this is server error
            log.error("IllegalArgumentException", e);
            apiResponse.setMessage(ErrorCode.INVALID_KEY.getMessage() + ": " + e.getFieldError().getDefaultMessage());
            apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
            return ResponseEntity.internalServerError().body(apiResponse);
        }

    }


}
