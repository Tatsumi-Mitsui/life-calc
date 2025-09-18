package com.example.demo.feature.variablebudget.dto;

import java.time.OffsetDateTime;
import java.util.List;

/*
 * 履歴の表示用DTO：DBの形に依存しないView向けの読み取り専用データ容器（スナップショット）
 * 
 * ※ Data Transfer Object：
 *    関連するデータを一つにまとめ、異なるプログラム間やコンピュータ間で効率的にデータを転送するためのオブジェクト
 * 
 * - id / savedAt / userId / income / fixedCostTotal / resultVariable はカードに出す基本情報
 * - fixedCosts は「固定費の明細（金額の配列）」を保持（正規化後もここに詰め替えて返す）
 * - 正規化後：HistoryEntry（+items）を読み出して、DTOに組み立てて返す
 * 
 * ※ DB正規化時（HistoryEntry/HistoryItem）でも、このDTOに集約して画面へ渡す
 * 
 * 正規化のメリット：
 * Controller/HTMLはこのDTOだけ見ればOK → DB実装差し替えが容易
 */

public class HistoryView {

        private Long id;                    // 履歴ID
        private OffsetDateTime savedAt;      // 保存日時
        private Long userId;                // ユーザーID（未ログインはnull想定→repo側で0扱いでもOK）
        private Long income;                // 収入
        private List<Long> fixedCosts;      // 固定費明細（行順を維持した金額のリスト）
        private Long fixedCostTotal;        // 固定費合計
        private Long resultVariable;        // 使える変動費

        public HistoryView(Long id,
                           OffsetDateTime savedAt,
                           Long userId,
                           Long income,
                           List<Long> fixedCosts,
                           Long fixedCostTotal,
                           Long resultVariable) {
        
            this.id = id;
            this.savedAt = savedAt;
            this.userId = userId;
            this.income = income;
            this.fixedCosts = fixedCosts;
            this.fixedCostTotal = fixedCostTotal;
            this.resultVariable = resultVariable;
        }

        // ===== getter =====
        public Long getId(){ return id; }
        public OffsetDateTime getSavedAt() { return savedAt; }
        public Long getUserId() { return userId; }
        public Long getIncome() { return income; }
        public List<Long> getFixedCosts() { return fixedCosts; }
        public Long getFixedCostTotal() { return fixedCostTotal; }
        public Long getResultVariable() { return resultVariable; }

        // ===== setter =====
        public void setId(Long id) { this.id = id; }
        public void setSavedAt(OffsetDateTime savedAt) { this.savedAt = savedAt; }
        public void setUserId(Long userId) { this.userId = userId; }
        public void setIncome(Long income) { this.income = income; }
        public void setFixedCosts(List<Long> fixedCosts) { this.fixedCosts = fixedCosts; }
        public void setFixedCostTotal(Long fixedCostTotal) { this.fixedCostTotal = fixedCostTotal; }
        public void setResultVariable(Long resultVariable) { this.resultVariable = resultVariable; }
}
