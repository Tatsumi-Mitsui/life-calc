package com.example.demo.variablebudget.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.variablebudget.model.VariableBudgetForm;
import com.example.demo.variablebudget.service.VariableBudgetCalcService;
import com.example.demo.variablebudget.service.VariableBudgetHistoryService;


@Controller
@RequestMapping("/variableBudget")
public class VariableBudgetController {

    private final VariableBudgetCalcService calcService;
    private final VariableBudgetHistoryService historyService;

    public VariableBudgetController(VariableBudgetCalcService calcService,
                                    VariableBudgetHistoryService historyService) {
        this.calcService = calcService;
        this.historyService = historyService;
    }

    // 画面で使うフォームのデフォルト（毎回必ず用意）
    @ModelAttribute("form")
    public VariableBudgetForm setupForm() {
        return new VariableBudgetForm();
    }

    // 初期表示：フォーム ＋ 履歴5件（右側）
    @GetMapping
    public String showVariableForm(Model model) {
        model.addAttribute("recentHistory", historyService.getRecentHistory5());
        return "variablebudget/variableBudget";
    }

    // 計算ボタン：フォームをもとに計算して再描画（保存はしない）
    @PostMapping("/calc")
    public String calc(@ModelAttribute("form") VariableBudgetForm form, Model model) {
        // fixedCosts 正規化 / fixedCostTotal 設定 / resultVariable 設定
        calcService.calc(form);

        // 右側の履歴（直近5件）
        model.addAttribute("recentHistory", historyService.getRecentHistory5());
        
        // 同じ画面で結果表示＆保存ボタンを出す
        return "variablebudget/variableBudget";
    }

    // 保存ボタン：再計算してからDB保存 → 初期表示へリダイレクト（F5 二重投稿防止）
    @PostMapping("/save")
    public String save(@ModelAttribute("form") VariableBudgetForm form) {
        
        // 改ざん対策＆整合性のために再計算（fixedCostTotal / resultVariable も再セット）
        calcService.calc(form);

        // フォームからそのまま保存（createAt は @Prepersist or DB DEFAULT で自動）
        historyService.saveFromForm(form);
        
        return "redirect:/variableBudget";
    }
}