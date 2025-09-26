/* header.script.js — 헤더/드로어 + 실시간 포인트 (logout: 단순 링크 이동, CTX 적용) */
(function () {
  "use strict";
  if (window.__EUM_HEADER_INITED__) return;
  window.__EUM_HEADER_INITED__ = true;

  var $  = function (s, r) { return (r || document).querySelector(s); };
  var $$ = function (s, r) { return Array.prototype.slice.call((r || document).querySelectorAll(s)); };

  function getHeaderEl(){
    var h = document.getElementById("header") || document.querySelector("header.site-header");
    if (h && !h.id) h.id = "header";
    if (h && !h.hasAttribute("data-eum-header")) h.setAttribute("data-eum-header","");
    return h;
  }
  function hasTransformAncestor(el){
    var p = el && el.parentElement;
    while (p && p !== document.body){
      var cs = getComputedStyle(p);
      if ((cs.transform && cs.transform !== "none") ||
          (cs.filter && cs.filter !== "none") ||
          (cs.perspective && cs.perspective !== "none") ||
          (cs.willChange && /transform|filter|perspective/.test(cs.willChange))) return true;
      p = p.parentElement;
    }
    return false;
  }
  function ensureHeaderRoot(force){
    var h = getHeaderEl(); if (!h) return;
    if (h.getAttribute("data-rootfix") === "off") return;
    if (force || hasTransformAncestor(h) || h.parentElement !== document.body) {
      document.body.insertBefore(h, document.body.firstChild || null);
      h.classList.add("header-fixed-root");
    }
    h.style.position = "fixed";
    h.style.top = "0px";
    h.style.left = "0";
    h.style.right = "0";
    h.style.transform = "none";
    h.style.zIndex = "2147483647";
  }
  if (document.readyState !== "loading") ensureHeaderRoot(true);
  else document.addEventListener("DOMContentLoaded", function(){ ensureHeaderRoot(true); });

  /* 컨텍스트 경로 */
  function detectCtx() {
    var right = $("#rightArea");
    if (right && right.dataset && right.dataset.ctx) return (window.EUM_CTX = right.dataset.ctx);
    if (window.EUM_CTX) return window.EUM_CTX;

    var scripts = document.scripts || [];
    var node = null;
    for (var si = 0; si < scripts.length; si++) {
      var s = scripts[si];
      if (s && s.src && s.src.indexOf("header.script.js") >= 0) { node = s; break; }
    }
    if (!node) return (window.EUM_CTX = "");
    try {
      var u = new URL(node.src, location.href);
      var i = u.pathname.indexOf("/resources/");
      return (window.EUM_CTX = (i >= 0 ? u.pathname.slice(0, i) : ""));
    } catch (e) { return (window.EUM_CTX = ""); }
  }
  var CTX = detectCtx();
  function url(p){
    if(!p) return "#";
    p = String(p);
    if(/^https?:\/\//i.test(p)) return p;
    return (CTX || "") + (p.charAt(0)==="/" ? p : "/"+p);
  }

  /* dataset 링크: 모든 항목에 CTX 적용 */
  function getLinkFromData(name, fallback){
    var ra = document.getElementById('rightArea');
    var v = (ra && ra.dataset) ? ra.dataset[name] : "";
    if (v && typeof v === "string") return url(v.trim());
    return fallback || "#";
  }
  function getMyPageLink(){ return getLinkFromData("mypage", url("/member/mypage.do")); }
  function getPointLink(){  return getLinkFromData("point",  url("/member/point.do"));  }
  function getNotifyLink(){ return getLinkFromData("notify", url("/notify.do")); }
  function getLogoutLink(){ return getLinkFromData("logout", url("/member/logout.do")); }
  function getLoginLink(){  return getLinkFromData("login",  url("/member/loginForm.do")); }
  function getSignupLink(){ return getLinkFromData("signup", url("/member/signupForm.do")); }

  /* 인증 표시 관련(프론트 전용) */
  function isAuthed() {
    var right = document.getElementById('rightArea');
    if (right && right.dataset && typeof right.dataset.auth !== "undefined") {
      return String(right.dataset.auth) === "true";
    }
    try { return !!localStorage.getItem("loggedInUser"); } catch (e) { return false; }
  }
  function loginName() {
    var right = document.getElementById('rightArea');
    if (right && right.dataset && right.dataset.username) return right.dataset.username;
    try { return localStorage.getItem("loggedInUser") || ""; } catch (e) { return ""; }
  }
  function getPoints() {
    var ra = document.getElementById('rightArea');
    if (ra && ra.dataset && typeof ra.dataset.points !== "undefined") {
      var n = Number(ra.dataset.points);
      if (!isNaN(n)) return n;
    }
    try { return Number(localStorage.getItem("userPoints") || 0); } catch(e){ return 0; }
  }
  function readUser(){
    var name = loginName();
    return { isLogged: isAuthed() && !!name, name: name, points: getPoints() };
  }

  var rightArea = null, leftArea = null;
  function RA(){ return rightArea || (rightArea = $("#rightArea")); }
  function LA(){ return leftArea  || (leftArea  = $("#leftArea"));  }

  var bellBtnSVG =
    '<button class="icon-btn" id="notifyBtn" aria-label="알림" aria-haspopup="dialog" aria-controls="notifyPanel">' +
      '<svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">' +
        '<path d="M12 22a2 2 0 0 0 2-2H10a2 2 0 0 0 2 2Zm6-6V11a6 6 0 1 0-12 0v5l-2 2v1h16v-1l-2-2Z" fill="currentColor"></path>' +
      '</svg><span class="badge" id="notifyBadge" hidden></span>' +
    '</button>';

  /* 드로어 */
  var isOpen = false;
  function openDrawer() {
    var d = $("#drawer"), b = $("#drawerBackdrop"), ham = $("#hamburger");
    if (!d || !b || !ham) return;
    d.classList.add("open");
    d.setAttribute("aria-hidden", "false");
    b.hidden = false;
    document.documentElement.classList.add("no-scroll");
    ham.setAttribute("aria-expanded", "true");
    isOpen = true;
    var first = $("a,button,[tabindex='0']", d);
    if (first) first.focus();
  }
  function closeDrawer() {
    var d = $("#drawer"), b = $("#drawerBackdrop"), ham = $("#hamburger");
    if (!d || !b || !ham) return;
    d.classList.remove("open");
    d.setAttribute("aria-hidden", "true");
    b.hidden = true;
    document.documentElement.classList.remove("no-scroll");
    ham && ham.setAttribute("aria-expanded", "false");
    isOpen = false;
    ham && ham.focus();
  }
  function toggleDrawer(){ isOpen ? closeDrawer() : openDrawer(); }

  function preventDrop(){
    ensureHeaderRoot(true);
    var h = getHeaderEl(); if (!h) return;
    if (Math.round(h.getBoundingClientRect().top) !== 0) h.style.top = "0px";
  }
  window.addEventListener('scroll', preventDrop, { passive: true });
  window.addEventListener('resize', preventDrop);
  window.addEventListener('orientationchange', preventDrop);
  if (document.fonts && document.fonts.ready) { document.fonts.ready.then(preventDrop); }

  function goSafely(href){
    if (!href || href === "#") return;
    if (isOpen) { closeDrawer(); setTimeout(function(){ location.href = href; }, 0); }
    else { location.href = href; }
  }
  function ensureNav(el, href){
    if (!el || !href || !String(href).trim()) return;
    el.setAttribute("href", href);
    if (!el.__bound_go__) {
      var go = function(e){ e.preventDefault(); e.stopPropagation(); goSafely(href); };
      el.addEventListener("click", go, { capture: true, passive: false });
      el.addEventListener("keydown", function(e){ if (e.key === "Enter" || e.key === " ") go(e); }, { capture: true });
      el.__bound_go__ = true;
    }
  }

  function buildLeftNavHTML(){
    return ''
      + '<a href="'+url('/main.do')+'">홈</a>'
      + '<a href="'+url('/about.do')+'">서비스 소개</a>'
      + '<a href="'+url('/guide.do')+'">이용방법</a>'
      + '<a href="'+url('/notice.do')+'">공지</a>'
      + '<a href="'+url('/board/articleForm.do')+'">도움 요청하기</a>'
      + '<a href="'+url('/board/listArticles.do')+'">도움 주기</a>'
      + '<a href="'+url('/member/mypage.do')+'">내 정보</a>'
      + '<a href="'+url('/member/point.do')+'">포인트</a>'
      + '<a href="'+url('/chat/list.do')+'">채팅 리스트</a>';
  }
  function renderLeftNav(){
    var left = LA(); if (!left) return;
    if (!left.children.length) left.innerHTML = buildLeftNavHTML();
  }

  function renderDrawerMenu() {
    var state = readUser();
    var mount = document.getElementById("drawerMenu");
    if (!mount) return;
    var common =
      '<a href="'+url('/main.do')+'">홈</a>' +
      '<a href="'+url('/about.do')+'">서비스 소개</a>' +
      '<a href="'+url('/guide.do')+'">이용방법</a>' +
      '<a href="'+url('/notice.do')+'">공지</a>';
    var html = state.isLogged
      ? common +
    	'<a href="'+url('/board/articleForm.do')+'">도움 요청하기</a>' +
        '<a href="'+url('/board/listArticles.do')+'">도움 주기</a>' +
        '<a href="'+url('/member/mypage.do')+'">내 정보</a>' +
        '<a href="'+url('/chat/list.do')+'">채팅 리스트</a>' +
        '<a href="'+ getLogoutLink() +'">로그아웃</a>'
      : common +
        '<a href="'+getLoginLink()+'">로그인</a>' +
        '<a href="'+getSignupLink()+'">회원가입</a>';

    if (!(mount.dataset.filled === '1' && mount.innerHTML.trim() === html.trim())) {
      mount.innerHTML = html;
      mount.dataset.filled = '1';
      $$('#drawerMenu a, #drawerMenu button').forEach(function (el) {
        el.addEventListener("click", function(){ closeDrawer(); }, { passive:true });
      });
    }
    if (!mount._observerAttached) {
      var obs = new MutationObserver(function(){
        if (!mount.firstElementChild || !mount.innerHTML.trim()) {
          mount.innerHTML = html;
          $$('#drawerMenu a, #drawerMenu button').forEach(function (el) {
            el.addEventListener("click", function(){ closeDrawer(); }, { passive:true });
          });
        }
      });
      obs.observe(mount, { childList:true });
      mount._observerAttached = true;
    }
  }

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
    $$("#leftArea a[href], .drawer-links a[href]").forEach(function (a) {
      var p = normalize(a.getAttribute("href") || a.pathname || "");
      if (p === cur || (p !== "/" && cur.indexOf(p) === 0)) a.classList.add("active");
      else a.classList.remove("active");
    });
  }

  function updatePointsDom(p) {
    var h = $("#userPointsLink");  if (h) { h.textContent = p + "P"; h.setAttribute('aria-live','polite'); }
    var d = $("#drawerPoints");    if (d) d.textContent = p + "P";
  }
  function startLivePoints() {
    var last = getPoints();
    updatePointsDom(last);
    setInterval(function () {
      var cur = getPoints();
      if (cur !== last) { last = cur; updatePointsDom(cur); }
    }, 1000);
    window.addEventListener("storage", function (e) {
      if (e.key === "userPoints") updatePointsDom(Number(e.newValue || 0));
      if (e.key === "loggedInUser") scheduleRender();
    });
    document.addEventListener("visibilitychange", function(){
      if (!document.hidden) { updatePointsDom(getPoints()); }
    });
  }

  function renderRightArea() {
    var area = RA(); if (!area) return;
    var state = readUser();
    var MYPAGE_LINK = getMyPageLink();
    var POINT_LINK  = getPointLink();
    var NOTIFY_LINK = getNotifyLink();

    if (state.isLogged) {
      area.innerHTML =
        '<span class="hide-600">' +
          '<a href="'+POINT_LINK+'" class="user-points" id="userPointsLink" aria-live="polite">'+state.points+'P</a>' +
          '<span class="sep" aria-hidden="true">|</span>' +
        '</span>' +
        '<a href="'+MYPAGE_LINK+'" class="user-name" id="userNameLink">'+ (state.name||"") +' 님</a>' +
        '<span class="sep" aria-hidden="true">|</span>' +
        '<span class="hide-600">' +
          bellBtnSVG +
          '<span class="sep" aria-hidden="true">|</span>' +
        '</span>' +
        '<a href="'+ getLogoutLink() +'" class="auth-btn btn-ghost">로그아웃</a>';
    } else {
      area.innerHTML =
        '<button type="button" class="auth-btn" id="loginBtn">로그인</button>' +
        '<span class="sep" aria-hidden="true">|</span>' +
        '<button type="button" class="auth-btn" id="signupBtn">회원가입</button>';

      var loginBtn  = $("#loginBtn");
      var signupBtn = $("#signupBtn");
      if (loginBtn && !loginBtn.__bound__)  { loginBtn.addEventListener("click", function(){ goSafely(getLoginLink()); }); loginBtn.__bound__=true; }
      if (signupBtn && !signupBtn.__bound__){ signupBtn.addEventListener("click", function(){ goSafely(getSignupLink()); }); signupBtn.__bound__=true; }
    }

    var nb = $("#notifyBtn");
    if (nb && !nb.__bound__) {
      nb.addEventListener("click", function(e){ e.preventDefault(); goSafely(NOTIFY_LINK); });
      nb.__bound__ = true;
    }

    ensureNav($("#userNameLink"),   MYPAGE_LINK);
    ensureNav($("#userPointsLink"), POINT_LINK);
    renderDrawerMenu();
  }

  function bindEvents() {
    var ham = $("#hamburger"), closeBtn = $("#drawerClose"), backdrop = $("#drawerBackdrop");
    if (ham && !ham.__bound__) { ham.addEventListener("click", toggleDrawer); ham.__bound__=true; }
    if (closeBtn && !closeBtn.__bound__) { closeBtn.addEventListener("click", closeDrawer); closeBtn.__bound__=true; }
    if (backdrop && !backdrop.__bound__) { backdrop.addEventListener("click", closeDrawer); backdrop.__bound__=true; }

    document.addEventListener("keydown", function (e) {
      if (e.key === "Escape" && isOpen) { e.preventDefault(); closeDrawer(); }
    });

    preventDrop();
  }

  var renderTimer = 0;
  function scheduleRender(){
    if (renderTimer) return;
    renderTimer = window.requestAnimationFrame(function(){
      renderTimer = 0;
      renderLeftNav();
      renderRightArea();
      highlightCurrent();
      preventDrop();
    });
  }

  function setupRightAreaHitTest(){
    var area = RA();
    if (!area || area.__hitTestBound__) return;
    area.addEventListener("click", function(e){
      var nameEl   = $("#userNameLink");
      var pointEl  = $("#userPointsLink");
      var nameHref = getMyPageLink();
      var pointHref= getPointLink();
      function within(el){
        if (!el) return false;
        var r = el.getBoundingClientRect();
        var x = e.clientX, y = e.clientY;
        return x >= r.left && x <= r.right && y >= r.top && y <= r.bottom;
      }
      if (within(nameEl))  { e.preventDefault(); e.stopPropagation(); goSafely(nameHref); }
      if (within(pointEl)) { e.preventDefault(); e.stopPropagation(); goSafely(pointHref); }
    }, true);
    area.__hitTestBound__ = true;
  }

  function attachRecovery(){
    var ra = RA();
    if (ra && !ra.__recoverObserver__) {
      var obs = new MutationObserver(function(){
        if (!ra.innerHTML || !ra.querySelector(".user-points,.user-name,.auth-btn,#notifyBtn")) scheduleRender();
      });
      obs.observe(ra, { childList:true, subtree:false });
      ra.__recoverObserver__ = true;
    }
  }

  function init(){
    rightArea = $("#rightArea");
    leftArea  = $("#leftArea");

    ensureHeaderRoot(true);
    renderLeftNav();
    renderRightArea();
    renderDrawerMenu();
    bindEvents();
    setupRightAreaHitTest();
    highlightCurrent();
    startLivePoints();
    preventDrop();
    attachRecovery();
  }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();
