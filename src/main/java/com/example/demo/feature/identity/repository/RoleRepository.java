package com.example.demo.feature.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.feature.identity.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);     // "USER" / "ADMIN"
}
