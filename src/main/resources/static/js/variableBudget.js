/**
 * 役割：
 * - 固定費の行を 追加/削除
 * - 行番号と name="fixedCosts[i]" を再採番
 * - 「保存する」時に calc-form の値を save-form へ hidden でコピーしてPOST
 * 
 * 使用している関数
 * - addCost()：テンプレの __INDEX__ / __NUM__ を置換 → appendChildで追加 → reindex()
 * - removeCost(btn)：最小1行を残して削除 → reindex()
 * - reindex()：固定費{n}のラベルと name="fixedCosts[n]"を整える。先頭行の削除ボタンを非表示
 * - handleSave()：calc-form の入力値を save-form にコピーして保存フォームに submit
 * - bindEvents()：HTMLにあるidを手掛かりに、ユーザー操作と処理関数を結びつける
 */

(() => {
    const $ = (sel, root = document) => root.querySelector(sel);
    const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

    const listEl = $('#fixed-cost-list');
    const tplEl = $('#fixed-cost-template');
    const addBtn = $('#add-fixed');
    const saveBtn = $('#save-button');

    const calcForm = $('#calc-form');
    const saveForm = $('#save-form');

    // ----- 行追加 -----
    function addCost() {
        const idx = $$('.fixed-cost-block', listEl).length;     // 次のインデックス
        const num = idx + 1;                                    // 表示番号

        // テンプレを文字列として取り出して置換
        const html = tplEl.innerHTML.replaceAll('__INDEX__', String(idx)).replaceAll('__NUM__', String(num));

        // DOM化して append（付加する）
        const wrap = document.createElement('div');
        wrap.innerHTML = html.trim();
        const block = wrap.firstElementChild;
        listEl.appendChild(block);

        // 最低1行以外は削除ボタンを表示（テンプレは常についているのでOK）
        
        reindex();
    }

    // ----- 行削除（最低1行は残す） -----
    function removeCost(btn) {
        const blocks = $$('.fixed-cost-block', listEl);
        if (blocks.length <= 1) return;                         // 1行は残す

        const block = btn.closest('fixed-cost-block');
        if (block) block.remove();

        reindex();
    }

    // ----- 再採番によって固定費{n}のラベルと name="fixedCosts[n]"を整える＆削除ボタンの表示制御 -----
    function reindex() {
        const blocks = $$('.fixed-cost-block', listEl);

        blocks.forEach((block, i) => {
            // ラベル：固定費{i+1}：
            const label = $('label', block);
            if (label) {
                label.textContent = `固定費${i + 1}：`;
            }

            // 入力の name を fixedCosts[i] に
            const input = $('input[type="number"]', block);
            if (input) {
                input.name = `fixedCosts[${i}]`;
                // input.id は使用していないので付与不要
            }

            // 1行目は削除ボタンを隠す、2行目以降は表示
            const delBtn = $('.delete-btn', block);
            if (delBtn) {
                delBtn.style.display = (i === 0) ? 'none' : '';
                // アイコンが Font Awesome のため、要素自体は残し display で制御
            }
        });
    }

    // ----- 「保存する」：calc-form の入力値を save-form にコピーして submit -----
    function handleSave() {
        if (!calcForm || !saveForm) return;

        // 以前のコピーを削除（CSRF hiddenは残す）
        $$('.__cloned', saveForm).forEach(n => n.remove());

        // 送信用に calc-form の name を持つ要素を収集
        const fields = $$('input[name], select[name], textarea[name]', calcForm);

        fields.forEach (src => {
            // 無効化された項目は無視
            if (src.disabled) return;

            // チェックボックス/ラジオはチェック済みだけ
            if ((src.type === 'checkbox' || src.type === 'radio') && !src.checked) return;

            // 空の固定費は送らない（ノイズ削減）
            if (/^fixedCosts\[\d+\]$/.test(src.name) && (src.value === '' || src.value == null)) return;

            const hidden = document.createElement('input');
            hidden.type = 'hidden';
            hidden.name = src.name;
            hidden.value = src.value;
            hidden.className = '__cloned';
            saveForm.appendChild = (hidden);
        });

        // 送信
        saveForm.submit();
    }

    // ----- イベント束ね -----
    function bindEvents() {
        if (addBtn) addBtn.addEventListener('click', addCost);
        if (saveBtn) saveBtn.addEventListener('click', handleSave);

        // 既存1行の削除ボタン表示制御（初期描画時）
        reindex();

        // 動的行の削除用に window へ公開（テンプレの onclick がこれを呼ぶ）
        window.removeCost = removeCost;
    }

    document.addEventListener('DOMContentLoaded', bindEvents);
})();