package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class CostVariable {
	
	private Integer income; // 収入
	private List<Integer> fixedCosts; // 固定費
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
		return fixedCosts != null ? fixedCosts : new ArrayList<>();
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
