package com.example.demo.service;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.CostVariable;

@Service
public class CalcService {
	
	public void calcVariable(CostVariable form) {
		// nullや0の固定費を削除しておく
		form.setFixedCosts(
			form.getFixedCosts().stream()
			.filter(cost -> cost != null && cost > 0)
			.collect(Collectors.toList())
		);
		
		// null を除いて固定費合計を算出
		int sumFixedCost = form.getFixedCosts().stream()
				.filter(Objects::nonNull)
				.mapToInt(Integer::intValue)
				.sum();
		
		// income が　null の場合は 0 とみなす
		int income = form.getIncome() != null ? form.getIncome() : 0;
		form.setResultVariable(income - sumFixedCost);
	}

}
