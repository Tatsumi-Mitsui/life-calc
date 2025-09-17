package com.example.demo.feature.variablebudget.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.feature.variablebudget.dto.HistoryView;
import com.example.demo.feature.variablebudget.repository.HistoryRepository;
import com.example.demo.feature.variablebudget.web.model.VariableBudgetForm;

import java.time.LocalDateTime;
import java.util.List;

/*
 * 履歴のユースケース（画面の"保存 / 取得 / 削除"）をまとめる層。
 * - 保存ボタン時のスナップショット保存
 * - ユーザー別の直近履歴取得
 * - 履歴の個別削除
 * 
 * 設計：
 * - 一貫性：保存前に必ず再計算して、表示値と保存値のズレを防ぐ
 * - 依存の向き：Controller → HistoryAppSerVice → （CalcService, Repository）
 * - 差し替え：RepositoryはInMemory→JPAに後で置き換え可
 * 
 * 計算は CalcService に委譲してから保存する（値の一貫性を担保）
 */

@Service
public class HistoryAppService {
    
    // 未ログイン = セッション保存（メモリ）
    private final HistoryRepository sessionRepo;
    // ログイン済み = DB保存（JPA）
    private final HistoryRepository jpaRepo;

    private final CalcService calcService;

    public HistoryAppService(
            @Qualifier("sessionHistoryRepository") HistoryRepository sessionRepo,
            @Qualifier("jpaHistoryRepository") HistoryRepository jpaRepo,
            CalcService calcService
    ){
        this.sessionRepo = sessionRepo;
        this.jpaRepo = jpaRepo;
        this.calcService = calcService;
    }

    // 使うリポジトリを userId の有無で振り分け
    private HistoryRepository repoFor(Long userId) {
        return (userId == null || userId == 0L) ? sessionRepo : jpaRepo;
    }

    // 現在の入力を再計算してから、履歴として保存。戻り値は保存ID。
    @Transactional
    public Long saveSnapshot(VariableBudgetForm form) {
        
        // 入力から固定費合計/使える変動費を再計算して確定
        calcService.fillTotals(form);

        // リストはコピーしておく（ミュータブル参照を持たない）
        List<Long> fixedListCopy = form.getFixedCosts() == null
                ? List.of()
                : List.copyOf(form.getFixedCosts());

        HistoryView snapshot = new HistoryView(
                null,                           // id は repo 側で採番
                LocalDateTime.now(),            // 保存日時
                form.getUserId(),               // 将来ログイン導入で差し替え
                form.getIncome(),
                fixedListCopy,
                form.getFixedCostTotal(),
                form.getResultVariable()
        );
        return repoFor(form.getUserId()).save(snapshot);
    }

    // 指定ユーザーの直近履歴を取得（limit件）。未ログインはuserId=null → 0L 扱いは repo に委譲。
    // パフォーマンス最適化のため、読み取り専用
    @Transactional(readOnly = true)
    public List<HistoryView> recentByUser(Long userId, int limit) {
        return repoFor(userId).findRecentByUser(userId, limit);
    }

    // 履歴の個別削除。Controller から userId と id を受け取って repo に委譲。
    @Transactional
    public boolean deleteById(Long userId, Long id) {
        return repoFor(userId).deleteById(userId, id);
    }
}