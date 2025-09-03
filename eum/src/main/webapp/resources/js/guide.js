/* guide.js — TOC 유무 안전 + 헤더오프셋 + 스크롤스파이 보강 */
(() => {
  'use strict';

  const onReady = (fn) =>
    (document.readyState !== 'loading')
      ? fn()
      : document.addEventListener('DOMContentLoaded', fn);

  const $  = (s, r=document) => r.querySelector(s);
  const $$ = (s, r=document) => Array.from(r.querySelectorAll(s));

  const prefersReduce = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  // 고정 헤더 높이 계산( CSS 변수 + DOM )
  function headerHeight() {
    const cssVar = parseInt(getComputedStyle(document.documentElement).getPropertyValue('--header-h')) || 0;
    const h = $('.site-header');
    const domH = h ? Math.ceil(h.getBoundingClientRect().height) : 0;
    return Math.max(cssVar, domH, 72);
  }

  // 헤더 오프셋 반영 스크롤
  function scrollToEl(el, updateHash = false) {
    if (!el) return;
    const y = el.getBoundingClientRect().top + window.pageYOffset - (headerHeight() + 12);
    window.scrollTo({ top: y, behavior: prefersReduce ? 'auto' : 'smooth' });
    if (updateHash && history.replaceState) {
      history.replaceState(null, '', '#' + el.id);
    }
  }
  function scrollToHash(hash) {
    const id = (hash || '').replace(/^#/, '');
    if (!id) return;
    const el = document.getElementById(id);
    if (el) scrollToEl(el, false);
  }

  onReady(() => {
    const root = $('.page-guide') || document;
    const toc  = root.querySelector('.toc'); // 없어도 OK
    const sections = $$('.page-guide section[id]');

    // 내부 앵커 부드러운 스크롤 (히어로/본문 어디든)
    document.addEventListener('click', (e) => {
      const a = e.target.closest('a[href^="#"]');
      if (!a) return;

      // 새 탭/수정키/중클릭은 기본 동작 유지
      if (e.defaultPrevented || e.button !== 0 || a.target === '_blank' ||
          e.metaKey || e.ctrlKey || e.shiftKey || e.altKey) return;

      const id = a.getAttribute('href').slice(1);
      if (!id) return;
      const el = document.getElementById(id);
      if (!el) return;

      e.preventDefault();
      scrollToEl(el, true);
    });

    // data-href 버튼 네비게이션
    document.addEventListener('click', (e) => {
      const t = e.target.closest('[data-href]');
      if (!t) return;

      // 새 탭/수정키/중클릭은 기본 동작 유지
      if (e.defaultPrevented || e.button !== 0 ||
          e.metaKey || e.ctrlKey || e.shiftKey || e.altKey) return;

      const href = t.getAttribute('data-href');
      if (href) window.location.href = href;
    });

    // 스크롤스파이 (TOC 있을 때만)
    let io;
    function rebuildObserver() {
      if (!toc) return;
      if (io) io.disconnect();

      const linkById = new Map();
      $$('.toc a[href^="#"]', toc).forEach(a => {
        const id = a.getAttribute('href').slice(1);
        if (id) linkById.set(id, a);
      });

      io = new IntersectionObserver((entries) => {
        // 화면 상단에 더 가까운 섹션을 우선 선택
        let best = null;
        for (const ent of entries) {
          if (!ent.isIntersecting) continue;
          if (!best || ent.boundingClientRect.top < best.boundingClientRect.top) best = ent;
        }
        if (!best) return;

        const id = best.target.id;
        if (!id) return;

        $$('.toc a.is-active', toc).forEach(a => {
          a.classList.remove('is-active'); a.removeAttribute('aria-current');
        });
        const link = linkById.get(id);
        if (link) { link.classList.add('is-active'); link.setAttribute('aria-current', 'true'); }
      }, {
        root: null,
        rootMargin: `-${headerHeight() + 20}px 0px -60% 0px`,
        threshold: [0, 0.25, 0.5, 1],
      });

      sections.forEach(sec => io.observe(sec));
    }
    if (toc) rebuildObserver();

    // 초기 해시 보정 (새로고침/직접 링크 시 헤더에 가려짐 방지)
    if (location.hash) setTimeout(() => scrollToHash(location.hash), 0);

    // 리사이즈/폰트 로딩 시 헤더 높이 변동 → 스크롤스파이 재설정
    let rAF = 0;
    const onResize = () => {
      if (rAF) cancelAnimationFrame(rAF);
      rAF = requestAnimationFrame(() => { if (toc) rebuildObserver(); });
    };
    window.addEventListener('resize', onResize);
    if (document.fonts && document.fonts.ready) {
      document.fonts.ready.then(onResize).catch(() => {});
    }
  });
})();
