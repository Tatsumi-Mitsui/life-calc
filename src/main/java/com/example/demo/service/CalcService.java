package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.CostVariable;

@Service
public class CalcService {
	
	public void calcVariable(CostVariable form) {
		// fixedCosts を正規化：null→0、負の数は0、0は残す
		List<Integer> source = form.getFixedCosts();	// getter側がnullなら空リストを返す前提でも安全に
		List<Integer> normalized = (source == null ? List.<Integer>of() : source)
				.stream()
				.map(v -> v == null ? 0 : Math.max(0, v))
				.collect(Collectors.toList());
		
		// 固定費が全て空の場合は最低1件の0を用意（ビューで1行は必ず出す）
		if (normalized.isEmpty()) {
			normalized = new ArrayList<>(List.of(0));
		}
		
		// 正規化結果をフォームに戻す（再描画時に固定費1へ0が入る）
		form.setFixedCosts(normalized);
		
		// 固定費の合計を算出
		int sumFixedCost = normalized.stream()
				.mapToInt(Integer::intValue)
				.sum();
		
		// income ： null の場合は 0 とみなす
		int income = form.getIncome() != null ? form.getIncome() : 0;
		
		// 差額計算
		form.setResultVariable(income - sumFixedCost);
	}

}
