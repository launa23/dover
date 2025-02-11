package com.laun.dove.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laun.dove.controller.response.ApiResponse;
import com.laun.dove.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        log.info("Unauthorized error: {}", accessDeniedException.getMessage());

        ApiResponse errorResponse = new ApiResponse();
        errorResponse.setCode(ErrorCode.UNAUTHORIZED.getCode());
        errorResponse.setMessage(ErrorCode.UNAUTHORIZED.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
