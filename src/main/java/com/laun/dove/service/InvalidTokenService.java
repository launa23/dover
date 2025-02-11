package com.laun.dove.service;

import com.laun.dove.repository.InvalidTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class InvalidTokenService {
    private final InvalidTokenRepository invalidTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteInvalidToken() {
        invalidTokenRepository.deleteInvalidTokenByExpirationDateBefore(new Date());
    }

}
