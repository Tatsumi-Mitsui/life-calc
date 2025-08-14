package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CalculationHistory;
import com.example.demo.model.CostVariable;
import com.example.demo.repository.CalculationHistoryRepository;

@Service
public class CalcService {
	
	private final CalculationHistoryRepository historyRepository;
	
	public CalcService(CalculationHistoryRepository historyRepository) {
		this.historyRepository = historyRepository;
	}
	
	// 保存メソッド（保存ボタンで呼び出す想定）
	public void saveHistory(int income, List<Integer> fixedCosts, int result) {
		String fixedCostsStr = fixedCosts.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(","));
		
		CalculationHistory h = new CalculationHistory(
					income,
					fixedCostsStr,
					result,
					LocalDateTime.now()
				);
		
		historyRepository.save(h);
	}

	// 画面用DTO
	public static class HistoryItem {
		private final Long id;
		private final LocalDateTime createdAt;
		private final int income;
		private final int fixedCostTotal;
		private final int resultVariable;

		public HistoryItem(Long id, LocalDateTime createdAt, int income, int fixedCostTotal, int resultVariable) {
			this.id = id;
			this.createdAt = createdAt;
			this.income = income;
			this.fixedCostTotal = fixedCostTotal;
			this.resultVariable = resultVariable;
		}
		
		public Long getId() {
			return id;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public int getIncome() {
			return income;
		}
		public int getFixedCostTotal() {
			return fixedCostTotal;
		}
		public int getResultVariable() {
			return resultVariable;
		}
	}
	
	// その他履歴取得（表示用）
	public List<HistoryItem> getRecentHistory5() {
		return historyRepository.findTop5ByOrderByCreatedAtDesc()
				.stream()
				.map(h -> new HistoryItem(
					h.getId(),
					h.getCreatedAt(),
					h.getIncome(),
					sumFixedCosts(h.getFixedCosts()),
					h.getResultVariable()
				))
				.toList();
	}

	private int sumFixedCosts(String fixedCostsCsv) {
		if (fixedCostsCsv == null || fixedCostsCsv.isBlank()) return 0;
		return Arrays.stream(fixedCostsCsv.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.mapToInt(Integer::parseInt)
				.map(v -> Math.max(0,v)) // 念のため負数ガード
				.sum();
	}
	
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
