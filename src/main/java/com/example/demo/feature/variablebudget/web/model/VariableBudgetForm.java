package com.example.demo.feature.variablebudget.web.model;

import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

/*
 * 画面の入出力だけを持つ"フォーム用のクラス"（ドメイン/DBは関与しない）
 * - 入力：収入 / 固定費（金額だけ）
 * - 表示：固定費合計 / 使える変動費
 * - 将来：userId（未ログインはnull想定）
 * 
 * 設計：
 * - 型はLongで統一：桁あふれ防止＆サーバ計算と揃えるため
 * - Listは必ず非null返し：Thymeleaf再描画時のNPE回避
 * - 計算はサービス側（VariableBudgetCalcService）で行う
 */

public class VariableBudgetForm {
	
	// ===== 入力 ===== 

	// 収入（null可：未入力）
	@PositiveOrZero(message = "収入は0以上の数値で入力してください")
	private Long income;

	// 固定費の金額（名称は今は扱わない）
	private List<@PositiveOrZero(message = "固定費は0以上の数値で入力してください") Long> fixedCosts = new ArrayList<>();

	// ===== 表示（サーバ計算結果） =====
	
	// 固定費合計
	private Long fixedCostTotal;

	// 使える変動費 = income - fixedCostTotal
	private Long resultVariable;
	
	// ===== 将来用（ログイン導入時に利用） =====
	//未ログインはnullでOK
	private Long userId;

	// --- getter / setter ---
	public Long getIncome() {
		return income;
	}

	public void setIncome(Long income) {
		this.income = income;
	}

	public List<Long> getFixedCosts() {
		// Thymeleafの再描画時のNPE（ヌルポ）防止：必ず非nullを返す
		if (fixedCosts == null) {
			fixedCosts = new ArrayList<>();
		}
		return fixedCosts;
	}

	public void setFixedCosts(List<Long> fixedCosts) {
		// nullでも常に非nullを保持
		this.fixedCosts = (fixedCosts == null) ? new ArrayList<>() : fixedCosts;
	}

	public Long getFixedCostTotal() {
		return fixedCostTotal;
	}

	public void setFixedCostTotal(Long fixedCostTotal) {
		this.fixedCostTotal = fixedCostTotal;
	}

	public Long getResultVariable() {
		return resultVariable;
	}

	public void setResultVariable(Long resultVariable) {
		this.resultVariable = resultVariable;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
