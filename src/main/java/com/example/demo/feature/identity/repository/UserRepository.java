package com.example.demo.feature.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.feature.identity.entity.User;

/*
 * ユーザー検索 & 重複チェックの下地
 */

public interface UserRepository extends JpaRepository<User, Long> {

    // ログイン用： email で検索（role も同時フェッチしておく）
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    // サインアップ時の重複登録防止チェック用
    boolean existsByEmail(String email);
}
