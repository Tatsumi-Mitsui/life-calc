package com.example.demo.feature.identity.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    // ログイン画面（Spring Securityのフォームログイン先と合わせる）
    @GetMapping("/login")
    public String login() {
        // templates/auth/login.html
        return "auth/login";
    }
    
    // サインアップ（GET）
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("form", new SignupForm());
        // templates/auth/signup.html
        return "auth/signup";
    }

    // サインアップ（POST）
    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("form") SignupForm form, BindingResult bindingResult, Model model) {
        // 入力バリデーション
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            // UserService 側は「生パスワード」を渡せば中でエンコードして保存する想定
            // 既存のUserServiceのAPIに合わせて呼び出し名は変えてOK
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
