package com.example.demo.feature.identity.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

/*
 * アプリユーザー
 * V2__auth_schema.sql の users テーブルに対応
 */

@Entity
@Table(name = "app_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /* 認証IDとして使う */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /* パスワード（ハッシュ） */
    @Column(nullable = false, length = 255)
    private String password;    // BCript想定

    /* 画面表示用の名前 */
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    /* 作成日時（全体ルール：savedAt に統一） */
    @CreationTimestamp
    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;

    /* 権限 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "app_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role = new HashSet<>();


    // ===== getter / setter =====

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }
    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    public Set<Role> getRole() {
        return role;
    }
    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
