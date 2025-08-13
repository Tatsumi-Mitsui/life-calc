function setupValidation(el){
	// 入力中は数字以外（e,+,-,.など）を即除去
	el.addEventListener('input',() => {
		el.value = el.value.replace(/[^\d]/g, '');
		el.setCustomValidity('');
	});
	
	// ブラウザ既定のエラーメッセージを日本語で上書き
	el.addEventListener('invalid', () => {
		if (el.validity.valueMissing) {
			el.setCustomValidity('入力してください。');
		} else if(el.validity.badInput) {
			el.setCustomValidity('半角の数字で入力してください。');
		} else if(el.validity.rangeUnderflow || el.validity.stepMismatch){
			el.setCustomValidity('0以上の整数を入力してください。');
		} else {
			el.setCustomValidity('入力を確認してください。');
		}
		el.reportValidity();
	});
	
	// 次の入力に備えてクリア
	el.addEventListener('blur', () => el.setCustomValidity(''));
}

function addCost() {
	const form = document.querySelector("form");
	
	// 現在の固定費欄の数をカウント
	const fixedCosts = form.querySelectorAll("input[name^='fixedCosts']");
	const index = fixedCosts.length;

	// ラッパー div を作成
	const wrapper = document.createElement("div");
	wrapper.classList.add("input-block", "fixed-cost-block");	// 追加欄でも中央寄せ防止
	
	// 一意なID
	const id = `fixedCost${index}`;

	// 固定費ラベルと入力欄＋削除ボタン（1個目は削除ボタンなし）
	wrapper.innerHTML = `
		<label for="${id}">固定費${index + 1}：</label>
		<div class="input-row">
			<span>￥</span>
			<input
				id="${id}"
				type="number"
				inputmode="numeric"
				pattern="\\d*"
				min="0"
				step="1"
				name="fixedCosts[${index}]"
				value=""
				placeholder="例：10000" />
			${index > 0 ? `
			<button type="button" onclick="removeCost(this)" class="delete-btn">
				<i class="fa-solid fa-circle-minus icon-minus"></i>
			</button>` : ''}
		</div>
	`;

	// 追加ボタンの前に挿入
	const addBtnWrapper = form.querySelector(".add-button-wrapper");
	form.insertBefore(wrapper, addBtnWrapper);
	
	// 追加した入力にもバリデーション付与
	const newInput = wrapper.querySelector('input[type="number"][inputmode="numeric"]');
	if (newInput) {
		setupValidation(newInput);
	}
}

function removeCost(button) {
	const block = button.closest(".fixed-cost-block");	// 固定費のみ削除対象
	if (block) {
		block.remove();
	}

	// 再インデックス振り直し （ラベルと name属性）
	const wrappers = document.querySelectorAll(".fixed-cost-block");
	wrappers.forEach((wrapper, i) => {
		const label = wrapper.querySelector("label");
		const input = wrapper.querySelector("input[type='number']");
		const btn = wrapper.querySelector("button.delete-btn");
		
		const id = `fixedCost${i}`;
		
		if(label) {
			label.textContent = `固定費${i + 1}：`;
			label.setAttribute("for", id);
		}
		if (input) {
			input.setAttribute("name", `fixedCosts[${i}]`);
			input.setAttribute("id", id);
		}
		if (btn) {
			btn.style.display = i === 0 ? "none" : "inline-block";
		}
	});
}

document.addEventListener("DOMContentLoaded", () => {
	// 既存の number 入力にバリデーション適用
	document.querySelectorAll('input[type="number"][inputmode="numeric"]').forEach(setupValidation);

	const form = document.querySelector("form");
	if (!form) return;
	
	form.addEventListener("submit", () => {
		
		// 空欄行の削除
		// 静的コピーをとることでループ途中のDOM変更に影響されない
		const blocks =Array.from(form.querySelectorAll(".fixed-cost-block"));
		
		// 先に固定費1をチェックして、空欄なら0をセット
		const firstInput = blocks[0]?.querySelector("input[name^='fixedCosts']");
		
		for (let i = blocks.length - 1; i >= 1; i--) {	// 固定費1は対象外
			const block = blocks[i];
			const input = block.querySelector("input[name^='fixedCosts']");
			if(!input) continue;
			const val = (input.value ?? "").toString().trim();
			if (val === "") {
				block.remove();
			}
		}
		
		// もう一度取得して、連番振り直し 
		const normalizedOnce = Array.from(form.querySelectorAll(".fixed-cost-block"));
		normalizedOnce.forEach((block, idx) => {
			const label = block.querySelector("label");
			const input = block.querySelector("input[name^='fixedCosts']");
			const btn = block.querySelector("button.delete-btn");
			
			const id = `fixedCost${idx}`;
			
			if (label) {
				label.textContent = `固定費${idx + 1}：`;
				label.setAttribute("for",id);
			}
			if (input) {
				input.setAttribute("name", `fixedCosts[${idx}]`);
				input.setAttribute("id", id);
			}
			
			// 先頭の行は削除ボタン非表示、それ以外は表示
			if(btn) {
				btn.style.display = idx === 0 ? "none" : "inline-block";
			}
		});
		
		let preservedEmptyFirst = false;	// 先頭行の空欄を「0埋め対象から除外」するフラグ
		let normalized = Array.from(form.querySelectorAll(".fixed-cost-block"));
		if (normalized.length === 0) {
			addCost();
			const first = form.querySelector(".fixed-cost-block input[name^='fixedCosts']");
			if (first) {
				first.value = "0";	// 記載がなかった場合における計算後の固定費1の初期値
				preservedEmptyFirst = true;	// 空欄を維持するため後段の0埋めから除外
			}
			normalized = Array.from(form.querySelectorAll(".fixed-cost-block"));
		}
		
		// number入力の "空文字" を0扱い
		normalized.forEach((block, idx) => {
			const input = block.querySelector("input[name^='fixedCosts']");
			if (!input) return;
			if(preservedEmptyFirst && idx === 0) return; // 空欄を保持
			if (input.value.trim() === "") input.value = "0";
		});
		
		// 収入(income) が空なら 0 扱い
		const incomeInput = form.querySelector('input[name="income"], input[name="form.income"], input[id$="income"]');
		if (incomeInput && incomeInput.value.trim() === "") {
			incomeInput.value = "0";
		}
	});
});