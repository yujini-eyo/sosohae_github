/* footer.script.js — 모바일 탭바(<=900px) 로그인 상태별 렌더링 */
(function () {
  "use strict";
  if (window.__EUM_FOOTER_INITED__) return;
  window.__EUM_FOOTER_INITED__ = true;

  function $(s, r){ return (r||document).querySelector(s); }
  function $all(s, r){ return Array.prototype.slice.call((r||document).querySelectorAll(s)); }
  function isMobile(){ return window.matchMedia && window.matchMedia("(max-width: 900px)").matches; }

  /* ===== CTX 탐지: footer(data-ctx) → rightArea → 기존 전역 → 스크립트 경로 */
  function detectCtx(){
    var f = $("#eumFooter");
    if (f && f.dataset && f.dataset.ctx){ return (window.EUM_CTX = f.dataset.ctx); }
    var ra = $("#rightArea");
    if (ra && ra.dataset && ra.dataset.ctx){ return (window.EUM_CTX = ra.dataset.ctx); }
    if (window.EUM_CTX){ return window.EUM_CTX; }
    var node = null, scripts = document.scripts || [];
    for (var i=0;i<scripts.length;i++){
      if (scripts[i].src && scripts[i].src.indexOf("footer.script.js")>=0){ node = scripts[i]; break; }
    }
    if (!node) return (window.EUM_CTX = "");
    try{
      var u = new URL(node.src, location.href);
      var idx = u.pathname.indexOf("/resources/");
      return (window.EUM_CTX = (idx>=0 ? u.pathname.slice(0, idx) : ""));
    }catch(e){ return (window.EUM_CTX = ""); }
  }
  var CTX = detectCtx();
  function url(p){
    if (!p) return "#";
    p = String(p);
    if (/^https?:\/\//i.test(p)) return p;
    return (CTX||"") + (p.charAt(0)==="/" ? p : "/"+p);
  }

  function footerRoot(){ return $("#eumFooter") || $("#siteFooter") || $("footer.site-footer"); }
  function getData(name, fallback){
    var f = footerRoot();
    if (f && f.dataset && f.dataset[name]) return f.dataset[name];
    var ra = $("#rightArea");
    if (ra && ra.dataset && ra.dataset[name]) return ra.dataset[name];
    return fallback || "";
  }

  /* ===== 링크 수집 */
  function linkAbout()     { return url(getData("about",      "/about.do")); }
  function linkGuide()     { return url(getData("guide",      "/guide.do")); }
  function linkLogin()     { return url(getData("login",      "/member/loginForm.do")); }
  function linkSignup()    { return url(getData("signup",     "/member/signupForm.do")); }
  function linkHelpWrite() { return url(getData("helpwrite",  "/help/write.do")); }
  function linkBoardList() { return url(getData("boardlist",  "/board/listArticles.do")); }
  function linkChat()      { return url(getData("chat",       "/chat/list.do")); }
  function linkMypage()    { return url(getData("mypage",     "/member/mypage.do")); }

  /* ===== 인증 상태 */
  function isAuthed(){
    var f = footerRoot();
    if (f && f.dataset && typeof f.dataset.auth !== "undefined") return String(f.dataset.auth) === "true";
    var ra = $("#rightArea");
    if (ra && ra.dataset && typeof ra.dataset.auth !== "undefined") return String(ra.dataset.auth) === "true";
    try { return !!localStorage.getItem("loggedInUser"); } catch(e){ return false; }
  }

  /* ===== 탭 구성 */
  function tabsForGuest(){
    return [
      {key:"about", href: linkAbout(),     label:"서비스 소개"},
      {key:"guide", href: linkGuide(),     label:"이용방법"},
      {key:"login", href: linkLogin(),     label:"로그인"},
      {key:"signup",href: linkSignup(),    label:"회원가입"}
    ];
  }
  function tabsForMember(){
    return [
      {key:"write", href: linkHelpWrite(), label:"요청하기"},
      {key:"give",  href: linkBoardList(), label:"도움주기"},
      {key:"chat",  href: linkChat(),      label:"채팅"},
      {key:"me",    href: linkMypage(),    label:"내정보"}
    ];
  }

  /* ===== 탭바 DOM */
  function ensureBar(){
    var bar = $("#mobileTabbar");
    if (bar) {
      if (bar.parentNode !== document.body) document.body.appendChild(bar);
      return bar;
    }
    bar = document.createElement("nav");
    bar.id = "mobileTabbar";
    bar.className = "mobile-tabbar";
    bar.setAttribute("role","navigation");
    bar.setAttribute("aria-label","모바일 하단 탭");
    // 텍스트 색은 페이지 기본색 상속
    bar.style.color = "inherit";
    document.body.appendChild(bar);
    return bar;
  }

  /* ===== 경로/활성 판단 */
  function normalizePath(href){
    try{ return new URL(href, location.origin).pathname.replace(/\/+$/,"") || "/"; }
    catch(e){ return "/"; }
  }
  function currentPath(){
    try{ return location.pathname.replace(/\/+$/,"") || "/"; }
    catch(e){ return "/"; }
  }
  function isActive(href){
    var cur = currentPath();
    var p = normalizePath(href);
    if (p === "/") return cur === "/";
    return cur === p || (p !== "/" && cur.indexOf(p) === 0);
  }

  /* ===== 렌더링 & 갱신 */
  function render(){
    var bar = ensureBar();
    var items = isAuthed() ? tabsForMember() : tabsForGuest();
    var html = items.map(function(it){
      var active = isActive(it.href) ? " active" : "";
      return '<a class="tab'+active+'" href="'+it.href+'" data-key="'+it.key+'"><span class="tab-label">'+it.label+'</span></a>';
    }).join("");

    if (bar.__html__ !== html){
      bar.innerHTML = html;
      bar.__html__  = html;
    }
    updateAria();
    applyBodyBottomPadding();
    showHideByViewport();
  }

  function updateAria(){
    var bar = $("#mobileTabbar"); if (!bar) return;
    $all(".tab", bar).forEach(function(a){
      if (a.classList.contains("active")) a.setAttribute("aria-current","page");
      else a.removeAttribute("aria-current");
    });
  }

  function refreshActive(){
    var bar = $("#mobileTabbar"); if (!bar) return;
    $all(".tab", bar).forEach(function(a){
      var on = isActive(a.getAttribute("href"));
      if (on){ a.classList.add("active"); a.setAttribute("aria-current","page"); }
      else   { a.classList.remove("active"); a.removeAttribute("aria-current"); }
    });
  }

  /* ===== 표시 제어 & 안전 패딩(footer.pad.js가 없어도 최소 보장) */
  var lastPB = null, rafId = 0;
  function showHideByViewport(){
    var bar = $("#mobileTabbar"); if (!bar) return;
    // CSS가 없더라도 데스크톱에서 눈에 띄지 않도록
    bar.style.display = isMobile() ? "" : "none";
  }
  function applyBodyBottomPadding(){
    var bar = $("#mobileTabbar"); if (!bar) return;
    var shown = isMobile() && (getComputedStyle(bar).display !== "none");
    var h = shown ? Math.ceil(bar.getBoundingClientRect().height) : 0;
    if (lastPB === h) return;
    lastPB = h;
    // pad.js가 있으면 그쪽이 덮어쓸 수 있음
    document.body.style.paddingBottom = h ? (h + "px") : "";
  }

  function scheduleRefresh(){ 
    if (rafId) cancelAnimationFrame(rafId);
    rafId = requestAnimationFrame(function(){ rafId = 0; refreshActive(); applyBodyBottomPadding(); showHideByViewport(); });
  }

  /* ===== 이벤트 */
  function bind(){
    window.addEventListener("resize", scheduleRefresh);
    window.addEventListener("orientationchange", scheduleRefresh);
    window.addEventListener("popstate", scheduleRefresh);
    window.addEventListener("hashchange", scheduleRefresh);
    window.addEventListener("load", scheduleRefresh);

    window.addEventListener("storage", function(e){
      if (e.key === "loggedInUser") render();
    });

    var ra = $("#rightArea");
    if (ra && !ra.__footerObs__){
      try{
        var obs = new MutationObserver(function(muts){
          for (var i=0;i<muts.length;i++){
            if (muts[i].type === "attributes" && muts[i].attributeName === "data-auth"){ render(); break; }
          }
        });
        obs.observe(ra, { attributes:true });
        ra.__footerObs__ = obs;
      }catch(e){}
    }
  }

  function init(){ render(); bind(); }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();
