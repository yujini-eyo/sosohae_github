/* footer.pad.js — 모바일 탭바 높이만큼 본문 하단 패딩 + 현재 탭 하이라이트 (구형 브라우저/지연 렌더 대응) */
(function(){
  "use strict";
  if (window.__EUM_FOOTER_PAD_INITED__) return;
  window.__EUM_FOOTER_PAD_INITED__ = true;

  var $  = function (s, r) { return (r || document).querySelector(s); };
  var $$ = function (s, r) { return Array.prototype.slice.call((r || document).querySelectorAll(s)); };

  /* rAF polyfill (IE/구형 브라우저) */
  var _rAF = window.requestAnimationFrame || function(cb){ return setTimeout(cb, 16); };
  var _cAF = window.cancelAnimationFrame || clearTimeout;

  function stripCtx(path) {
    try {
      var p = new URL(path, location.origin).pathname;
      var ra = document.querySelector("#rightArea");
      var ctx = (ra && ra.dataset && ra.dataset.ctx) ? ra.dataset.ctx : "";
      if (ctx && p.indexOf(ctx) === 0) p = p.slice(ctx.length) || "/";
      if (p.length > 1 && /\/$/.test(p)) p = p.slice(0, -1);
      return p || "/";
    } catch(e){ return path || "/"; }
  }

  function isMobile(){
    try { return window.matchMedia && window.matchMedia("(max-width: 900px)").matches; }
    catch(e){ return (document.documentElement.clientWidth || window.innerWidth || 0) <= 900; }
  }

  function hasBar(){ return $(".mobile-tabbar"); }

  function highlightMobileTab() {
    var bar = hasBar();
    if (!bar) return;
    var cur = stripCtx(location.pathname);
    $$(".mobile-tabbar .tab", bar).forEach(function (a) {
      try {
        var p = stripCtx(a.pathname || a.getAttribute("href") || "/");
        var on = (p === cur) || (p !== "/" && cur.indexOf(p) === 0);
        if (on) { a.classList.add("active"); a.setAttribute("aria-current","page"); }
        else    { a.classList.remove("active"); a.removeAttribute("aria-current"); }
      } catch (e) {}
    });
  }

  var lastPB = null, rafId = 0;
  function applyBodyBottomPadding() {
    var bar = hasBar();
    if (!bar) { if (lastPB !== 0){ lastPB = 0; document.body.style.paddingBottom = ""; } return; }

    var show = isMobile();
    var cs = window.getComputedStyle ? getComputedStyle(bar) : null;
    if (cs && cs.display === "none") show = false;

    var h = show ? Math.ceil(bar.getBoundingClientRect().height || 0) : 0;
    if (lastPB === h) return;
    lastPB = h;
    document.body.style.paddingBottom = h ? (h + "px") : "";
  }

  function refresh(){ highlightMobileTab(); applyBodyBottomPadding(); }

  ["resize","orientationchange","hashchange","popstate","visibilitychange"].forEach(function(ev){
    window.addEventListener(ev, function(){
      if (rafId) _cAF(rafId);
      rafId = _rAF(function(){ rafId = 0; refresh(); });
    });
  });

  /* 탭바가 나중에 DOM에 추가되는 경우 대비 */
  function watchInsert(){
    if (hasBar()) { refresh(); return; }
    if (!("MutationObserver" in window)) return; // 구형이면 그냥 패스
    try{
      var obs = new MutationObserver(function(muts){
        for (var i=0;i<muts.length;i++){
          var m = muts[i];
          if (!m.addedNodes) continue;
          for (var j=0;j<m.addedNodes.length;j++){
            var n = m.addedNodes[j];
            if (n && n.nodeType === 1) {
              if (n.classList && n.classList.contains("mobile-tabbar")) { obs.disconnect(); refresh(); return; }
              // 자식에 포함되어 추가되는 경우도 탐지
              if (n.querySelector && n.querySelector(".mobile-tabbar")) { obs.disconnect(); refresh(); return; }
            }
          }
        }
      });
      obs.observe(document.body || document.documentElement, { childList:true, subtree:true });
    }catch(e){}
  }

  function init(){
    refresh();
    watchInsert();
  }

  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();
