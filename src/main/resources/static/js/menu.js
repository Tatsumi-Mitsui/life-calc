(() => {
    const drawer = document.getElementById("drawer");
    const toggle = document.getElementById("menu-toggle");
    const closeBtn = document.getElementById('menu-close');

    if (!drawer || !toggle) return;

    const openDrawer = () => {
        drawer.classList.add('open');
        toggle?.setAttribute('aria-expanded', 'true');
        drawer.setAttribute('aria-hidden', 'false');
    };
    
    const closeDrawer = () => {
        drawer.classList.remove('open');
        toggle?.setAttribute('aria-expanded', 'false');
        drawer.setAttribute('aria-hidden', 'true');
    }
    
    // トグル
    toggle.addEventListener("click", () => {
        drawer.classList.contains("open") ? closeDrawer() : openDrawer();
    });
    
    // ×ボタンで閉じる
    closeBtn?.addEventListener('click', closeDrawer);
    
    // Escキーで閉じる
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape" && drawer.classList.contains("open")) closeDrawer();
    });
    
    // オーバーレイ（背景）クリックで閉じる
    drawer.addEventListener("click", (e) => {
        if (e.target === drawer) closeDrawer();
    });
})();