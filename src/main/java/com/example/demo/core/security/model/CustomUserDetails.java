package com.example.demo.core.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/*
 * フレームワーク契約： getUsername() は「認証ID」を返すメソッド
 * 本プロジェクトでは email を認証IDとして採用
 * アプリ側の可読性向けに getLoginId() も用意
 */

public class CustomUserDetails implements UserDetails {
    
    private final Long id;
    private final String email;
    private final String password;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CustomUserDetails(
        Long id,
        String email,
        String password,
        String displayName,
        Collection<? extends GrantedAuthority> authorities,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    // ===== UserDetails =====
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Spring Securityは「ユーザー名」を返すメソッド名が getUsername 固定
    // ※ログインIDとして email を使っているため email を返す
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
}
