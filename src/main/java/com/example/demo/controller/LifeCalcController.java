package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.CostVariable;
import com.example.demo.service.CalcService;

@Controller
public class LifeCalcController {
	
	@Autowired
	private CalcService calcService;	// CalcServiceクラスに対して依存するオブジェクトを外部から注入
	
	public LifeCalcController(CalcService calcService) {
		this.calcService = calcService;
	}
	
	// 初期表示（GET） → formに初期値を入れる
	@GetMapping("/variableBudget")
	public String showVariableForm(Model model) {
		CostVariable form = new CostVariable();
		form.setIncome(null);
		List<Integer> initialFixedCosts = new ArrayList<>();
		initialFixedCosts.add(null);
		form.setFixedCosts(initialFixedCosts);
		model.addAttribute("form", form);
		return "variableBudget";
	}
	
	// 計算ボタン押下後の処理（POST）
	@PostMapping("/variableBudget/calc")
	public String calcVariable(@ModelAttribute("form") CostVariable form, Model model) {
		calcService.calcVariable(form);	// Serviceで計算処理
		model.addAttribute("form", form);	// 再度HTMLに渡す
		return "variableBudget";	// 同じHTMLに返す （リダイレクトしない）
	}
	
}
