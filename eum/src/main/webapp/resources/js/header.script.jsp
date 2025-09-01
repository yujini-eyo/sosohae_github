<%@ page language="java"
    contentType="application/javascript; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="true" %>
/* header.js (JSP wrapper)
 * - 로그인 여부에 따라 헤더 우측/드로어 메뉴 자동 렌더링
 * - 데스크탑(>900px)에서는 로그인/회원가입(또는 로그아웃) 버튼 표시
 * - 모바일(<=900px)에서는 헤더에서 로그인/로그아웃 버튼 숨김
 * - 드로어(햄버거) 토글 및 접근성 보강
 * - 현재 페이지 자동 강조
 */
(() => {
  "use strict";

  /* -------- 로그인 상태 결정 (localStorage 우선) -------- */
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

  /* -------- 현재 페이지 자동 강조 (.jsp 기준) -------- */
  function setActiveNav(scope=document){
    const path = (location.pathname.split('/').pop() || 'main.jsp').toLowerCase();
    const linkScope = scope.querySelectorAll ? scope : document; // 안전
    const links = linkScope.querySelectorAll('.topnav a, .drawer nav a');
    links.forEach(a=>{
      a.classList.remove('active');
      a.removeAttribute('aria-current');
      const href = (a.getAttribute('href') || '').toLowerCase();
      const isHome = (path === 'main.jsp') && (href === '/' || href === 'main.jsp');
      if (isHome || (href && path === href)) {
        a.classList.add('active');
        a.setAttribute('aria-current','page');
      }
    });
  }

  /* -------- 드로어 메뉴 (.jsp 링크로 변경) -------- */
  function buildDrawerHTML(loggedIn){
    if (!loggedIn) {
      return `
        <a href="about.jsp">서비스 소개</a>
        <a href="guide.jsp">이용방법</a>
        <a href="notice.jsp">공지</a>
        <a href="login.jsp" id="drawerLogin">로그인</a>
        <a href="register.jsp" id="drawerRegister">회원가입</a>
      `;
    }
    return `
      <a href="about.jsp">서비스 소개</a>
      <a href="guide.jsp">이용방법</a>
      <a href="notice.jsp">공지</a>
      <a href="mypage.jsp">내정보</a>
      <a href="points.jsp">포인트</a>
      <a href="write.jsp">도움 요청하기</a>
      <a href="give.jsp">도움 주기</a>
      <a href="chat.jsp">채팅</a>
      <a href="#" id="drawerLogout">로그아웃</a>
    `;
  }

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

  function renderDrawerMenu(){
    if (!drawerNav) return;
    drawerNav.innerHTML = buildDrawerHTML(isLoggedIn);

    drawerNav.querySelectorAll('a[href]').forEach(a => {
      a.addEventListener('click', closeDrawer);
    });

    const drawerLogin    = drawerNav.querySelector("#drawerLogin");
    const drawerRegister = drawerNav.querySelector("#drawerRegister");
    const drawerLogout   = drawerNav.querySelector("#drawerLogout");

    if (drawerLogin)    drawerLogin.addEventListener("click", ()=> location.href = "login.jsp");
    if (drawerRegister) drawerRegister.addEventListener("click", ()=> location.href = "register.jsp");
    if (drawerLogout){
      drawerLogout.addEventListener("click", (e)=> {
        e.preventDefault();
        localStorage.removeItem("loggedInUser");
        localStorage.removeItem("userPoints");
        isLoggedIn = false; userName = ""; userPoints = 0;
        renderRightArea();
        renderDrawerMenu();
        setActiveNav();
        closeDrawer();
      });
    }
    setActiveNav(drawer);
  }

  /* -------- 헤더 우측 영역 (.jsp 링크로 변경) -------- */
  function renderRightArea() {
    if (!rightArea) return;
    const mobile = mq.matches;

    if (isLoggedIn) {
      rightArea.innerHTML = `
        <span class="user-name">${userName} 님</span>
        <a href="points.jsp" class="user-points">(${userPoints}P)</a>
        ${bellBtnSVG}
        <button class="info-btn" onclick="location.href='mypage.jsp'">내정보</button>
        ${!mobile ? `<button class="auth-btn btn-ghost" id="logoutBtn">로그아웃</button>` : ``}
      `;
      const logoutBtn = document.getElementById("logoutBtn");
      if (logoutBtn) logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("loggedInUser");
        localStorage.removeItem("userPoints");
        isLoggedIn = false; userName = ""; userPoints = 0;
        renderRightArea(); renderDrawerMenu(); setActiveNav();
      });

    } else {
      rightArea.innerHTML = !mobile ? `
        ${bellBtnSVG}
        <button class="auth-btn" id="loginBtn">로그인</button>
        <button class="auth-btn" id="registerBtn">회원가입</button>
      ` : `
        ${bellBtnSVG}
      `;
      const loginBtn    = document.getElementById("loginBtn");
      const registerBtn = document.getElementById("registerBtn");
      if (loginBtn)    loginBtn.addEventListener("click", () => location.href = "login.jsp");
      if (registerBtn) registerBtn.addEventListener("click", () => location.href = "register.jsp");
    }

    const notifyBadge = document.getElementById("notifyBadge");
    if (notifyBadge) notifyBadge.hidden = false;
  }

  // 초기 렌더 & 리스너
  function init(){
    renderRightArea();
    renderDrawerMenu();
    setActiveNav();

    // 드로어 토글/접근성
    if (hamburger && drawer && backdrop && drawerClose){
      hamburger.setAttribute('aria-controls', 'drawer');
      hamburger.setAttribute('aria-expanded', 'false');

      hamburger.addEventListener("click", openDrawer);
      drawerClose.addEventListener("click", closeDrawer);
      backdrop.addEventListener("click", closeDrawer);
      window.addEventListener("keydown", (e)=>{ if(e.key === 'Escape') closeDrawer(); });
    }

    // 네비 스크롤 개선(트랙패드 세로 스크롤 → 가로 스크롤)
    if (topnav){
      topnav.addEventListener('wheel', (e) => {
        if (Math.abs(e.deltaY) < Math.abs(e.deltaX)) return;
        e.preventDefault();
        topnav.scrollBy({ left: e.deltaY, behavior: 'smooth' });
      }, { passive: false });
    }

    // 반응형 전환 시 우측영역 재렌더
    if (mq.addEventListener) mq.addEventListener("change", renderRightArea);
    else if (mq.addListener) mq.addListener(renderRightArea); // 구형 브라우저
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }

  window.addEventListener("popstate", setActiveNav);
  window.addEventListener("hashchange", setActiveNav);
})();
