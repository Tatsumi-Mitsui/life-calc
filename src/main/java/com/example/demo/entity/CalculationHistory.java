package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "caluculationhistory")
public class CalculationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer income;

    // カンマ区切りで固定費を保存（例："10000", 8000, 12000"）
    private String fixedCosts;


	private Integer resultVariable;

    private LocalDateTime createdAt;

    // コンストラクタ
    public CalculationHistory() {}

    public CalculationHistory(int income, String fixedCosts, int resultVariable, LocalDateTime createdAt) {
        this.income = income;
        this.fixedCosts = fixedCosts;
        this.resultVariable = resultVariable;
        this.createdAt = createdAt;
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
