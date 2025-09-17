package com.example.demo.core.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.core.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException {
        System.out.println("CustomLoginSuccessHandler invoked");

        // 認証済みユーザーのIDをセッションに保存
        var principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            System.out.println("userId = " + userDetails.getId());
            request.getSession().setAttribute("userId", userDetails.getId());
        }

        // ログイン後の遷移先
        response.sendRedirect("/variablebudget");
    }
}
