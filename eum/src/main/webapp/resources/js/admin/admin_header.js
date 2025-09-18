/* admin-header.js — 사이드바 토글 + 접속시간 표기 + 고정헤더 보정 */
(function(){
  'use strict';

  var $ = function(s,r){ return (r||document).querySelector(s); };

  // 본문 여백 클래스(고정 헤더 높이만큼)
  function ensureBodyPadding(){
    if (!document.body.classList.contains('admin-has-fixed-header')) {
      document.body.classList.add('admin-has-fixed-header');
    }
  }

  // 사이드바 토글 (페이지 레이아웃이 이 클래스를 보고 좌측 사이드바를 접었다 폈다 하도록)
  function initSidebarToggle(){
    var btn = $('#adminSidebarToggle');
    if (!btn) return;

    // 이전 상태 복원
    try{
      var saved = localStorage.getItem('adminCollapsed') === '1';
      document.documentElement.classList.toggle('admin-collapsed', !!saved);
      btn.setAttribute('aria-pressed', saved ? 'true' : 'false');
    }catch(e){}

    btn.addEventListener('click', function(){
      var now = !document.documentElement.classList.contains('admin-collapsed');
      document.documentElement.classList.toggle('admin-collapsed', now);
      btn.setAttribute('aria-pressed', now ? 'true' : 'false');
      try{ localStorage.setItem('adminCollapsed', now ? '1' : '0'); }catch(e){}
      // 레이아웃이 필요하면 이 이벤트 리슨
      document.dispatchEvent(new CustomEvent('admin:toggle', { detail: { collapsed: now }}));
    });
  }

  // 접속(로그인) 시각 표기
  function initLoginTime(){
    var meta = $('#adminMeta'), clock = $('#adminClock');
    if (!meta || !clock) return;
    var ms = Number(meta.dataset.loginAt || 0);
    if (!ms) { clock.textContent = '접속 시간 정보 없음'; return; }

    function fmt(d){
      var yy=d.getFullYear(),
          mm=String(d.getMonth()+1).padStart(2,'0'),
          dd=String(d.getDate()).padStart(2,'0'),
          hh=String(d.getHours()).padStart(2,'0'),
          mi=String(d.getMinutes()).padStart(2,'0'),
          ss=String(d.getSeconds()).padStart(2,'0');
      return yy+'-'+mm+'-'+dd+' '+hh+':'+mi+':'+ss;
    }

    var loginDate = new Date(ms);
    clock.textContent = '접속 ' + fmt(loginDate);
    clock.title = '세션 생성/로그인 시각';
  }

  function init(){
    ensureBodyPadding();
    initSidebarToggle();
    initLoginTime();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
