/* header.js
 * - 로그인 여부에 따라 헤더 우측/드로어 메뉴 자동 렌더링
 * - 데스크탑(>900px)에서는 로그인/회원가입(또는 로그아웃) 버튼 표시
 * - 모바일(<=900px)에서는 헤더에서 로그인/로그아웃 버튼 숨김
 * - 드로어(햄버거) 토글 및 접근성 보강
 * - 현재 페이지 자동 강조
 */

(() => {
  "use strict";

  /* -------- 로그인 상태 결정 (localStorage 우선) -------- */
  // 예: 로그인 성공 시 어딘가에서 
  // localStorage.setItem('loggedInUser','홍길동'); 
  // localStorage.setItem('userPoints','1250');
  const savedUser = localStorage.getItem("loggedInUser");
  let isLoggedIn  = !!savedUser && savedUser !== "null";
  let userName    = isLoggedIn ? savedUser : "";
  let userPoints  = isLoggedIn ? Number(localStorage.getItem("userPoints") || 0) : 0;

  /* -------- DOM -------- */
  const rightArea   = document.getElementById("rightArea");
  const topnav      = document.getElementById("topnav");
  const hamburger   = document.getElementById("hamburger");
  const drawer      = document.getElementById("drawer");
  const backdrop    = document.getElementById("drawerBackdrop");
  const drawerClose = document.getElementById("drawerClose");
  const drawerNav   = drawer ? drawer.querySelector("nav") : null;

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

  /* -------- 현재 페이지 자동 강조 (함수화) -------- */
  function setActiveNav(scope=document){
    const path = location.pathname.split('/').pop() || 'index.html';
    const links = scope.querySelectorAll('.topnav a, .drawer nav a');
    links.forEach(a=>{
      a.classList.remove('active');
      a.removeAttribute('aria-current');
      const href = a.getAttribute('href') || '';
      const isHome = (path === 'index.html') && (href === '/' || href === 'index.html');
      if (isHome || (href && path === href)) {
        a.classList.add('active');
        a.setAttribute('aria-current','page');
      }
    });
  }

  /* -------- 드로어 메뉴 HTML -------- */
  function buildDrawerHTML(loggedIn){
    if (!loggedIn) {
      // 비로그인: 서비스 소개, 이용방법, 공지, 로그인
      return `
        <a href="about.html">서비스 소개</a>
        <a href="guide.html">이용방법</a>
        <a href="notice.html">공지</a>
        <a href="login.html" id="drawerLogin">로그인</a>
        <a href="register.html" id="drawerLogin">회원가입</a>
      `;
    }
    // 로그인: 서비스 소개, 이용방법, 공지, 내정보, 포인트, 도움 요청하기, 도움 주기, 로그아웃
    return `
      <a href="about.html">서비스 소개</a>
      <a href="guide.html">이용방법</a>
      <a href="notice.html">공지</a>
      <a href="mypage.html">내정보</a>
      <a href="points.html">포인트</a>
      <a href="wirte.html">도움 요청하기</a>
      <a href="give.html">도움 주기</a>
      <a href="chat.html">채팅</a>
      <a href="#" id="drawerLogout">로그아웃</a>
    `;
  }

  /* -------- 드로어 열고/닫기 (외부에서도 쓰이게 함수 분리) -------- */
  function openDrawer(){
    if (!drawer || !backdrop || !hamburger) return;
    drawer.classList.add("open");
    backdrop.classList.add("show");
    drawer.setAttribute("aria-hidden", "false");
    document.body.style.overflow = 'hidden';
    hamburger.setAttribute('aria-expanded', 'true');
  }
  function closeDrawer(){
    if (!drawer || !backdrop || !hamburger) return;
    drawer.classList.remove("open");
    backdrop.classList.remove("show");
    drawer.setAttribute("aria-hidden", "true");
    document.body.style.overflow = '';
    hamburger.setAttribute('aria-expanded', 'false');
  }

  /* -------- 드로어 메뉴 렌더링 -------- */
  function renderDrawerMenu(){
    if (!drawerNav) return;
    drawerNav.innerHTML = buildDrawerHTML(isLoggedIn);

    // 드로어 내 링크 클릭 시 닫기
    drawerNav.querySelectorAll('a[href]').forEach(a => {
      a.addEventListener('click', closeDrawer);
    });

    // 로그인/로그아웃 핸들러
    const drawerLogin  = drawerNav.querySelector("#drawerLogin");
    const drawerLogout = drawerNav.querySelector("#drawerLogout");
    if (drawerLogin){
      drawerLogin.addEventListener("click", ()=> {
        // 필요 시 추가 로직 가능
        // 예: 로그인 페이지 이동
        location.href = "login.html";  // 이미 href로 이동함
      });
    }
    if (drawerLogout){
      drawerLogout.addEventListener("click", (e)=> {
        e.preventDefault();
        localStorage.removeItem("loggedInUser");
        localStorage.removeItem("userPoints");
        isLoggedIn = false;
        userName = "";
        userPoints = 0;
        renderRightArea();
        renderDrawerMenu();
        setActiveNav();
        closeDrawer();
      });
    }

    setActiveNav(drawer);
  }

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
      if (logoutBtn) logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("loggedInUser");
        localStorage.removeItem("userPoints");
        isLoggedIn = false;
        userName = "";
        userPoints = 0;
        renderRightArea();
        renderDrawerMenu();
        setActiveNav();
      });

    } else {
      rightArea.innerHTML = !isMobile ? `
        ${bellBtnSVG}
        <button class="auth-btn" id="loginBtn">로그인</button>
        <button class="auth-btn" id="registerBtn">회원가입</button>
      ` : `
        ${bellBtnSVG}
      `;
      const loginBtn  = document.getElementById("loginBtn");   // (버그 수정) 문자열이 아니라 요소 선택
      const registerBtn = document.getElementById("registerBtn");
      if (loginBtn)  loginBtn.addEventListener("click", () => location.href = "login.html");
      if (registerBtn) registerBtn.addEventListener("click", () => location.href = "register.html");
    }

    // 알림 뱃지(데모)
    const notifyBadge = document.getElementById("notifyBadge");
    if (notifyBadge) notifyBadge.hidden = false;
  }

  // 초기 렌더 & 리스너
  renderRightArea();
  renderDrawerMenu();
  if (mq.addEventListener) mq.addEventListener("change", ()=>{ renderRightArea(); });
  else if (mq.addListener) mq.addListener(renderRightArea); // 구형 브라우저

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

    // 접근성 속성
    hamburger.setAttribute('aria-controls', 'drawer');
    hamburger.setAttribute('aria-expanded', 'false');

    // 이벤트 바인딩
    hamburger.addEventListener("click", openDrawer);
    drawerClose.addEventListener("click", closeDrawer);
    backdrop.addEventListener("click", closeDrawer);
    window.addEventListener("keydown", (e)=>{ if(e.key === 'Escape') closeDrawer(); });

    // (처음 로드 시에도 nav가 존재하면 링크 닫기 바인딩은 renderDrawerMenu에서 수행)
  })();

  // 페이지 첫 로드 시 현재 메뉴 강조
  setActiveNav();
})();
