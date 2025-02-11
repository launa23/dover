package com.laun.dove.controller;

import com.laun.dove.controller.response.ApiResponse;
import com.laun.dove.controller.dto.AuthenticateDto;
import com.laun.dove.controller.response.AuthenticationResponse;
import com.laun.dove.domain.User;
import com.laun.dove.service.AuthenticationService;
import com.laun.dove.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Value("${cookie.max_age}")
    private int maxAge;
    private final AuthenticationService authenticateService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticateDto authenticateDto) {

        Map<String, String> tokens = authenticateService.authenticated(authenticateDto);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.get("refreshToken"))
                .httpOnly(true)
                .path("/")      // toàn bộ các request đều gửi cookie này
                .maxAge(maxAge)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.<AuthenticationResponse>builder()
                .data(AuthenticationResponse.builder()
                        .token(tokens.get("accessToken"))
                        .authenticated(true)
                        .build())
                .build());
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User user = userService.getCurrentUserLogin();
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .data(user)
                .build());
    }

    @PostMapping("refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(@CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
        Map<String, String> tokens = null;
        try {
            tokens = authenticateService.refreshToken(refreshToken);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.get("refreshToken"))
                .httpOnly(true)
                .path("/")      // toàn bộ các request đều gửi cookie này
                .maxAge(60 * 60 * 24 * 30)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.<AuthenticationResponse>builder()
                        .data(AuthenticationResponse.builder()
                                .token(tokens.get("accessToken"))
                                .authenticated(true)
                                .build())
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authorization)
            throws ParseException, JOSEException {
        try {
            String token = authorization.substring(7);
            authenticateService.logout(token);
            return ResponseEntity.ok(ApiResponse.<Void>builder().build());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        } catch (JOSEException e) {
            throw new JOSEException(e.getMessage());
        }
    }

    @PostMapping("/introspect")
    public ResponseEntity<Boolean> introspect(@RequestBody String token) {
        try {
            return ResponseEntity.ok(authenticateService.introspect(token));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
