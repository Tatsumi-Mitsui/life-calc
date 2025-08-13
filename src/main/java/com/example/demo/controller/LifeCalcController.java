package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.CalculationHistory;
import com.example.demo.model.CostVariable;
import com.example.demo.service.CalcService;

import jakarta.validation.Valid;

@Controller
public class LifeCalcController {
	
	private final CalcService calcService;	// CalcServiceクラスに対して依存するオブジェクトを外部から注入
	
	// コンストラクタインジェクション
	public LifeCalcController(CalcService calcService) {
		this.calcService = calcService;
	}
	
	// 初期表示（GET） → formに初期値を入れる
	@GetMapping("/variableBudget")
	public String showVariableForm(Model model) {
		model.addAttribute("form", new CostVariable());

		// 履歴を取得してModelに渡す
		List<CalculationHistory> recentHistory = calcService.getRecentHistory();
		model.addAttribute("recentHistory", recentHistory);
		
		return "variableBudget";
	}
	
	// 計算ボタン押下後の処理（POST）
	@PostMapping("/variableBudget/calc")
	public String calcVariable(
			@Valid @ModelAttribute("form") CostVariable form,
			BindingResult bindingResult,
			Model model) {
		
		// 型変換ミス（文字入力など） やBean Validation違反はここで検知
		if (bindingResult.hasErrors()) {
			// エラーがあればそのまま画面に戻す（Thymeleafの th:errors で表示）
			return "variableBudget";	// 同じHTMLに返す （リダイレクトしない）			
		}
		
		calcService.calcVariable(form);	// Serviceで計算処理
		model.addAttribute("form", form);
		return "variableBudget";
	}
	
	// 保存処理（POST）
	@PostMapping("/variableBudget/save")
	public String saveHistory(@ModelAttribute("form") CostVariable form) {
		// 入力値（収入・固定費・結果） をServiceに渡す
		calcService.saveHistory(
				form.getIncome(),
				form.getFixedCosts(),
				form.getResultVariable()
		);
		
		// 保存後は元の画面にリダイレクト（メッセージ表示などは必要に応じて）
		return "redirect:/variableBudget";
	}
	
}
