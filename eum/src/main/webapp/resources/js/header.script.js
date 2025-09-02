(function(){
  "use strict";

  /* ===== 서버 값 주입 ===== */
  var CTX         = "<c:out value='${CTX}'/>";
  var SERVER_USER = "<c:out value='${S_USER}'/>";

  /* ===== 헬퍼 ===== */
  var $    = function(sel, root){ return (root||document).querySelector(sel); };
  var $all = function(sel, root){ return (root||document).querySelectorAll(sel); };

  function getLoginName(){
    if (SERVER_USER) return SERVER_USER;
    try { return localStorage.getItem("loggedInUser") || ""; }
    catch(e){ return ""; }
  }
  function stripCtx(path){
    // 컨텍스트 경로(CTX)를 제거해 비교를 안정화
    if (!path) return "/";
    return path.indexOf(CTX) === 0 ? path.slice(CTX.length) || "/" : path;
  }

  /* ===== 헤더 우측 영역 렌더 ===== */
  function renderRightArea(){
    var el = $("#rightArea");
    if (!el) return;

    var name = getLoginName();
    if (name){
      el.innerHTML =
        '<span class="user-name" aria-label="로그인 사용자">'+ name +'님</span>' +
        '<button type="button" class="auth-btn btn-ghost" data-action="logout">로그아웃</button>';
    } else {
      el.innerHTML =
        '<a class="auth-btn" href="'+ CTX +'/member/login.do" id="loginBtn">로그인</a>' +
        '<a class="auth-btn btn-ghost" href="'+ CTX +'/member/signup.do">회원가입</a>';
    }
  }

  /* ===== 드로어 토글 ===== */
  var isOpen = false;

  function openDrawer(){
    var drawer   = $("#drawer");
    var backdrop = $("#drawerBackdrop");
    var ham      = $("#hamburger");
    if (!drawer || !backdrop || !ham) return;

    drawer.classList.add("open");
    drawer.setAttribute("aria-hidden","false");
    backdrop.hidden = false;
    document.documentElement.classList.add("no-scroll");
    ham.setAttribute("aria-expanded","true");
    isOpen = true;
  }

  function closeDrawer(){
    var drawer   = $("#drawer");
    var backdrop = $("#drawerBackdrop");
    var ham      = $("#hamburger");
    if (!drawer || !backdrop || !ham) return;

    drawer.classList.remove("open");
    drawer.setAttribute("aria-hidden","true");
    backdrop.hidden = true;
    document.documentElement.classList.remove("no-scroll");
    ham.setAttribute("aria-expanded","false");
    isOpen = false;
  }

  function toggleDrawer(){
    isOpen ? closeDrawer() : openDrawer();
  }

  /* ===== 현재 메뉴 강조 ===== */
  function highlightCurrent(){
    var cur = stripCtx(location.pathname);

    $all("#leftArea a[href], .drawer nav a[href]").forEach(function(a){
      try{
        // a.pathname은 절대경로. 컨텍스트 제거 후 비교
        var p = stripCtx(a.pathname);
        if (p === cur || (p !== "/" && cur.indexOf(p) === 0)){
          a.classList.add("active");
          var li = a.closest("li"); if (li) li.classList.add("active");
        }
      }catch(e){}
    });
  }

  /* ===== 이벤트 바인딩 ===== */
  function bindEvents(){
    var ham       = $("#hamburger");
    var closeBtn  = $("#drawerClose");
    var backdrop  = $("#drawerBackdrop");

    if (ham)      ham.addEventListener("click", toggleDrawer);
    if (closeBtn) closeBtn.addEventListener("click", closeDrawer);
    if (backdrop) backdrop.addEventListener("click", closeDrawer);

    // ESC 닫기
    document.addEventListener("keydown", function(ev){
      if (ev.key === "Escape" && isOpen) closeDrawer();
    });

    // 드로어 내 링크 클릭 시 닫기
    $all(".drawer nav a").forEach(function(a){
      a.addEventListener("click", closeDrawer);
    });

    // 로그아웃 처리
    document.addEventListener("click", function(e){
      var t = e.target;
      if (t && (t.matches("[data-action='logout']") || t.classList.contains("btn-logout"))){
        try {
          localStorage.removeItem("loggedInUser");
          localStorage.removeItem("userPoints");
        } catch(e){}
        window.location.href = CTX + "/member/logout.do";
      }
    });

    // (옵션) 우측 영역 축약 토글
    function applyResponsive(){
      var right = $("#rightArea");
      if (!right) return;
      var w = window.innerWidth || document.documentElement.clientWidth;
      if (w <= 900) right.classList.add("is-compact");
      else right.classList.remove("is-compact");
    }
    window.addEventListener("resize", applyResponsive);
    applyResponsive();
  }

  /* ===== 초기화 ===== */
  function init(){
    renderRightArea();
    bindEvents();
    highlightCurrent();
  }

  if (document.readyState === "loading"){
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }

})();
