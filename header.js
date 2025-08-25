/* header.js
 * - 데스크탑(>900px)에서는 로그인/회원가입(또는 로그아웃) 버튼 표시
 * - 모바일(<=900px)에서는 헤더에서 로그인/로그아웃 버튼 숨김
 * - 드로어(햄버거) 토글 및 접근성 보강
 * - 현재 페이지 자동 강조
 */

(() => {
  "use strict";

  /* -------- 로그인 상태(데모 값) -------- */
  const isLoggedIn = true;
  const userName = "홍길동";
  const userPoints = 1250;

  /* -------- DOM -------- */
  const rightArea   = document.getElementById("rightArea");
  const topnav      = document.getElementById("topnav");
  const hamburger   = document.getElementById("hamburger");
  const drawer      = document.getElementById("drawer");
  const backdrop    = document.getElementById("drawerBackdrop");
  const drawerClose = document.getElementById("drawerClose");

  /* -------- 미디어쿼리(모바일 판단) -------- */
  const mq = window.matchMedia("(max-width: 900px)");

  /* -------- 공용 아이콘/템플릿 -------- */
  const bellBtnSVG = `
    <button class="icon-btn" id="notifyBtn" aria-label="알림">
      <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">
        <path d="M12 22a2 2 0 0 0 2-2H10a2 2 0 0 0 2 2Zm6-6V11a6 6 0 1 0-12 0v5l-2 2v1h16v-1l-2-2Z" fill="currentColor"/>
      </svg>
      <span class="badge" id="notifyBadge"></span>
    </button>
  `;

  /* -------- 헤더 우측 영역 렌더링 -------- */
  function renderRightArea() {
    if (!rightArea) return;
    const isMobile = mq.matches;

    if (isLoggedIn) {
      rightArea.innerHTML = `
        <span class="user-name">${userName} 님</span>
        <a href="points.html" class="user-points">(${userPoints}P)</a>
        ${bellBtnSVG}
        <button class="info-btn" onclick="location.href='mypage.html'">내정보</button>
        ${!isMobile ? `<button class="auth-btn btn-ghost" id="logoutBtn">로그아웃</button>` : ``}
      `;
      const logoutBtn = document.getElementById("logoutBtn");
      if (logoutBtn) logoutBtn.addEventListener("click", () => alert("로그아웃 처리"));
    } else {
      rightArea.innerHTML = !isMobile ? `
        ${bellBtnSVG}
        <button class="auth-btn" id="loginBtn">로그인</button>
        <button class="auth-btn" id="signupBtn">회원가입</button>
      ` : `
        ${bellBtnSVG}
      `;
      const loginBtn = `<button class="auth-btn" id="loginBtn">로그인</button>`;
      const signupBtn = document.getElementById("signupBtn");
      if (loginBtn)  loginBtn.addEventListener("click", () => location.href = "login.html");
      if (signupBtn) signupBtn.addEventListener("click", () => location.href = "signup.html");
    }

    // 알림 뱃지(데모)
    const notifyBadge = document.getElementById("notifyBadge");
    if (notifyBadge) notifyBadge.hidden = false;
  }

  // 초기 렌더 & 리스너
  renderRightArea();
  if (mq.addEventListener) mq.addEventListener("change", renderRightArea);
  else if (mq.addListener) mq.addListener(renderRightArea); // 구형 브라우저

  /* -------- 현재 페이지 자동 강조 -------- */
  (function setActiveNav(){
    const path = location.pathname.split('/').pop() || 'index.html';
    const links = document.querySelectorAll('.topnav a, .drawer nav a');
    links.forEach(a=>{
      const href = a.getAttribute('href') || '';
      const isHome = (path === 'index.html') && (href === '/' || href === 'index.html');
      if (isHome || (href && path === href)) {
        a.classList.add('active');
        a.setAttribute('aria-current','page');
      }
    });
  })();

  /* -------- 네비: 휠 세로 -> 가로 스크롤 전환(데스크탑 트랙패드 편의) -------- */
  (function enhanceTopnavScroll(){
    if (!topnav) return;
    topnav.addEventListener('wheel', (e) => {
      // 이미 가로 스크롤이면 패스
      if (Math.abs(e.deltaY) < Math.abs(e.deltaX)) return;
      e.preventDefault();
      topnav.scrollBy({ left: e.deltaY, behavior: 'smooth' });
    }, { passive: false });
  })();

  /* -------- 모바일 드로어 토글 + 접근성 -------- */
  (function initDrawer(){
    if (!hamburger || !drawer || !backdrop || !drawerClose) return;

    function openDrawer(){
      drawer.classList.add("open");
      backdrop.classList.add("show");
      drawer.setAttribute("aria-hidden", "false");
      document.body.style.overflow = 'hidden';
      hamburger.setAttribute('aria-expanded', 'true');
    }
    function closeDrawer(){
      drawer.classList.remove("open");
      backdrop.classList.remove("show");
      drawer.setAttribute("aria-hidden", "true");
      document.body.style.overflow = '';
      hamburger.setAttribute('aria-expanded', 'false');
    }

    // 접근성 속성
    hamburger.setAttribute('aria-controls', 'drawer');
    hamburger.setAttribute('aria-expanded', 'false');

    // 이벤트 바인딩
    hamburger.addEventListener("click", openDrawer);
    drawerClose.addEventListener("click", closeDrawer);
    backdrop.addEventListener("click", closeDrawer);
    window.addEventListener("keydown", (e)=>{ if(e.key === 'Escape') closeDrawer(); });

    // 드로어 내 링크 클릭 시 닫기
    drawer.querySelectorAll('a[href]').forEach(a => {
      a.addEventListener('click', closeDrawer);
    });
  })();

})();
