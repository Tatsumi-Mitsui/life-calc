package com.example.demo.variablebudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.variablebudget.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
    
}