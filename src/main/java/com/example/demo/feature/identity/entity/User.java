package com.example.demo.feature.identity.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/*
 * アプリユーザー
 * V2__auth_schema.sql の users テーブルに対応
 */

@Entity
@Table(name = "auth_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /* 画面表示用の名前 */
    @Column(nullable = false, length = 30)
    private String displayName;
    
    /* 認証IDとして使う */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /* パスワード */
    @Column(nullable = false, length = 255)
    private String password;    // BCript想定

    /* アカウント作成日 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /* 権限（ロールはログイン直後に必要なのでEAGER） */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> userRole) {
        this.roles = userRole;
    }
}
