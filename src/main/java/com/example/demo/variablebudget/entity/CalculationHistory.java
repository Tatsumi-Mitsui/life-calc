package com.example.demo.variablebudget.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "calculation_history")
public class CalculationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer income;

    // カンマ区切りで固定費を保存（例："10000", 8000, 12000"）
    @Column(name = "fixed_costs", length = 2000)
    private String fixedCosts;

    // 合計（保存時にサーバー側で計算して入れる）
    @Column(name = "fixed_cost_total")
    private Integer fixedCostTotal;

    // 変動費（= 結果）
    @Column(name = "result_variable")
	private Integer resultVariable;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // コンストラクタ
    public CalculationHistory() {}

    public CalculationHistory(Integer income, String fixedCosts,Integer fixedCostTotal, Integer resultVariable) {
        this.income = income;
        this.fixedCosts = fixedCosts;
        this.fixedCostTotal = fixedCostTotal;
        this.resultVariable = resultVariable;
    }

    // Getter/Setter
    public Long getId() {
        return id;
    }
    
    public int getIncome() {
        return income;
    }
    
    public void setIncome(Integer income) {
    	this.income = income;
    }
    
    public String getFixedCosts() {
    	return fixedCosts;
    }
    
    public void setFixedCosts(String fixedCosts) {
    	this.fixedCosts = fixedCosts;
    }

    public int getFixedCostTotal() {
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
    
    public LocalDateTime getCreatedAt() {
    	return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
    	this.createdAt = createdAt;
    }
    
}
