package com.example.demo.feature.variablebudget.repository;

import java.util.List;

import com.example.demo.feature.variablebudget.web.dto.HistoryView;

/*
 * 履歴データのリポジトリインターフェース
 * - DBでもメモリでも、これを実装することで差し替え可能
 * 
 * 責務：データ保存の契約だけ定義（実装は関知しない）
 * 柔軟性：InMemory実装でも、将来のJPA実装でも同じAPIで使える
 * 依存関係：Service層はRepositoryに依存するが、実態は差し替え可能
 */

public interface HistoryRepository {
	
	// 履歴を保存して ID を返す（DBなら採番、メモリなら連番）
	Long save(HistoryView history);

	// ユーザーごとの直近履歴を新しい順で limit 件取得
	List<HistoryView> findRecentByUser(Long userId, int limit);

	// ユーザーごとの履歴を個別削除。成功したら true。
	boolean deleteById(Long userId, Long id);
}
