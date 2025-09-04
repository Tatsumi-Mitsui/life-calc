package com.example.demo.feature.identity.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.feature.identity.entity.User;
import com.example.demo.feature.identity.entity.Role;

import java.util.Collection;
import java.util.List;

/*
 * フレームワーク契約： getUsername() は「認証ID」を返すメソッド
 * 本プロジェクトでは email を認証IDとして採用
 * アプリ側の可読性向けに getLoginId() も用意
 */

public class CustomUserDetails implements UserDetails {
    
    private final User user;

    public CustomUserDetails(User user) {
        
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        String roleName = (role != null && role.getName() != null) ? role.getName() : "USER";
        // Securityは "ROLE_XXX" 形式の権限を見ることが多いのでここで付与
        String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        return List.of(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();         // 契約上の "username" = 認証ID（email）
    }

    public String getDisplayName() {
        return user.getDisplayName();   // 表示用に displayName を追加で公開
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return this.user;
    }
}
