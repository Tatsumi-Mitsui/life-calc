function addCost() {
	const form = document.querySelector("form");
	
	// 現在の固定費欄の数をカウント
	const fixedCosts = form.querySelectorAll("input[name^='fixedCosts']");
	const index = fixedCosts.length;

	// ラッパー div を作成
	const wrapper = document.createElement("div");
	wrapper.classList.add("input-block", "fixed-cost-block");	// 追加欄でも中央寄せ防止

	// 固定費ラベルと入力欄＋削除ボタン（1個目は削除ボタンなし）
	wrapper.innerHTML = `
		<label>固定費${index + 1}： </label>
		<div class="input-row">
			<span>￥</span>
			<input type="text" name="fixedCosts[${index}]" value="" placeholder="例：10000" />
			${index > 0 ? `
			<button type="button" onclick="removeCost(this)" class="delete-btn">
				<i class="fa-solid fa-circle-minus icon-minus"></i>
			</button>` : ''}
		</div>
	`;

	// 追加ボタンの前に挿入
	const addBtnWrapper = form.querySelector(".add-button-wrapper");
	form.insertBefore(wrapper, addBtnWrapper);
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
		label.textContent = `固定費${i + 1}：`;

		const input = wrapper.querySelector("input");
		input.setAttribute("name", `fixedCosts[${i}]`);
		input.setAttribute("id", `fixedCost${i}`);

		const btn = wrapper.querySelector("button");
		if (btn) {
			btn.style.display = i === 0 ? "none" : "inline-block";
		}
	});
}

document.addEventListener("DOMContentLoaded", () => {
	const form = document.querySelector("form");
	
	form.addEventListener("submit", () => {
		// 静的コピーをとることでループ途中のDOM変更に影響されない
		const inputs = Array.from(form.querySelectorAll("input[name^='fixedCosts']"));
		
		// 逆順ループ
		for (let i = inputs.length - 1; i >= 0; i--) {
			const input = inputs[i];
			const cleanedValue = input.value?.replace(/[\s\u3000]/g, '');
			if (!cleanedValue) {
				const block = input.closest(".fixed-cost-block");
				if (block) block.remove();
			}	
		}
	});
});