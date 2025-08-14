package com.example.demo.repository;

import com.example.demo.entity.CalculationHistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CalculationHistoryRepository extends JpaRepository<CalculationHistory, Long> {
	
	// 直近10件を取得（作成日時の降順）
	List<CalculationHistory> findTop5ByOrderByCreatedAtDesc();
}
