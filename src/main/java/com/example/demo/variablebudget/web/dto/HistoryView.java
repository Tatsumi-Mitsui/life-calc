package com.example.demo.variablebudget.web.dto;

import java.time.LocalDateTime;
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

public record HistoryView(
        Long id,                    // 履歴ID
        LocalDateTime savedAt,      // 保存日時
        Long userId,                // ユーザーID（未ログインはnull想定→repo側で0扱いでもOK）
        Long income,                // 収入
        List<Long> fixedCosts,      // 固定費明細（行順を維持した金額のリスト）
        Long fixedCostTotal,        // 固定費合計
        Long resultVariable         // 使える変動費
) {}
