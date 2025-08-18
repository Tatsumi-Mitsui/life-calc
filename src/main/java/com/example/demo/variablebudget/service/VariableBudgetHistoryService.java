package com.example.demo.variablebudget.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.variablebudget.entity.CalculationHistory;
import com.example.demo.variablebudget.model.VariableBudgetForm;
import com.example.demo.variablebudget.repository.CalculationHistoryRepository;

@Service
public class VariableBudgetHistoryService {
    
    private final CalculationHistoryRepository historyRepository;

    public VariableBudgetHistoryService(CalculationHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    // フォームから履歴を保存（念のため再計算＆正規化して整合性確保）
    // createAt は @PrePersist or DBの DEFAULT CURRENT_TIMESTAMP に任せる想定
    public void saveFromForm(VariableBudgetForm form) {
        List<Integer> fixedCosts = normalizeFixedCosts(form.getFixedCosts());
        if (fixedCosts.isEmpty()) fixedCosts = new ArrayList<>(List.of(0));
        
        int fixedTotal = fixedCosts.stream().mapToInt(Integer::intValue).sum();
        int income = nz(form.getIncome());
        int result = income - fixedTotal;

        String fixedCostsStr = fixedCosts.stream()
                .map(String::valueOf) // 正規化済みなのでそのまま
                .collect(Collectors.joining(","));

        CalculationHistory h = new CalculationHistory(
            income,
            fixedCostsStr,
            fixedTotal,
            result
        );
        historyRepository.save(h);
    }

    // 直近5件（新しい順）
    public List<CalculationHistory> getRecentHistory5() {
        return historyRepository.findTop5ByOrderByCreatedAtDesc();
    }

    // 詳細表示用：IDで1件取得
    public CalculationHistory getByID(Long id) {
        return historyRepository.findById(id).orElse(null);
    }

    // helpers
    private List<Integer> normalizeFixedCosts(List<Integer> list) {
        if (list == null) return List.of();
        return list.stream()
                .map(v -> v == null ? 0 : Math.max(0, v))
                .collect(Collectors.toList());
    }

    private int nz(Integer v) {
        return v == null ? 0 : v;
    }
}
