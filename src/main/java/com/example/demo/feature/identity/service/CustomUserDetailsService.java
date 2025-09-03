package com.example.demo.feature.identity.service;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.demo.feature.identity.entity.Role;
import com.example.demo.feature.identity.entity.User;
import com.example.demo.feature.identity.repository.UserRepository;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("not found: " + email));
        
        var authorities = u.getRoles().stream()
                    .map(Role::getName)
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        
        return new CustomUserDetails(
                u.getId(),
                u.getEmail(),
                u.getPassword(),
                u.getDisplayName(),
                authorities,
                true, true, true, true
        );
    }
}
