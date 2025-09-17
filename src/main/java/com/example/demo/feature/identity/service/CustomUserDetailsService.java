package com.example.demo.feature.identity.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.security.model.CustomUserDetails;
import com.example.demo.feature.identity.entity.Role;
import com.example.demo.feature.identity.entity.User;
import com.example.demo.feature.identity.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // ログ
        System.out.println("loadUserByUsername called with: " + email);

        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        
        Set<GrantedAuthority> authorities = u.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new CustomUserDetails(
            u.getId(),
            u.getEmail(),
            u.getPassword(),
            u.getDisplayName(),
            authorities,
            true,
            true,
            true,
            true
        );
    }
}
