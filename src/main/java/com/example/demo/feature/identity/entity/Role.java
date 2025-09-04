package com.example.demo.feature.identity.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

/*
 * 権限ロール（例：ROLE_USER, ROLE_ADMIN）
 * V2__auth_schema.sql の roles テーブルに対応
 */

@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 例：ROLE_USER, ROLE_ADMIN
    @Column(nullable = false, unique = true, length = 50)
    private String name;    // "USER" or "ADMIN"

    @CreationTimestamp
    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;

    // ===== getter / setter =====
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id; 
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }
    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}