/* header.patch.js
 * 원본 header.js 를 수정하지 않고 보강하는 패치
 * - 헤더 높이만큼 body 상단 여백 자동 적용 (겹침 방지)
 * - 드로어 포커스 트랩 / 접근성 보강
 * - 모바일 탭바 활성 상태 동기화
 * - 잘못된 링크(wirte.html) 우회
 */

(() => {
  'use strict';

  /** 유틸: 디바운스 */
  const debounce = (fn, d=120) => {
    let t; return (...args) => { clearTimeout(t); t = setTimeout(()=>fn(...args), d); };
  };

  /** 1) 헤더 겹침 방지: 실제 헤더 높이만큼 body padding-top 적용 */
  function applyHeaderSafeTop() {
    const header = document.querySelector('body > header, header.site-header, .site-header, .header');
    if (!header) return;
    const h = header.getBoundingClientRect().height || 60;
    // CSS 변수도 함께 갱신 (기존 CSS가 var(--header-h)를 쓰는 경우 동작)
    document.documentElement.style.setProperty('--header-h', `${h}px`);
    // 인라인 패딩으로 즉시 반영
    if (parseFloat(getComputedStyle(document.body).paddingTop) !== h) {
      document.body.style.paddingTop = `${h}px`;
    }
  }
  window.addEventListener('load', applyHeaderSafeTop);
  window.addEventListener('resize', debounce(applyHeaderSafeTop, 120));

  /** 2) 드로어 포커스 트랩 & 접근성 */
  function initDrawerA11y() {
    const drawer   = document.getElementById('drawer');
    const backdrop = document.getElementById('drawerBackdrop');
    const closeBtn = document.getElementById('drawerClose');
    const hamburger= document.getElementById('hamburger');
    if (!drawer || !backdrop || !hamburger || !closeBtn) return;

    const FOCUSABLE = [
      'a[href]', 'button:not([disabled])', 'input:not([disabled])',
      'select:not([disabled])', 'textarea:not([disabled])', '[tabindex]:not([tabindex="-1"])'
    ].join(',');

    let lastActive = null;

    const trapKey = (e) => {
      if (e.key !== 'Tab') return;
      const items = drawer.querySelectorAll(FOCUSABLE);
      if (!items.length) return;
      const first = items[0], last = items[items.length - 1];
      if (e.shiftKey && document.activeElement === first) { last.focus(); e.preventDefault(); }
      else if (!e.shiftKey && document.activeElement === last) { first.focus(); e.preventDefault(); }
    };

    const openObserver = new MutationObserver(() => {
      const isOpen = drawer.classList.contains('open');
      if (isOpen) {
        lastActive = document.activeElement;
        drawer.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
        hamburger.setAttribute('aria-expanded', 'true');
        // 첫 포커스 이동
        const first = drawer.querySelector(FOCUSABLE);
        (first || closeBtn).focus();
        drawer.addEventListener('keydown', trapKey);
      } else {
        drawer.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';
        hamburger.setAttribute('aria-expanded', 'false');
        drawer.removeEventListener('keydown', trapKey);
        if (lastActive && typeof lastActive.focus === 'function') lastActive.focus();
      }
    });
    openObserver.observe(drawer, { attributes: true, attributeFilter: ['class'] });

    // ESC / 백드롭 안전
    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && drawer.classList.contains('open')) {
        closeBtn.click();
      }
    });
    backdrop.addEventListener('click', () => { if (drawer.classList.contains('open')) closeBtn.click(); });
  }
  window.addEventListener('DOMContentLoaded', initDrawerA11y);

  /** 3) 모바일 탭바와 현재 페이지 활성 동기화 */
  function syncTabbarActive() {
    const bar = document.querySelector('.mobile-tabbar');
    if (!bar) return;
    const path = location.pathname.split('/').pop() || 'index.html';
    bar.querySelectorAll('a.tabbar-item').forEach(a => {
      const href = a.getAttribute('href') || '';
      const isHash = href.startsWith('#') && (location.hash === href);
      const isPath = !href.startsWith('#') && (path === href || location.pathname.startsWith(href));
      const active = isHash || isPath;
      a.classList.toggle('is-active', active);
      if (active) a.setAttribute('aria-current', 'page'); else a.removeAttribute('aria-current');
    });
  }
  window.addEventListener('DOMContentLoaded', syncTabbarActive);
  window.addEventListener('popstate', syncTabbarActive);
  window.addEventListener('hashchange', syncTabbarActive);
  document.addEventListener('click', (e) => {
    const a = e.target.closest('a.tabbar-item');
    if (!a) return;
    // 네비게이션 전 즉시 활성화 표시
    const bar = a.closest('.mobile-tabbar');
    if (!bar) return;
    bar.querySelectorAll('.tabbar-item').forEach(el => el.classList.remove('is-active'));
    a.classList.add('is-active');
  });

  /** 4) 잘못된 링크(wirte.html) 우회 */
  document.addEventListener('click', (e) => {
    const a = e.target.closest('a[href$="wirte.html"]');
    if (!a) return;
    e.preventDefault();
    const fixed = a.href.replace(/wirte\.html(\b|$)/, 'write.html');
    location.href = fixed;
  });

  /** 5) (선택) iOS 탭바 블러 비활성화 토글
   *   - 헤더 깜빡임이 여전하면 아래 플래그를 true 로 바꾸세요.
   *     window.__DISABLE_TABBAR_BLUR__ = true;
   */
  function maybeDisableTabbarBlur() {
    if (!window.__DISABLE_TABBAR_BLUR__) return;
    const style = document.createElement('style');
    style.textContent = `
      .mobile-tabbar{ -webkit-backdrop-filter: none !important; backdrop-filter: none !important; }
    `;
    document.head.appendChild(style);
  }
  maybeDisableTabbarBlur();

  /** 6) 첫 페인트 직후 한 번 더 보정 (폰트 로딩으로 헤더 높이 변동 대비) */
  window.addEventListener('load', () => setTimeout(applyHeaderSafeTop, 50));
})();
