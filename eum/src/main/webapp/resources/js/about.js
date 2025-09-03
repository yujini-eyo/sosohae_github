(() => {
  'use strict';

  const ready = (fn) =>
    (document.readyState !== 'loading')
      ? fn()
      : document.addEventListener('DOMContentLoaded', fn);

  // 고정 헤더 높이 계산 (CSS 변수와 실제 DOM 모두 고려)
  function headerHeight() {
    const cssVar = parseInt(
      getComputedStyle(document.documentElement).getPropertyValue('--header-h')
    ) || 0;
    const header = document.querySelector('.site-header');
    const domH = header ? Math.ceil(header.getBoundingClientRect().height) : 0;
    return Math.max(cssVar, domH, 72);
  }

  // 헤더 오프셋을 고려한 부드러운 스크롤
  function smoothScrollToEl(el) {
    if (!el) return;
    const y = el.getBoundingClientRect().top + window.pageYOffset - (headerHeight() + 12);
    window.scrollTo({ top: y, behavior: 'smooth' });
  }
  function smoothScrollToHash(hash) {
    const id = (hash || '').replace(/^#/, '');
    if (!id) return;
    const target = document.getElementById(id);
    if (target) smoothScrollToEl(target);
  }

  ready(() => {
    const root = document.querySelector('.page-about');
    if (!root) return;

    // 1) data-href 버튼 내비게이션
    root.addEventListener('click', (e) => {
      const btn = e.target.closest('[data-href]');
      if (!btn) return;
      const href = btn.getAttribute('data-href');
      if (href) window.location.href = href;
    });

    // 2) TOC(있을 때만) — 클릭 시 헤더 오프셋 반영 스크롤
    const toc = root.querySelector('.toc');
    if (toc) {
      toc.addEventListener('click', (e) => {
        const a = e.target.closest('a[href^="#"]');
        if (!a) return;
        const hash = a.getAttribute('href');
        const target = root.querySelector(hash);
        if (!target) return;
        e.preventDefault();
        smoothScrollToEl(target);
        // URL 해시만 갱신 (페이지 재점프 방지)
        if (history.replaceState) history.replaceState(null, '', hash);
      });
    }

    // 3) 스크롤스파이 (TOC 있을 때만 활성화)
    let io;
    function setActiveById(id) {
      if (!toc) return;
      const links = toc.querySelectorAll('a[href^="#"]');
      links.forEach(a => {
        const on = a.getAttribute('href') === ('#' + id);
        a.classList.toggle('is-active', on);
        if (on) a.setAttribute('aria-current', 'true');
        else a.removeAttribute('aria-current');
      });
    }
    function buildObserver() {
      if (!toc) return;
      if (io) io.disconnect();

      const sections = Array.from(root.querySelectorAll('section[id]'));
      if (sections.length === 0) return;

      io = new IntersectionObserver((entries) => {
        // 화면 상단에 더 가까운 섹션을 우선 선택
        const candidates = entries.filter(e => e.isIntersecting);
        if (candidates.length === 0) return;
        candidates.sort((a, b) => a.boundingClientRect.top - b.boundingClientRect.top);
        const best = candidates[0];
        const id = best.target.getAttribute('id');
        if (id) setActiveById(id);
      }, {
        root: null,
        rootMargin: `-${headerHeight() + 16}px 0px -60% 0px`,
        threshold: [0, 0.1, 0.25, 0.5, 1]
      });

      sections.forEach(sec => io.observe(sec));
    }
    if (toc) buildObserver();

    // 4) 초기 해시 보정 (새로고침/직접 링크 시 헤더에 가려지는 문제 방지)
    if (location.hash) {
      // 레이아웃 계산 후 적용
      setTimeout(() => smoothScrollToHash(location.hash), 0);
    }

    // 5) 리사이즈/폰트 로딩으로 헤더 높이 변동 시 관찰자 재설정
    let rafId = 0;
    function onResize() {
      if (rafId) cancelAnimationFrame(rafId);
      rafId = requestAnimationFrame(() => {
        if (toc) buildObserver(); // rootMargin 재계산
      });
    }
    window.addEventListener('resize', onResize);
    if (document.fonts && document.fonts.ready) {
      document.fonts.ready.then(onResize).catch(() => {});
    }
  });
})();
