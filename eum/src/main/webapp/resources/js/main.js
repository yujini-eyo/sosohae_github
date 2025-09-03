(() => {
  'use strict';

  const ready = (fn) => {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  };

  ready(() => {
    document.addEventListener('click', (e) => {
      const btn = e.target.closest('[data-href]');
      if (!btn) return;
      const href = btn.getAttribute('data-href');
      if (href) window.location.href = href;
    });

    // 엔터키로도 작동 (button 외 요소 지원)
    document.addEventListener('keydown', (e) => {
      if (e.key !== 'Enter') return;
      const el = document.activeElement;
      if (!el) return;
      const href = el.getAttribute && el.getAttribute('data-href');
      if (href) {
        e.preventDefault();
        window.location.href = href;
      }
    });
  });
})();
