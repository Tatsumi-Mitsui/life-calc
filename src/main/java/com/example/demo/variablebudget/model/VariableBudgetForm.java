package com.example.demo.variablebudget.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public class VariableBudgetForm {
	
	// 収入：nullはOK（未入力扱い）。入ってくるなら0以上
	@PositiveOrZero(message = "収入は0以上の数値で入力してください")
	private Integer income; // 収入
	
	// 固定費：各要素は0以上（nullは送信前にJSで消す想定だが、保険としてnull許容）
	private List<@PositiveOrZero(message = "固定費は0以上の数値で入力してください") Integer> fixedCosts; // 固定費

	// 固定費合計（表示用）
	private Integer fixedCostTotal;
	
	// 変動費予算
	private Integer resultVariable;
	
	public VariableBudgetForm() {
		// 初期は空リスト（計算時に最低1行の0を補う設計）
		this.fixedCosts = new ArrayList<>();
	}
	
	// getter / setter
	public Integer getIncome() {
		return income;
	}

	public void setIncome(Integer income) {
		this.income = income;
	}

	public List<Integer> getFixedCosts() {
		// Thymeleafの再バインドでNPEにならないように必ず非nullを返す
		if(fixedCosts == null) {
			fixedCosts = new ArrayList<>();
		}
		return fixedCosts;
	}

	public void setFixedCosts(List<Integer> fixedCosts) {
		// nullでも常に非nullを保持
		this.fixedCosts = (fixedCosts == null) ? new ArrayList<>() : fixedCosts;
	}

	public Integer getFixedCostTotal() {
		return fixedCostTotal;
	}

	public void setFixedCostTotal(Integer fixedCostTotal) {
		this.fixedCostTotal = fixedCostTotal;
	}

	public Integer getResultVariable() {
		return resultVariable;
	}
	public void setResultVariable(Integer resultVariable) {
		this.resultVariable = resultVariable;
	}
	
}
