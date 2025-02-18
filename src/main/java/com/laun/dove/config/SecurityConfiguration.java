package com.laun.dove.config;

import com.laun.dove.component.CustomAccessDeniedHandler;
import com.laun.dove.component.CustomAuthenticationEntryPoint;
import com.laun.dove.component.CustomJwtDecoder;
import com.laun.dove.domain.enumeration.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    @Value("${api.prefix}")
    private String apiPrefix;

    private String[] publicEndpoints;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomJwtDecoder customJwtDecoder;
    public SecurityConfiguration(CustomAuthenticationEntryPoint authenticationEntryPoint,
                                 CustomAccessDeniedHandler accessDeniedHandler, CustomJwtDecoder customJwtDecoder) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customJwtDecoder = customJwtDecoder;
    }

    @PostConstruct
    public void init() {
        publicEndpoints = new String[] {
                apiPrefix + "/user/create",
                apiPrefix + "/auth/login",
                apiPrefix + "/auth/introspect",
                apiPrefix + "/auth/refresh-token",
                "/ws/**"
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(publicEndpoints).permitAll()
                        .requestMatchers(HttpMethod.GET, apiPrefix + "/user").hasAuthority(Role.ROLE_ADMIN.name())
                        .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        // Xác thực token từ client, dùng customJwtDecoder để xác thực
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        // Tùy chỉnh xử lý lỗi
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return httpSecurity.build();
    }

    // Bean này sẽ xác thực token từ client, nếu token hợp lệ thì cho phép truy cập(hợp lệ khi token được tạo từ secretKey)
    // Có thể custom lại cho phù hợp với yêu cầu của dự án
//    @Bean
//    public JwtDecoder jwtDecoder () {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }

    // Tạo ra một bean để mã hóa password, bởi vì passwordEncoder sẽ đc sử dụng nhiều lần
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // Bean này để set mặc định là không có prefix cho authority(mac định là claim name trong token)
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
