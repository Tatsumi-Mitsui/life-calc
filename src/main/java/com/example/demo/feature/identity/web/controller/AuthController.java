package com.example.demo.feature.identity.web.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.example.demo.feature.identity.service.UserService;
import com.example.demo.feature.identity.web.model.SignupForm;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    // ログイン画面（Spring Securityのフォームログイン先と合わせる）
    @GetMapping("/login")
    public String login(Authentication auth) {
        if (isLoggedIn(auth)) {
            return "redirect:/variablebudget";  // 今後の拡張で "redirect:/"
        }
        return "auth/login";
    }
    
    // サインアップ（GET）
    @GetMapping("/signup")
    public String signupForm(Authentication auth, Model model) {
        if (isLoggedIn(auth)) {
            return "redirect:/variablebudget";  // 今後の拡張で "redirect:/"
        }
        model.addAttribute("form", new SignupForm());
        return "auth/signup";
    }

    // サインアップ（POST）
    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("form") SignupForm form,
                           BindingResult bindingResult,
                           Model model,
                           Authentication auth) {
        
        if (isLoggedIn(auth)) {
            return "redirect:/variablebudget";  // 今後の拡張で "redirect:/"
        }
        
        // パスワード一致チェック（field単位で出すなら confirmPassword に付ける）
        if (!bindingResult.hasFieldErrors("password")
            && !bindingResult.hasFieldErrors("confirmPassword")
            && (form.getPassword() == null || !form.getPassword().equals(form.getConfirmPassword()))) {
                bindingResult.rejectValue("confirmPassword", "mismatch", "パスワードが一致しません");
            }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            userService.register(form);
        } catch (IllegalArgumentException dup) {
            // 例：メール重複など
            bindingResult.rejectValue("email", "duplicate", "このメールアドレスは使用できません");
            return "auth/signup";
        } catch (IllegalStateException noRole) {
            // 例：ロール未投入
            bindingResult.reject("role_missing", noRole.getMessage());
            return "auth/signup";
        }

        // サインアップ完了 -> ログイン画面へ（クエリで完了メッセージ）
        return "redirect:/auth/login?registered";
    }
}
