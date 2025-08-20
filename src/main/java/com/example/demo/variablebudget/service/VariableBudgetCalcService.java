package com.example.demo.variablebudget.service;

import com.example.demo.variablebudget.web.model.VariableBudgetForm;
import org.springframework.stereotype.Service;

import java.util.Objects;

/*
 * 計算ロジック専用サービス。Controllerや保存は触らない。
 * - 収入と固定費リストから「固定費合計」「使える変動費」を算出して form に埋める
 * - 画面や保存のことは一切考えない（純粋ロジックに集中）
 * - 副作用でformに書き戻す：テンプレから参照しやすい
 * - null安全：nz()とfilter(Objects::nonNull)でNPE回避
 */
@Service
public class VariableBudgetCalcService {
	
	// 入ってきた form を副作用で更新（固定費合計/使える変動費をセット）
	public void fillTotals(VariableBudgetForm form) {
		long income = nz(form.getIncome());

		long fixedTotal = (form.getFixedCosts() == null) ? 0L
				: form.getFixedCosts().stream()
					.filter(Objects::nonNull)
					.mapToLong(Long::longValue)
					.sum();

		form.setFixedCostTotal(fixedTotal);
		form.setResultVariable(income - fixedTotal);
	}

	// null を 0 扱いするユーティリティ
	private long nz(Long v) {
		return v == null ? 0 : v;
	}
}
