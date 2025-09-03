/* guide.js — TOC 유무와 무관하게 안전하게 작동 */
(() => {
  'use strict';

  const onReady = (fn) =>
    (document.readyState !== 'loading') ? fn() : document.addEventListener('DOMContentLoaded', fn);

  const $  = (s, r=document) => r.querySelector(s);
  const $$ = (s, r=document) => Array.from(r.querySelectorAll(s));

  // 고정 헤더 높이 계산 (CSS 변수와 실제 DOM 둘 다 고려)
  function headerHeight() {
    const cssVar = parseInt(getComputedStyle(document.documentElement).getPropertyValue('--header-h')) || 0;
    const h = $('.site-header');
    const domH = h ? Math.ceil(h.getBoundingClientRect().height) : 0;
    return Math.max(cssVar, domH, 72);
  }

  // 앵커 스크롤 (헤더 오프셋 반영)
  function smoothScrollToId(id) {
    const el = document.getElementById(id);
    if (!el) return;
    const y = el.getBoundingClientRect().top + window.pageYOffset - (headerHeight() + 12);
    window.scrollTo({ top: y, behavior: 'smooth' });
  }

  onReady(() => {
    const root = $('.page-guide') || document;
    const toc  = $('.page-guide .toc'); // 없어도 OK
    const sections = $$('.page-guide section[id]');

    // 1) (선택) TOC 클릭 처리 — TOC가 있을 때만
    if (toc) {
      toc.addEventListener('click', (e) => {
        const a = e.target.closest('a[href^="#"]');
        if (!a) return;
        e.preventDefault();
        const id = a.getAttribute('href').slice(1);
        if (id) smoothScrollToId(id);
      });
    }

    // 2) 본문 내 앵커도 부드러운 스크롤 (히어로/본문 어디든)
    document.addEventListener('click', (e) => {
      const a = e.target.closest('a[href^="#"]');
      if (!a) return;
      const href = a.getAttribute('href');
      // 해시만 있는 내부 링크만 처리
      if (href && href.startsWith('#')) {
        e.preventDefault();
        const id = href.slice(1);
        if (id) smoothScrollToId(id);
      }
    });

    // 3) 스크롤스파이 — TOC가 있을 때만 활성화
    if (toc) {
      const linkById = new Map();
      $$('.toc a[href^="#"]', toc).forEach(a => {
        const id = a.getAttribute('href').slice(1);
        if (id) linkById.set(id, a);
      });

      const io = new IntersectionObserver((entries) => {
        let best = null;
        for (const ent of entries) {
          if (!ent.isIntersecting) continue;
          // 화면 상단에서 가까운 섹션 우선
          if (!best || ent.boundingClientRect.top < best.boundingClientRect.top) best = ent;
        }
        if (best) {
          const id = best.target.getAttribute('id');
          if (!id) return;
          // 활성 링크 토글
          $$('.toc a.is-active', toc).forEach(a => a.classList.remove('is-active'));
          const link = linkById.get(id);
          if (link) link.classList.add('is-active');
        }
      }, {
        rootMargin: `-${headerHeight() + 20}px 0px -60% 0px`,
        threshold: [0, 0.2, 0.5, 1]
      });

      sections.forEach(sec => io.observe(sec));
    }

    // 4) data-href 네비게이션 (헤더 스크립트가 이미 처리해도 중복 영향 없음)
    document.addEventListener('click', (e) => {
      const t = e.target.closest('[data-href]');
      if (!t) return;
      const href = t.getAttribute('data-href');
      if (href) window.location.href = href;
    });

    // 5) 리사이즈 시 헤더 높이 변동 대응(스크롤스파이 rootMargin 갱신 필요 시 재계산)
    let rAF = 0;
    window.addEventListener('resize', () => {
      if (rAF) cancelAnimationFrame(rAF);
      rAF = requestAnimationFrame(() => {
        // 스크롤스파이를 쓰고 있으면 rootMargin만 재적용하기보단 간단히 재초기화가 안전
        // (여기서는 간단히 앵커 오프셋만 최신값을 쓰도록 두고 별도 작업은 생략)
      });
    });
  });
})();
