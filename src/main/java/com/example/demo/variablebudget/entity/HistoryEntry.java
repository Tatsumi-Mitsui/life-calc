package com.example.demo.variablebudget.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

/*
 * 履歴の親エンティティ（正規化：Entry 1 : N Item）
 * - 「ヘッダ情報」（合計・結果・保存日時など）を保持
 * - 明細（金額行）は HistoryItem 側にぶら下げる
 * 
 * 関連：@OneToMany(mappedBy="entry", cascade=ALL, orphanRemoval=true)で子の自動保存＆削除
 * 順序：@OrderBy("idx ASC")で行順を保証（HistoryItemにidxを持たせる）
 * PrePersist：savedAtなどを保存直前に埋める安全策
 * ヘルパー：addItem/replaceItemsで双方向関連を崩さずに操作
 */

@Entity
@Table(name = "history_entry")
public class HistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 将来のログイン導入用。未ログイン時は 0L などにマッピング可
    @Column(nullable = false)
    private Long userId;

    // 収入
    @Column(nullable = true)
    private Long income;

    // 固定費合計
    @Column(nullable = false)
    private Long fixedCostTotal;

    // 使える変動費
    @Column(nullable = false)
    private Long resultVariable;

    // 保存日時
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime savedAt;

    // 明細（行順は idx 昇順で保持）
    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idx ASC")
    private List<HistoryItem> items = new ArrayList<>();

    // ===== ライフサイクル =====
    @PrePersist
    public void onPrePersist() {
        if (userId == null) userId = 0L;    // 未ログイン時の暫定
        if (fixedCostTotal == null) fixedCostTotal = 0L;
        if (resultVariable == null) resultVariable = 0L;
    }

    // ===== 便利メソッド =====

    // 双方向関連を持ちながら明細を追加（idx は呼び出し元で設定する運用でもOK）
    public void addItem(HistoryItem item) {
        if (item == null) return;
        item.setEntry(this);
        items.add(item);
    }

    // 既存の明細をクリアしてから、与えたリストを丸ごと設定
    public void replaceItems(List<HistoryItem> newItems) {
        items.clear();
        if (newItems == null) return;
        for (HistoryItem it : newItems) addItem(it);
    }

    // ===== getter / setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getIncome() { return income; }
    public void setIncome(Long income) { this.income = income; }
    
    public Long getFixedCostTotal() { return fixedCostTotal; }
    public void setFixedCostTotal(Long fixedCostTotal) { this.fixedCostTotal = fixedCostTotal; }
    
    public Long getResultVariable() { return resultVariable; }
    public void setResultVariable(Long resultVariable) { this.resultVariable = resultVariable; }
    
    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }
    
    public List<HistoryItem> getItems() { return items; }
    public void setItems(List<HistoryItem> items) { this.items = (items == null) ? new ArrayList<>() : items; }
}
