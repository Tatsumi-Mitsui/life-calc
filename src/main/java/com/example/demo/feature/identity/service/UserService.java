package com.example.demo.feature.identity.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.feature.identity.entity.Role;
import com.example.demo.feature.identity.entity.User;
import com.example.demo.feature.identity.repository.RoleRepository;
import com.example.demo.feature.identity.repository.UserRepository;
import com.example.demo.feature.identity.web.model.SignupForm;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * 新規ユーザー登録
     * @param displayName 表示名
     * @param email ログインID用のメールアドレス
     * @param rawPassword 平文パスワード
     * @return 保存されたUser 
     */

    @Transactional
    public User register(SignupForm form) {
        // 既に存在するかチェック
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に使われています");
        }

        // "USER" を基本ロール名として採用（DBは素名、Security返却時に ROLE_ を付ける方針）
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ロール 'USER' が未作成"));

        User user = new User();
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setDisplayName(form.getDisplayName());
        user.setRole(userRole);

        // DB保存
        return userRepository.save(user);
    }
}
