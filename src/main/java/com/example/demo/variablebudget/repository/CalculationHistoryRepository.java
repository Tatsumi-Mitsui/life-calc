package com.example.demo.variablebudget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.variablebudget.entity.CalculationHistory;


@Repository
public interface CalculationHistoryRepository extends JpaRepository<CalculationHistory, Long> {
	
	// 直近5件を取得（作成日時の降順）
	List<CalculationHistory> findTop5ByOrderByCreatedAtDesc();
}
