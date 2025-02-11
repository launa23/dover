package com.laun.dove.config;

import com.laun.dove.domain.User;
import com.laun.dove.domain.enumeration.Role;
import com.laun.dove.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitConfiguration {
    private final PasswordEncoder passwordEncoder;

    // Khởi tạo dữ liệu ban đầu
    @Bean
    public ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            // init data
            if (userRepository.findByFullName("admin").isEmpty()) {

                Set<String> roles = new HashSet<>();
                roles.add(Role.ROLE_ADMIN.name());
                roles.add(Role.ROLE_USER.name());
                userRepository.save(User.builder()
                        .email("admin@laun.com")
                        .fullName("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build());
                log.info("Admin user created with password: admin");
            }

        };
    }


}
