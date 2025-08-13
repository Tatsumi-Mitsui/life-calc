package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public class CostVariable {
	
	// 収入：nullはOK（未入力扱い）。入ってくるなら0以上
	@PositiveOrZero(message = "収入は0以上の数値で入力してください")
	private Integer income; // 収入
	
	// 固定費：各要素は0以上（nullは送信前にJSで消す想定だが、保険としてnull許容）
	private List<@PositiveOrZero(message = "固定費は0以上の数値で入力してください") Integer> fixedCosts; // 固定費
	
	private Integer resultVariable; // 変動費予算
	
	public CostVariable() {
		this.income = null;
		this.fixedCosts = new ArrayList<>();
		this.fixedCosts.add(null);
	}
	
	public Integer getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	public List<Integer> getFixedCosts() {
		if(fixedCosts == null) {
			fixedCosts = new ArrayList<>();
		}
		// 空なら必ず1件（null）を入れてレンダリングさせる
		if (fixedCosts.isEmpty()) {
			fixedCosts.add(null);
		}
		return fixedCosts;
	}
	public void setFixedCosts(List<Integer> fixedCosts) {
		if(fixedCosts == null) {
			this.fixedCosts = new ArrayList<>();
		} else {
			this.fixedCosts = fixedCosts;			
		}
	}
	public Integer getResultVariable() {
		return resultVariable;
	}
	public void setResultVariable(Integer resultVariable) {
		this.resultVariable = resultVariable;
	}
	
}
