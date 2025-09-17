package com.example.demo.feature.variablebudget.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.feature.variablebudget.dto.HistoryView;
import com.example.demo.feature.variablebudget.entity.HistoryEntry;
import com.example.demo.feature.variablebudget.entity.HistoryItem;

import java.util.ArrayList;
import java.util.List;

@Repository("jpaHistoryRepository")
public class HistoryJpaRepositoryAdapter implements HistoryRepository {

    private final HistoryEntryRepository entryRepo;

    public HistoryJpaRepositoryAdapter(HistoryEntryRepository entryRepo) {
        this.entryRepo = entryRepo;
    }

    @Override
    @Transactional
    public Long save(HistoryView v) {
        if (v.getUserId() == null || v.getUserId() == 0L) {
            throw new IllegalArgumentException("未ログインユーザーはJPA保存できません");
        }
        // DTO → Entity
        HistoryEntry e = new HistoryEntry();
        Long userId = (v.getUserId() == null) ? 0L : v.getUserId();

        e.setUserId(userId);
        e.setIncome(nz(v.getIncome()));
        e.setFixedCostTotal(nz(v.getFixedCostTotal()));
        e.setResultVariable(nz(v.getResultVariable()));
        e.setSavedAt(v.getSavedAt());

        // 明細をぶら下げ
        List<Long> fixeds = (v.getFixedCosts() == null) ? List.of() : v.getFixedCosts();
        for (int i = 0; i < fixeds.size(); i++) {
            HistoryItem it = new HistoryItem();
            it.setIdx(i);
            it.setAmount(nz(fixeds.get(i)));
            it.setEntry(e);
            e.getItems().add(it);
        }

        return entryRepo.save(e).getId();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HistoryView> findRecentByUser(Long userId, int limit) {
        Long uid = (userId == null) ? 0L : userId;
        var entries = entryRepo.findByUserIdOrderBySavedAtDesc(uid, PageRequest.of(0, Math.max(1, limit)));
        var list = new ArrayList<HistoryView>(entries.size());
        for (HistoryEntry e : entries) {
            list.add(toView(e));
        }
        return list;
    }

    @Override
    @Transactional
    public boolean deleteById(Long userId, Long id) {
        Long uid = (userId == null) ? 0L : userId;
        long n = entryRepo.deleteByIdAndUserId(id, uid);
        return n > 0;
    }

    // ===== helper =====

    private static long nz(Long v) {
        return (v == null) ? 0L : v;
    }

    private static HistoryView toView(HistoryEntry e) {
        var costs = new ArrayList<Long>();
        if (e.getItems() != null) {
            e.getItems().forEach(it -> costs.add(it.getAmount()));
        }

        return new HistoryView(
                e.getId(),
                e.getSavedAt(),
                e.getUserId(),
                e.getIncome(),
                costs,
                e.getFixedCostTotal(),
                e.getResultVariable()
        );
    }
}