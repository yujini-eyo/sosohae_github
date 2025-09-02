/* header.script.js — 레이아웃은 그대로, JS만 교체 */
(function () {
  "use strict";

  // === CTX 추출: 현재 스크립트 src에서 '/resources/' 앞까지를 컨텍스트로 간주 ===
  function getCtxFromScript(filename) {
    var node = Array.from(document.scripts || []).find(function (s) {
      return s.src && s.src.indexOf(filename) >= 0;
    });
    if (!node) return window.EUM_CTX || "";
    var u = new URL(node.src, location.href);
    var i = u.pathname.indexOf("/resources/");
    var ctx = i >= 0 ? u.pathname.slice(0, i) : "";
    return (window.EUM_CTX = ctx || "");
  }
  var CTX = getCtxFromScript("header.script.js");

  // === 헬퍼 ===
  var $ = function (s, r) { return (r || document).querySelector(s); };
  var $$ = function (s, r) { return (r || document).querySelectorAll(s); };

  // 로그인 이름: 세션 접근 불가 → localStorage만 사용
  function getLoginName() {
    try { return localStorage.getItem("loggedInUser") || ""; }
    catch (e) { return ""; }
  }

  // 고정 헤더 높이만큼 본문 보정
  var lastH = 0, rafId = 0;
  function headerHeight() {
    var h = $(".site-header");
    return h ? Math.ceil(h.getBoundingClientRect().height) : 0;
  }
  function applyBodyTopPadding(force) {
    var h = headerHeight();
    if (force || h !== lastH) {
      document.body.style.paddingTop = h + "px";
      lastH = h;
    }
  }

  // 우측 사용자 버튼 렌더
  function renderRight() {
    var box = $("#rightArea");
    if (!box) return;
    var name = getLoginName();
    if (name) {
      box.innerHTML =
        '<span class="user-name" aria-label="로그인 사용자">' + name + '님</span>' +
        '<button type="button" class="auth-btn btn-ghost" data-action="logout">로그아웃</button>';
    } else {
      box.innerHTML =
        '<a class="auth-btn" href="' + CTX + '/member/login.do" id="loginBtn">로그인</a>' +
        '<a class="auth-btn btn-ghost" href="' + CTX + '/member/signup.do">회원가입</a>';
    }
  }

  // 드로어 토글
  var isOpen = false;
  function openDrawer() {
    var d = $("#drawer"), b = $("#drawerBackdrop"), h = $("#hamburger");
    if (!d || !b || !h) return;
    d.classList.add("open");
    d.setAttribute("aria-hidden", "false");
    b.hidden = false;
    document.documentElement.classList.add("no-scroll");
    h.setAttribute("aria-expanded", "true");
    isOpen = true;
  }
  function closeDrawer() {
    var d = $("#drawer"), b = $("#drawerBackdrop"), h = $("#hamburger");
    if (!d || !b || !h) return;
    d.classList.remove("open");
    d.setAttribute("aria-hidden", "true");
    b.hidden = true;
    document.documentElement.classList.remove("no-scroll");
    h.setAttribute("aria-expanded", "false");
    isOpen = false;
  }
  function toggleDrawer() { isOpen ? closeDrawer() : openDrawer(); }

  // 현재 메뉴 강조 (#leftArea, .drawer nav)
  function stripCtx(path) {
    if (!path) return "/";
    var c = window.EUM_CTX || "";
    return path.indexOf(c) === 0 ? (path.slice(c.length) || "/") : path;
  }
  function highlightCurrent() {
    var cur = stripCtx(location.pathname);
    $$("#leftArea a[href], .drawer nav a[href]").forEach(function (a) {
      try {
        var p = stripCtx(a.pathname);
        if (p === cur || (p !== "/" && cur.indexOf(p) === 0)) a.classList.add("active");
        else a.classList.remove("active");
      } catch (e) {}
    });
  }

  // 이벤트 바인딩
  function bindEvents() {
    var ham = $("#hamburger"), closeBtn = $("#drawerClose"), backdrop = $("#drawerBackdrop");
    if (ham) ham.addEventListener("click", toggleDrawer);
    if (closeBtn) closeBtn.addEventListener("click", closeDrawer);
    if (backdrop) backdrop.addEventListener("click", closeDrawer);
    document.addEventListener("keydown", function (e) {
      if (e.key === "Escape" && isOpen) closeDrawer();
    });
    $$(".drawer nav a").forEach(function (a) {
      a.addEventListener("click", closeDrawer);
    });

    // 로그아웃
    document.addEventListener("click", function (e) {
      var t = e.target;
      if (t && (t.matches("[data-action='logout']") || t.classList.contains("btn-logout"))) {
        try {
          localStorage.removeItem("loggedInUser");
          localStorage.removeItem("userPoints");
        } catch (e) {}
        location.href = CTX + "/member/logout.do";
      }
    });

    // 리사이즈 시 헤더 보정
    window.addEventListener("resize", function () {
      if (rafId) cancelAnimationFrame(rafId);
      rafId = requestAnimationFrame(function () { applyBodyTopPadding(false); });
    });

    // 폰트 로딩 후 높이 변동 대응
    if (document.fonts && document.fonts.ready) {
      document.fonts.ready.then(function () { applyBodyTopPadding(true); });
    }
  }

  function init() {
    renderRight();
    bindEvents();
    highlightCurrent();
    applyBodyTopPadding(true);
  }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();
