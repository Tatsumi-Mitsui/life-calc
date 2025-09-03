package com.example.demo.feature.identity.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String loginPage() {
        // templates/feature/identity/login.html
        return "feature/identity/login";
    }
    
    // サインアップ画面
    @GetMapping("/signup")
    public String signupPage(Model model) {
        // 既に form が入っていなければ新規で突っ込む
        if (!model.containsAttribute("form")){
            model.addAttribute("form", new SignupForm());
        }
        // templates/feature/identity/signup.html
        return "feature/identity/signup";
    }

    // サインアップ実行
    @PostMapping("/signup")
    public String doSignup(@Valid SignupForm form, BindingResult binding, Model model) {
        
        // 入力エラー
        if (binding.hasErrors()) {
            return "feature/identity/signup";
        }

        try {
            // UserService 側は「生パスワード」を渡せば中でエンコードして保存する想定
            // 既存のUserServiceのAPIに合わせて呼び出し名は変えてOK
            userService.register(form.getDisplayName(), form.getEmail(), form.getPassword());
        } catch (IllegalStateException dup) {
            // メール重複のような業務例外をここで拾う想定
            binding.rejectValue("email", "duplicate", "このメールアドレスは使用できません");
            return "feature/identity/signup";
        }

        return "redirect:/auth/login?registered";
    }
}
