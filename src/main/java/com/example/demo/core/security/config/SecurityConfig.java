package com.example.demo.core.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.core.security.handler.CustomLoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // CustomUserDetailsService が自動登録される想定
    private final UserDetailsService userDetailsService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    
    public SecurityConfig(UserDetailsService userDetailsService,
                          CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 静的と公開ページ
                .requestMatchers(
                    "/",                                // ホーム（後で作る）
                    "/variablebudget","/variablebudget/**",         // 変動費計算
                    "/auth/login",                                  // ログイン（GETのみ）
                    "/auth/signup", "/auth/signup/**",               // サインアップ画面
                    "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/h2-console/**",                                // dev用H2
                    "/error", "/favicon.ico"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/auth/login")                                // GET ログイン画面
                .loginProcessingUrl("/auth/login")              // POST 認証エンドポイント
                .successHandler(customLoginSuccessHandler)
                .failureHandler((HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) -> {
                    System.out.println("Login failed: " + ex.getMessage());
                    res.sendRedirect("/auth/login?error");
                })
                .usernameParameter("email")                 // <input name="email">
                .passwordParameter("password")              // <input name="password">
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((HttpServletRequest req, HttpServletResponse res, Authentication auth) -> {
                    String referer = req.getHeader("Referer");
                    res.sendRedirect(referer != null && !referer.isBlank() ? referer : "/");
                })
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
}
