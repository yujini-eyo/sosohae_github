/* about.js — 부드러운 앵커 스크롤 + 푸터 가림 방지 + data-href 네비게이션 */
(() => {
  'use strict';

  const ready = (fn) =>
    (document.readyState !== 'loading') ? fn() : document.addEventListener('DOMContentLoaded', fn);

  const $ = (s, r=document) => r.querySelector(s);

  // 헤더 높이 계산
  function headerHeight() {
    const cssVar = parseInt(getComputedStyle(document.documentElement).getPropertyValue('--header-h')) || 0;
    const header = $('.site-header');
    const domH = header ? Math.ceil(header.getBoundingClientRect().height) : 0;
    return Math.max(cssVar, domH, 72);
  }

  // 푸터/모바일 탭바 높이 → 하단 패딩 보정(가림 방지)
  function footerHeight() {
    const f = $('.site-footer');
    const fb = f ? Math.ceil(f.getBoundingClientRect().height) : 0;
    const tab = $('.mobile-tabbar');
    const tb = (tab && getComputedStyle(tab).display !== 'none') ? Math.ceil(tab.getBoundingClientRect().height) : 0;
    return Math.max(fb, tb, 0);
  }
  function applyBottomPadding(root) {
    if (!root) return;
    const pad = footerHeight();
    root.style.paddingBottom = pad ? (pad + 16) + 'px' : '';
  }

  // 앵커 스크롤(헤더 오프셋 반영)
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
    const root = $('.page-about');
    if (!root) return;

    // data-href 버튼 네비게이션
    root.addEventListener('click', (e) => {
      const t = e.target.closest('[data-href]');
      if (!t) return;
      const href = t.getAttribute('data-href');
      if (href) location.href = href;
    });

    // 본문 내 해시 링크도 부드럽게
    document.addEventListener('click', (e) => {
      const a = e.target.closest('a[href^="#"]');
      if (!a) return;
      e.preventDefault();
      smoothScrollToHash(a.getAttribute('href'));
    });

    // 초기 해시 보정(헤더에 가리지 않게)
    if (location.hash) setTimeout(() => smoothScrollToHash(location.hash), 0);

    // 푸터 가림 방지
    const onResize = () => applyBottomPadding(root);
    onResize();
    window.addEventListener('resize', onResize);
    if (document.fonts && document.fonts.ready) {
      document.fonts.ready.then(onResize).catch(() => {});
    }
  });
})();
