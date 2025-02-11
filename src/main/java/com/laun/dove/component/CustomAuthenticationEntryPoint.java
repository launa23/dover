package com.laun.dove.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laun.dove.controller.response.ApiResponse;
import com.laun.dove.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.info("Unauthorized error: {}", authException.getMessage());
        ApiResponse errorResponse = new ApiResponse<>();
        errorResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        errorResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
