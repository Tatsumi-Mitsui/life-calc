package com.example.demo.feature.variablebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.feature.variablebudget.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
    
}