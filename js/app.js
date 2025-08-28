(() => {
  // 역할별 목적지 매핑
  const ROUTE = {
    ask: 'write.html', // 도와주세요 → 글쓰기
    give: 'board.html' // 도와줄게요 → 게시판
  };

  const el = {
    year: document.getElementById('year'),
    actions: document.querySelector('.actions'),
    goMain: document.getElementById('goMain'),
  };

  // 연도 자동 표기
  if (el.year) el.year.textContent = new Date().getFullYear();

  // 역할 선택 시 해당 페이지로 이동
  function navigate(role){
    const target = ROUTE[role];
    if (!target) return;       // 안전장치: 정의되지 않은 role 무시
    location.href = target;    // 쿼리 대신 바로 해당 파일로 이동
  }

  // 버튼 클릭(이벤트 위임)
  el.actions?.addEventListener('click', (ev) => {
    const btn = ev.target.closest('button[data-role]');
    if (!btn) return;
    navigate(btn.dataset.role);
  });

  // 초기 포커스
  document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('button[data-role="ask"]')?.focus();
  });
})();
