package com.example.demo.feature.identity.entity;

import jakarta.persistence.*;

/*
 * 権限ロール（例：ROLE_USER, ROLE_ADMIN）
 * V2__auth_schema.sql の roles テーブルに対応
 */

@Entity
@Table(name = "role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 例：ROLE_USER, ROLE_ADMIN
    @Column(nullable = false, unique = true, length = 100)
    private String name;

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
}
