package com.example.demo.feature.identity.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.feature.identity.entity.User;
import com.example.demo.feature.identity.repository.UserRepository;

@Service
public class UserService {
    
    public static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * 新規ユーザー登録
     * @param displayName 表示名
     * @param email ログインID用のメールアドレス
     * @param rawPassword 平文パスワード
     * @return 保存されたUser 
     */

    
    public User register(String displayName, String email, String rawPassword) {
        // 既に存在するかチェック
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("このメールアドレスは既に使われています");
        }

        // パスワードをエンコード
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // エンティティ生成
        User user = new User();
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        // デフォルトロール付与（ROLE_USER など）
        user.setRole("ROLE_USER");

        // DB保存
        return userRepository.save(user);
    }
}
