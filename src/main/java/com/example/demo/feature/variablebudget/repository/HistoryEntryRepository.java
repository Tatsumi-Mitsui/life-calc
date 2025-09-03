package com.example.demo.feature.variablebudget.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.feature.variablebudget.entity.HistoryEntry;

import java.util.List;


public interface HistoryEntryRepository extends JpaRepository<HistoryEntry, Long> {
    
    // ユーザー別の新しい順。件数はPageableで可変にする
    List<HistoryEntry> findByUserIdOrderBySavedAtDesc(Long userId, Pageable pageable);

    // ユーザーを絞って安全に削除
    long deleteByIdAndUserId(Long id, Long userId);
}
