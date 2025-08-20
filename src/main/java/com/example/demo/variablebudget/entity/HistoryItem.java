package com.example.demo.variablebudget.entity;

import jakarta.persistence.*;

/*
 * 履歴の明細エンティティ（固定費の1行分）
 * - Entry:Item = 1:N の「N」側
 * - 固定費の"1行"を表す
 * - 行順（idx）と金額（amount）だけをまずは保持（名称などは将来拡張）
 * - @ManyToOneで親エントリー（HistoryEntry）にぶら下がる
 * - setEntry()によりHistoryEntry.addItem()のエラーを解消
 * 
 * 拡張：
 * - あとでlabel（固定費名称）を足すのは容易
 */

@Entity
@Table(name = "history_item")
public class HistoryItem {
 
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // 行順（0, 1, 2,...）
   @Column(nullable = false)
   private Integer idx;

   // 金額
   @Column(nullable = false)
   private Long amount;

   // 親エントリー
   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "entry_id", nullable = false)
   private HistoryEntry entry;

   // ===== getter / setter =====
    
   public Long getId() { return id; }
   public void setId(Long id) { this.id = id; }
   
   public Integer getIdx() { return idx; }
   public void setIdx(Integer idx) { this.idx = idx; }

   public Long getAmount() { return amount; }
   public void setAmount(Long amount) { this.amount = amount; }

   public HistoryEntry getEntry() { return entry; }
   public void setEntry(HistoryEntry entry) { this.entry = entry; }
}