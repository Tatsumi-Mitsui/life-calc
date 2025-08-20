package com.example.demo.variablebudget.repository;

import com.example.demo.variablebudget.web.dto.HistoryView;
import org.springframework.stereotype.Repository;

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

@Repository
public class InMemoryHistoryRepository implements HistoryRepository {
    
    // 採番用のIDカウンター（スレッドセーフ）
    private final AtomicLong idGenerator = new AtomicLong(0);

    // userIdごとに履歴を保持
    private final Map<Long, List<HistoryView>> store = new ConcurrentHashMap<>();

    @Override
    public Long save(HistoryView history) {
        Long id = idGenerator.incrementAndGet();
        HistoryView withId = new HistoryView(
            id,
            history.savedAt(),
            history.userId(),
            history.income(),
            history.fixedCosts(),
            history.fixedCostTotal(),
            history.resultVariable()
        );
        store.computeIfAbsent(history.userId(), k -> new ArrayList<>()).add(0, withId); // 新しいものを先頭に追加
        return id;
    }

    @Override
    public List<HistoryView> findRecentByUser(Long userId, int limit) {
        return store.getOrDefault(userId, Collections.emptyList())
                    .stream()
                    .limit(limit)
                    .toList();
    }

    @Override
    public boolean deleteById(Long userId, Long id) {
        List<HistoryView> list = store.get(userId);
        if (list == null) {
            return false;
        }
        return list.removeIf(h -> Objects.equals(h.id(), id));
    }
}
