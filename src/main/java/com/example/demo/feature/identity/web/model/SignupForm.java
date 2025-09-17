package com.example.demo.feature.identity.web.model;

import jakarta.validation.constraints.*;

public class SignupForm {
    
    @NotBlank(message = "表示名を入力してください")
    @Size(max = 30, message = "表示名は30文字以内で入力してください")
    private String displayName;
    
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 64, message = "パスワードは8文字以上64文字以内で入力してください")
    @Pattern(
        regexp = "^[a-zA-Z0-9]+$",
        message = "パスワードは英数字のみで入力してください"
    )
    private String password;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword;

    // ===== getter / setter =====
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
