/* notice.js — 서버 렌더링 + 점진적 향상(옵션 API) */
(() => {
  'use strict';

  const onReady = (fn) =>
    (document.readyState !== 'loading') ? fn() : document.addEventListener('DOMContentLoaded', fn);

  const $  = (s, r=document) => r.querySelector(s);
  const $$ = (s, r=document) => Array.from(r.querySelectorAll(s));

  function ctx() {
    // header.script.js가 window.EUM_CTX를 세팅했다면 활용
    if (window.EUM_CTX) return window.EUM_CTX;
    // fallback: <link .../resources/> 경로에서 추정
    const any = $('link[href*="/resources/"]');
    if (!any) return '';
    try {
      const u = new URL(any.href, location.href);
      const i = u.pathname.indexOf('/resources/');
      return i >= 0 ? u.pathname.slice(0, i) : '';
    } catch(e){ return ''; }
  }

  // 검색 초기화
  function bindToolbar() {
    const form  = $('#noticeSearchForm');
    const q     = $('#q');
    const sort  = $('#sort');
    const reset = $('#resetBtn');

    if (reset) {
      reset.addEventListener('click', () => {
        if (q) q.value = '';
        if (sort) sort.value = 'recent';
        if (form) form.submit();
      });
    }

    // 엔터 제출 시 기본 동작(서버 검색)
    if (form) {
      form.addEventListener('submit', () => {
        // 추가 파라미터 붙이고 싶으면 여기서 조정
      });
    }
  }

  // (옵션) JSON API로 목록 불러오기 — 서버가 준비되면 활성화
  // 기대 응답 예시: { list:[{id,title,createDate,views,isPinned}], page:1, totalPages:3 }
  async function fetchNotices({page=1, q='', sort='recent'}={}) {
    const base = ctx() + '/notice/list.json';
    const url  = new URL(base, location.origin);
    if (q)    url.searchParams.set('q', q);
    if (sort) url.searchParams.set('sort', sort);
    if (page) url.searchParams.set('page', page);
    const res = await fetch(url.toString(), { headers:{'Accept':'application/json'} });
    if (!res.ok) throw new Error('Failed to load notices');
    return res.json();
  }

  function renderRows(list) {
    const tbody = $('#tbody-notice');
    if (!tbody) return;
    if (!list || list.length === 0) {
      tbody.innerHTML = `<tr class="empty"><td colspan="4">등록된 공지사항이 없습니다.</td></tr>`;
      return;
    }
    tbody.innerHTML = list.map((n, idx) => {
      const id    = n.noticeNo ?? n.id ?? idx+1;
      const title = (n.title ?? '').toString();
      const date  = (n.createDate ?? n.writeDate ?? '').toString().slice(0, 10).replaceAll('-', '.');
      const views = n.viewCnt ?? n.views ?? 0;
      const pinned= n.isPinned ? `<span class="badge">고정</span>` : '';
      return `
        <tr>
          <td class="td-no">${id}</td>
          <td class="td-title"><a href="${ctx()}/notice/view.do?id=${encodeURIComponent(id)}">${escapeHtml(title)}</a> ${pinned}</td>
          <td class="td-date">${date}</td>
          <td class="td-views">${views}</td>
        </tr>`;
    }).join('');
  }

  function renderPagination(page, totalPages, q, sort) {
    const nav = $('#pagination-notice');
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

    // SPA 느낌으로 전환하고 싶으면 기본동작 막고 fetch로 갱신
    nav.addEventListener('click', async (e) => {
      const a = e.target.closest('a.page[data-page]');
      if (!a) return;
      // 기본 네비게이션 사용하려면 return; (주석 해제)
      e.preventDefault();
      const next = parseInt(a.dataset.page, 10) || 1;
      try {
        const data = await fetchNotices({ page: next, q, sort });
        renderRows(data.list || []);
        renderPagination(data.page || next, data.totalPages || 1, q, sort);
        window.history.replaceState(null, '', `?page=${next}&q=${encodeURIComponent(q||'')}&sort=${encodeURIComponent(sort||'recent')}`);
      } catch (err) {
        // 실패 시 링크로 이동
        location.href = a.getAttribute('href');
      }
    }, { once:true });
  }

  function escapeHtml(s){
    return String(s).replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
  }

  onReady(async () => {
    const root = $('.page-notice');
    if (!root) return;

    bindToolbar();

    // 점진적 향상: 서버가 list.json API를 제공한다면 아래를 유지
    // 제공하지 않는다면 이 블록은 에러 없이 빠르게 빠져나옵니다.
    const params = new URLSearchParams(location.search);
    const page = parseInt(params.get('page') || '1', 10);
    const q    = params.get('q') || '';
    const sort = params.get('sort') || 'recent';

    try {
      const data = await fetchNotices({ page, q, sort });
      if (data && Array.isArray(data.list)) {
        renderRows(data.list);
        renderPagination(data.page || page, data.totalPages || 1, q, sort);
      }
    } catch (e) {
      // API가 없거나 실패하면 서버 렌더링 그대로 사용
    }
  });
})();
