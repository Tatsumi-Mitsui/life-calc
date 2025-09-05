package com.example.demo.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // CustomUserDetailsService が自動登録される想定
    private final UserDetailsService userDetailsService;
    
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 静的と公開ページ
                .requestMatchers(
                    "/",                                // ホーム（後で作る）
                    "/variablebudget","/variablebudget/**",         // 変動費計算
                    "/auth/**",                                     // ログイン/サインアップ画面
                    "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/h2-console/**",                                // dev用H2
                    "/error", "/favicon.ico"
                    ).permitAll()
                    // それ以外は要ログイン
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/auth/login")                                // GET ログイン画面
                .loginProcessingUrl("/auth/login")              // POST 認証エンドポイント
                .usernameParameter("email")                 // <input name="email">
                .passwordParameter("password")              // <input name="password">
                // 直前にブロックされたURLがあれば /auth/login へ、なければ "/" へ
                .defaultSuccessUrl("/variablebudget", false)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            )
            // H2 Console 用
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(h -> h.frameOptions(f -> f.sameOrigin()));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }
}
