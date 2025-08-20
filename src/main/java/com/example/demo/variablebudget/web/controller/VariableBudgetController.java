package com.example.demo.variablebudget.web.controller;

import com.example.demo.variablebudget.service.HistoryAppService;
import com.example.demo.variablebudget.service.CalcService;
import com.example.demo.variablebudget.web.model.VariableBudgetForm;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/*
 * 画面用コントローラ（最小）
 * - GET /variablebudget: 初期表示（右カラムに履歴だけ出す）
 * - POST /variablebudget/calc: サーバー側で計算して結果を表示（保存はしない）
 * - POST /variablebudget/save: 現在の入力を再計算してから履歴に保存 → 右カラム更新
 * 
 * 責務分離：
 * - 計算は variableBudgetCalcService へ
 * - 保存/取得は HistoryAppService へ
 * これにより Controller は「画面の入出力」にだけ集中する。
 * 
 * 設計：
 * - Controllerを薄く保つことで処理の見通しが良くなり、拡張（DB/JPA化/ログイン連携）が楽になる
 */

@Controller
@RequestMapping("/variablebudget")
public class VariableBudgetController {

    private final CalcService calcService;
    private final HistoryAppService historyService;

    public VariableBudgetController(CalcService calcService,
                                    HistoryAppService historyService) {
        this.calcService = calcService;
        this.historyService = historyService;
    }

    // 初期表示：フォームは空のまま。右の履歴だけ読み込む。
    @GetMapping
    public String show(@ModelAttribute("form") VariableBudgetForm form, Model model) {
        model.addAttribute("histories", historyService.recentByUser(form.getUserId(),5));
        return "variablebudget/variableBudget";
    }

    // 計算だけ実行：保存はしない。結果を画面に反映。
    @PostMapping("/calc")
    public String calc(@Valid @ModelAttribute("form") VariableBudgetForm form, BindingResult br, Model model) {
        if (!br.hasErrors()) {
            calcService.fillTotals(form);  // 収入・固定費から「固定費合計 / 使える変動費」を算出
        }
        model.addAttribute("histories", historyService.recentByUser(form.getUserId(), 5));
        return "variablebudget/variableBudget";
    }

    /*
     * 保存ボタン：現在の入力を「再計算」してから履歴に保存。
     * 右の履歴は保存時にだけ更新される。（計算ボタンとは分離）
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("form") VariableBudgetForm form, BindingResult br, Model model) {
        if (!br.hasErrors()) {
            calcService.fillTotals(form);
            historyService.saveSnapshot(form);  // 内部で再計算 → スナップショット保存
        }
        model.addAttribute("histories", historyService.recentByUser(form.getUserId(),5));
        return "variablebudget/variableBudget";
    }

    // 個別削除：POST /variablebudget/history/delete/{id}
    @PostMapping("/history/delete/{id}")
    public String delete(@PathVariable Long id,
                         @ModelAttribute("form") VariableBudgetForm form,
                         Model model) {
        historyService.deleteById(form.getUserId(), id);
        model.addAttribute("histories", historyService.recentByUser(form.getUserId(), 5));
        return "redirect:/variablebudget";
    }
    
}