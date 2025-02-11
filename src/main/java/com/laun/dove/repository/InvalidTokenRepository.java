package com.laun.dove.repository;

import com.laun.dove.domain.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
    void deleteInvalidTokenByExpirationDateBefore(Date now);
}
