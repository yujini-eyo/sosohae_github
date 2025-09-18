/* notice.js — 서버 렌더링 우선, API 있으면만 조용히 향상 */
(() => {
  'use strict';

  // ---------- tiny utils ----------
  const onReady = (fn) =>
    (document.readyState !== 'loading') ? fn() : document.addEventListener('DOMContentLoaded', fn);
  const $  = (s, r=document) => r.querySelector(s);
  const escapeHtml = (s) =>
    String(s).replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));

  function ctx() {
    if (window.EUM_CTX) return window.EUM_CTX;
    const meta = document.querySelector('meta[name="app:ctx"]');
    if (meta?.content) return meta.content;
    const any = document.querySelector('link[href*="/resources/"]');
    if (!any) return '';
    try {
      const u = new URL(any.href, location.href);
      const i = u.pathname.indexOf('/resources/');
      return i >= 0 ? u.pathname.slice(0, i) : '';
    } catch { return ''; }
  }

  // ---------- toolbar (서버 submit 유지) ----------
  function bindToolbar() {
    const form  = document.querySelector('#noticeSearchForm');
    const q     = document.querySelector('#q');
    const sort  = document.querySelector('#sort');
    const reset = document.querySelector('#resetBtn');

    reset?.addEventListener('click', () => {
      if (q) q.value = '';
      if (sort) sort.value = 'recent';
      form?.submit();
    });
    form?.addEventListener('submit', () => {});
  }

  // ---------- API 존재 감지(있을 때만 사용) ----------
  // - sessionStorage에 캐시하여 새로고침마다 재요청 안 함
  // - JSON 아니면 사용 안 함(406/뷰리졸버 HTML 등 모두 회피)
  const API_CACHE_KEY = 'noticeApiBase:v1';
  async function detectApiBase() {
    const cached = sessionStorage.getItem(API_CACHE_KEY);
    if (cached === 'none') return null;
    if (cached) return cached;

    const bases = [
      `${ctx()}/notice/list.json`,
      // 백엔드에 따라 이런 형태일 수도 있어 대비용으로 한 개 더(있어도 안 쓰면 무시)
      `${ctx()}/notice/list.do?fmt=json`,
    ];

    for (const base of bases) {
      try {
        const url = new URL(base, location.origin);
        // Accept를 넓게(*/*) 줘서 406 유발 안 하되, 실제로 JSON인지는 우리가 검사
        const res = await fetch(url.toString(), {
          method: 'GET',
          headers: { 'Accept': '*/*', 'X-Requested-With': 'XMLHttpRequest' },
          credentials: 'same-origin',
          redirect: 'manual',
        });
        if (!res.ok) continue;
        const ct = res.headers.get('content-type') || '';
        if (ct.includes('application/json')) {
          sessionStorage.setItem(API_CACHE_KEY, base);
          return base;
        }
      } catch { /* 조용히 무시 */ }
    }
    sessionStorage.setItem(API_CACHE_KEY, 'none');
    return null;
  }

  // ---------- fetch & render ----------
  let lastAbort = null;
  async function fetchNotices(apiBase, {page=1, q='', sort='recent'}={}) {
    const url  = new URL(apiBase, location.origin);
    if (q)    url.searchParams.set('q', q);
    if (sort) url.searchParams.set('sort', sort);
    url.searchParams.set('page', String(page));

    if (lastAbort) lastAbort.abort();
    lastAbort = new AbortController();

    const res = await fetch(url.toString(), {
      method: 'GET',
      headers: {
        'Accept': 'application/json, */*;q=0.1',
        'X-Requested-With': 'XMLHttpRequest'
      },
      credentials: 'same-origin',
      signal: lastAbort.signal,
    });
    if (!res.ok) throw new Error('fetch failed');
    return res.json();
  }

  function renderRows(list) {
    const tbody = document.querySelector('#tbody-notice');
    if (!tbody) return;

    if (!list || list.length === 0) {
      tbody.innerHTML = `<tr class="empty"><td colspan="4">등록된 공지사항이 없습니다.</td></tr>`;
      return;
    }

    tbody.innerHTML = list.map((n, i) => {
      const id    = n.noticeNo ?? n.id ?? n.articleNo ?? (i+1);
      const title = (n.title ?? '').toString();
      const raw   = (n.createDate ?? n.writeDate ?? n.regDate ?? '').toString();
      const date  = raw.slice(0,10).replaceAll('-', '.');
      const views = n.viewCnt ?? n.views ?? n.viewCount ?? 0;
      const pinned= (n.isPinned || n.isNotice || n.pinned) ? `<span class="badge">고정</span>` : '';
      return `
        <tr>
          <td class="td-no">${id}</td>
          <td class="td-title"><a href="${ctx()}/notice/view.do?id=${encodeURIComponent(id)}">${escapeHtml(title)}</a> ${pinned}</td>
          <td class="td-date">${date}</td>
          <td class="td-views">${views}</td>
        </tr>`;
    }).join('');
  }

  function renderPagination(page, totalPages, q, sort, apiBase) {
    const nav = document.querySelector('#pagination-notice');
    if (!nav) return;

    if (!totalPages || totalPages <= 1) {
      nav.innerHTML = '';
      return;
    }
    let html = '';
    if (page > 1) {
      html += `<a class="page" data-page="${page-1}" href="?page=${page-1}&q=${encodeURIComponent(q||'')}&sort=${encodeURIComponent(sort||'recent')}">이전</a>`;
    }
    for (let p=1; p<=totalPages; p++) {
      html += `<a class="page ${p===page?'is-active':''}" data-page="${p}" href="?page=${p}&q=${encodeURIComponent(q||'')}&sort=${encodeURIComponent(sort||'recent')}">${p}</a>`;
    }
    if (page < totalPages) {
      html += `<a class="page" data-page="${page+1}" href="?page=${page+1}&q=${encodeURIComponent(q||'')}&sort=${encodeURIComponent(sort||'recent')}">다음</a>`;
    }
    nav.innerHTML = html;

    // API 있을 때만 SPA 네비게이션—없으면 기본 링크
    if (!apiBase) return;
    nav.addEventListener('click', async (e) => {
      const a = e.target.closest('a.page[data-page]');
      if (!a) return;
      e.preventDefault();
      const next = parseInt(a.dataset.page, 10) || 1;
      try {
        const data = await fetchNotices(apiBase, { page: next, q, sort });
        if (data?.list) {
          renderRows(data.list);
          renderPagination(data.page || next, data.totalPages || 1, q, sort, apiBase);
          window.history.replaceState(null, '', `?page=${next}&q=${encodeURIComponent(q||'')}&sort=${encodeURIComponent(sort||'recent')}`);
        }
      } catch {
        // 실패 시 전통 링크로 백업
        location.href = a.getAttribute('href');
      }
    }, { once:true });
  }

  // ---------- boot ----------
  onReady(async () => {
    const root = document.querySelector('.page-notice');
    if (!root) return;

    bindToolbar();

    // 서버가 JSON 안 주면 아예 시도 안 함(406/콘솔노이즈 방지)
    const apiBase = await detectApiBase();
    if (!apiBase) return;

    const params = new URLSearchParams(location.search);
    const page = parseInt(params.get('page') || '1', 10);
    const q    = params.get('q') || '';
    const sort = params.get('sort') || 'recent';

    try {
      const data = await fetchNotices(apiBase, { page, q, sort });
      if (data?.list) {
        renderRows(data.list);
        renderPagination(data.page || page, data.totalPages || 1, q, sort, apiBase);
      }
    } catch {
      // 조용히 서버 렌더 유지
    }
  });
})();
