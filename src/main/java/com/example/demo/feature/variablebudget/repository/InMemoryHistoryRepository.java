package com.example.demo.feature.variablebudget.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.feature.variablebudget.dto.HistoryView;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/*
 * メモリ上に履歴を保持する簡易リポジトリ
 * - 本番ではDBに差し替え予定
 * 
 * AtomicLong：保存のたびにIDを自動採番
 * Map<Long, List<HistoryView>>：userIdごとに履歴を保持
 * 先頭追加：新しい履歴をリストの0番目に入れるので常に新しい順
 * 差し替え可：あとでJPAに乗せ替える時もServiceやControllerの修正不要
 */

@Repository("sessionHistoryRepository")
public class InMemoryHistoryRepository implements HistoryRepository {
    
    // userIdごとに履歴を保持
    private final Map<Long, NavigableMap<Long, HistoryView>> store = new ConcurrentHashMap<>();
    
    // 採番用のIDカウンター（スレッドセーフ）
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Long save(HistoryView history) {
        Long id = (history.getId() != null) ? history.getId() : seq.getAndIncrement();
        Long userId = (history.getUserId() == null) ? 0L : history.getUserId();

        var perUser = store.computeIfAbsent(userId, k -> new TreeMap<>(Comparator.reverseOrder()));

        // 破壊的変更の影響を避けるためコピーして格納
        HistoryView snapshot = new HistoryView(
                id,
                history.getSavedAt(),
                userId,
                nz(history.getIncome()),
                (history.getFixedCosts() == null) ? List.of() : List.copyOf(history.getFixedCosts()),
                nz(history.getFixedCostTotal()),
                nz(history.getResultVariable())
        );
        
        perUser.put(id, snapshot);
        return id;
    }

    @Override
    public List<HistoryView> findRecentByUser(Long userId, int limit) {
        Long uid = (userId == null) ? 0L : userId;          // ここで正規化
        var perUser = store.get(uid);
        if (perUser == null) {
            return List.of();
        }
        return perUser.values().stream().limit(Math.max(1, limit)).toList();
    }

    @Override
    public boolean deleteById(Long userId, Long id) {
        Long uid = (userId == null) ? 0L : userId;         //ここでも正規化
        var perUser = store.get(uid);
        if (perUser == null) {
            return false;
        }
        return perUser.remove(id) != null;
    }

    private static Long nz(Long v) {
        return v == null ? 0L : v;
    }
}
