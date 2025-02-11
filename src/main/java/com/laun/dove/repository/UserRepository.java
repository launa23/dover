package com.laun.dove.repository;

import com.laun.dove.domain.User;
import com.laun.dove.domain.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByStatus(Status status);
    Optional<User> findByFullName(String fullName);
    Optional<User> findByEmail(String email);
}
