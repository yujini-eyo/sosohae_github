/* header.script.js — 레이아웃은 그대로, JS만 교체 */
(function () {
  "use strict";

  // === 셀렉터 헬퍼 ===
  var $  = function (s, r) { return (r || document).querySelector(s); };
  var $$ = function (s, r) { return Array.prototype.slice.call((r || document).querySelectorAll(s)); };

  // === 컨텍스트 경로 추출: data-ctx > window.EUM_CTX > 스크립트 경로
  function detectCtx() {
    var right = $("#rightArea");
    if (right && right.dataset && right.dataset.ctx) return (window.EUM_CTX = right.dataset.ctx);
    if (window.EUM_CTX) return window.EUM_CTX;
    var node = Array.prototype.find.call(document.scripts || [], function (s) {
      return s.src && s.src.indexOf("header.script.js") >= 0;
    });
    if (!node) return (window.EUM_CTX = "");
    try {
      var u = new URL(node.src, location.href);
      var i = u.pathname.indexOf("/resources/");
      return (window.EUM_CTX = i >= 0 ? u.pathname.slice(0, i) : "");
    } catch (e) { return (window.EUM_CTX = ""); }
  }
  var CTX = detectCtx();

  // === 로그인 상태/이름: data-auth 우선, 없으면 localStorage
  function isAuthed() {
    var right = $("#rightArea");
    if (right && right.dataset && typeof right.dataset.auth !== "undefined") {
      return String(right.dataset.auth) === "true";
    }
    try { return !!localStorage.getItem("loggedInUser"); } catch (e) { return false; }
  }
  function loginName() {
    var right = $("#rightArea");
    if (right && right.dataset && right.dataset.username) return right.dataset.username;
    try { return localStorage.getItem("loggedInUser") || ""; } catch (e) { return ""; }
  }

  // === 헤더 높이 → body 패딩 + CSS 변수(--header-h) 동기화
  var lastH = 0, rafId = 0;
  function headerHeight() {
    var h = $(".site-header");
    return h ? Math.ceil(h.getBoundingClientRect().height) : 0;
  }
  function applyHeaderCompensation(force) {
    var h = headerHeight();
    if (force || h !== lastH) {
      document.body.style.paddingTop = h + "px";
      document.documentElement.style.setProperty("--header-h", h + "px");
      lastH = h;
    }
  }

  // === 우측 사용자 버튼 렌더
  function renderRight() {
    var box = $("#rightArea");
    if (!box) return;
    if (isAuthed()) {
      var name = loginName();
      box.innerHTML =
        (name ? '<span class="user-name" aria-label="로그인 사용자">' + name + '님</span>' : "") +
        '<button type="button" class="auth-btn btn-ghost" data-action="logout">로그아웃</button>';
    } else {
      box.innerHTML =
        '<a class="auth-btn" href="' + CTX + '/loginForm.do" id="loginBtn">로그인</a>' +
        '<a class="auth-btn btn-ghost" href="' + CTX + '/signupForm.do">회원가입</a>';
    }
  }

  // === 드로어 토글
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
    var first = $("a,button", d);
    if (first) first.focus();
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
    h.focus();
  }
  function toggleDrawer(){ isOpen ? closeDrawer() : openDrawer(); }

  // === 현재 메뉴 강조
  function stripCtx(path) {
    if (!path) return "/";
    var c = window.EUM_CTX || CTX || "";
    return path.indexOf(c) === 0 ? (path.slice(c.length) || "/") : path;
  }
  function normalize(p){
    if (!p) return "/";
    try { p = new URL(p, location.origin).pathname; } catch(e){}
    p = stripCtx(p);
    if (p.length > 1 && p.endsWith("/")) p = p.slice(0, -1);
    return p;
  }
  function highlightCurrent() {
    var cur = normalize(location.pathname);
    $$("#leftArea a[href], .drawer nav a[href]").forEach(function (a) {
      var p = normalize(a.getAttribute("href") || a.pathname || "");
      if (p === cur || (p !== "/" && cur.indexOf(p) === 0)) a.classList.add("active");
      else a.classList.remove("active");
    });
  }

  // === 이벤트 바인딩
  function bindEvents() {
    var ham = $("#hamburger"), closeBtn = $("#drawerClose"), backdrop = $("#drawerBackdrop");
    if (ham) ham.addEventListener("click", toggleDrawer);
    if (closeBtn) closeBtn.addEventListener("click", closeDrawer);
    if (backdrop) backdrop.addEventListener("click", closeDrawer);

    document.addEventListener("keydown", function (e) {
      if (e.key === "Escape" && isOpen) { e.preventDefault(); closeDrawer(); }
    });
    $$(".drawer nav a").forEach(function (a) { a.addEventListener("click", closeDrawer); });

    // 로그아웃 POST
    document.addEventListener("click", function (e) {
      var t = e.target;
      if (!t) return;
      if (t.matches("[data-action='logout'], .btn-logout")) {
        try { localStorage.removeItem("loggedInUser"); localStorage.removeItem("userPoints"); } catch(e){}
        var form = document.createElement("form");
        form.method = "post";
        form.action = (CTX || "") + "/logout.do";
        document.body.appendChild(form); form.submit();
      }
    });

    // 리사이즈/폰트 로딩에 따른 높이 갱신
    window.addEventListener("resize", function () {
      if (rafId) cancelAnimationFrame(rafId);
      rafId = requestAnimationFrame(function(){ applyHeaderCompensation(false); });
    });
    if (document.fonts && document.fonts.ready) {
      document.fonts.ready.then(function(){ applyHeaderCompensation(true); });
    }
  }

  function init() {
    renderRight();
    bindEvents();
    highlightCurrent();
    applyHeaderCompensation(true);
  }

  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();
