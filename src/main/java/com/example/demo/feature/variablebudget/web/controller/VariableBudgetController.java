package com.example.demo.feature.variablebudget.web.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.core.context.UserContextService;
import com.example.demo.feature.variablebudget.service.CalcService;
import com.example.demo.feature.variablebudget.service.HistoryAppService;
import com.example.demo.feature.variablebudget.web.model.VariableBudgetForm;
import org.springframework.web.bind.annotation.GetMapping;

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
    private final UserContextService userContextService;
    private final HttpSession session;

    public VariableBudgetController(CalcService calcService,
                                    HistoryAppService historyService,
                                    UserContextService userContextService,
                                    HttpSession session) {
        this.calcService = calcService;
        this.historyService = historyService;
        this.userContextService = userContextService;
        this.session = session;
    }

    @GetMapping
    public String show(Model model) {
        Long userId = userContextService.resolveUserId(session);
        System.out.println("Controller sees userID = " + userId);

        // userId を直接渡す
        model.addAttribute("userId", userId);

        // form の初期化（FlashAttribute から来ている場合は再利用）
        VariableBudgetForm form = (model.getAttribute("form") instanceof VariableBudgetForm f)
            ? f 
            : new VariableBudgetForm();

        form.setUserId(userId);
        model.addAttribute("form", form);

        // 履歴の初期表示（FlashAttribute にhistories がなければ取得）
        if (!model.containsAttribute("histories")) {
            model.addAttribute("histories",historyService.recentByUser(userId, 5));
        }

        return "variablebudget/variablebudget";
    }

    // 計算だけ実行：保存はしない。結果を画面に反映。
    @PostMapping("/calc")
    public String calc(@Valid @ModelAttribute("form") VariableBudgetForm form, BindingResult br, Model model) {
        form.setUserId(userContextService.resolveUserId(session));
        if (!br.hasErrors()) {
            calcService.fillTotals(form);  // 収入・固定費から「固定費合計 / 使える変動費」を算出
        }

        // 履歴を再取得して表示
        model.addAttribute("histories", historyService.recentByUser(form.getUserId(), 5));

        return "variablebudget/variablebudget";
    }

    /*
     * 保存ボタン：現在の入力を「再計算」してから履歴に保存。
     * 右の履歴は保存時にだけ更新される。（計算ボタンとは分離）
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("form") VariableBudgetForm form,
                        BindingResult br,
                        RedirectAttributes ra,
                        Model model) {
        form.setUserId(userContextService.resolveUserId(session));
        if (br.hasErrors()) {
            // バリデーション NG はそのまま再描画（エラー表示のためリダイレクトしない）
            model.addAttribute("histories", historyService.recentByUser(form.getUserId(), 5));
            return "variablebudget/variablebudget";
        }

        // ここで再計算してから保存（サービス側でも二重防御）
        calcService.fillTotals(form);
        historyService.saveSnapshot(form);

        // 入力値をリダイレクト先へ引き継ぎ（画面に残す）
        ra.addFlashAttribute("form", form);
        ra.addFlashAttribute("histories", historyService.recentByUser(form.getUserId(), 5));
        return "redirect:/variablebudget";
    }

    // 個別削除：POST /variablebudget/history/delete/{id}
    @PostMapping("/history/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         @ModelAttribute("form") VariableBudgetForm form,
                         RedirectAttributes ra) {
        form.setUserId(userContextService.resolveUserId(session));
        historyService.deleteById(form.getUserId(), id);

        // 入力値はそのままにして一覧だけ表示したいので form を持ち越す
        ra.addFlashAttribute("form", form);
        return "redirect:/variablebudget";
    }
    
}