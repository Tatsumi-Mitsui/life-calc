package com.example.demo.feature.identity.repository;

import java.util.Optional;

import com.example.demo.feature.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * ユーザー検索 & 重複チェックの下地
 */

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}
