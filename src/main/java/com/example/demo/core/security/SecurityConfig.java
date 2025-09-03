package com.example.demo.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                    "/",            // ホーム（後で作る）
                    "/login", "/signup",        // 認証系画面
                    "/css/**", "/js/**", "/images/**", "/webjars/**",
                    "/variablebudget/**",       // 変動費計算は今の公開のまま
                    "/h2-console/**"            // dev用
                    ).permitAll()
                // それ以外は要ログイン
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")                                // ログイン画面
                .loginProcessingUrl("/login")              // POST /login
                .usernameParameter("email")                 // フォームの name="email"
                .passwordParameter("password")              // フォームの name="password"
                // 直前にブロックされたページがなければ "/" へ
                .defaultSuccessUrl("/", false)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            // 明示的に DaoAuthenticationProvider を差し込む
            .authenticationProvider(daoAuthProvider());

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
