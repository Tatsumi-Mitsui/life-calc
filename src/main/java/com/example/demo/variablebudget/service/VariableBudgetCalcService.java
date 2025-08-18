package com.example.demo.variablebudget.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.variablebudget.model.VariableBudgetForm;

@Service
public class VariableBudgetCalcService {
	
	/* 
	 *  変動費の計算を行い、フォームへ結果を反映する。
	 *  - fixedCosts を正規化（null→0、負の数→0）
	 *  - 入力が空なら最低1行（0）を維持（再描画で1行目が消えないように）
	 *  - 合計（fixedCostTotal）と結果（resultVariable）をセット
	 */
	
	// 変動費を計算してフォームにセット
	public void calc(VariableBudgetForm form) {
		// fixedCosts を正規化：null→0、負の数→0
		List<Integer> normalized = normalizeFixedCosts(form.getFixedCosts());
		
		// 少なくとも1行（0）は維持
		if (normalized.isEmpty()) {
			normalized = new ArrayList<>(List.of(0));
		}

		// 合計算出
		int fixedTotal = normalized.stream().mapToInt(Integer::intValue).sum();

		// 収入は null を 0 扱い
		int income = nz(form.getIncome());

		// 結果セット
		form.setFixedCosts(normalized);
		form.setFixedCostTotal(fixedTotal);
		form.setResultVariable(income - fixedTotal);
	}


	// helpers
	// 固定費リストを正規化（null→0、負の数→0）。null リストは空扱い
	private List <Integer> normalizeFixedCosts(List<Integer> list) {
		if (list == null) return List.of();
		return list.stream()
				.map(v -> v == null ? 0 : Math.max(0, v))
				.collect(Collectors.toList());
	}

	// null を 0 扱いするユーティリティ
	private int nz(Integer v) {
		return v == null ? 0 : v;
	}
}
