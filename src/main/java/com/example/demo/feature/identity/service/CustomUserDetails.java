package com.example.demo.feature.identity.service;

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
    private final String email;             // 認証ID
    private final String password;
    private final String displayName;       // 画面表示用
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired, accountNonLocked, credentialsNonExpired, enabled;

    public CustomUserDetails(Long id,
                             String email,
                             String password,
                             String displayName,
                             Collection<? extends GrantedAuthority> authorities,
                             boolean accountNonExpired,
                             boolean accountNonLocked,
                             boolean credentialsNonExpired,
                             boolean enabled) {
        
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
    
    public String getLoginId() {
        return email;       // アプリ用の別名
    }
    
    public String getDisplayName() {
        return displayName;
    }
   
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;       // 契約上の "username" = 認証ID（email）
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
