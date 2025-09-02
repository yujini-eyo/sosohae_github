/* footer.script.js — 모바일 탭바 하이라이트 + 패딩 보정 */
(function () {
  "use strict";

  // CTX: footer.pad.js와 공유할 수 있도록 1회 계산
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
  var CTX = window.EUM_CTX || getCtxFromScript("footer.script.js");

  var $ = function (s, r) { return (r || document).querySelector(s); };
  var $$ = function (s, r) { return (r || document).querySelectorAll(s); };

  function isMobile() { return window.matchMedia("(max-width: 900px)").matches; }
  function stripCtx(path) {
    if (!path) return "/";
    var c = window.EUM_CTX || "";
    return path.indexOf(c) === 0 ? (path.slice(c.length) || "/") : path;
  }

  // 모바일 탭바 활성 탭 표시
  function highlightMobileTab() {
    var bar = $(".mobile-tabbar");
    if (!bar) return;
    var cur = stripCtx(location.pathname);
    $$(".mobile-tabbar .tab").forEach(function (a) {
      try {
        var p = stripCtx(a.pathname);
        if (p === cur || (p !== "/" && cur.indexOf(p) === 0)) a.classList.add("active");
        else a.classList.remove("active");
      } catch (e) {}
    });
  }

  // 탭바 높이만큼 본문 하단 패딩
  var lastPB = null, rafId = 0;
  function applyBodyBottomPadding() {
    var bar = $(".mobile-tabbar");
    if (!bar) return;
    var show = isMobile() && getComputedStyle(bar).display !== "none";
    var pb = show ? Math.ceil(bar.getBoundingClientRect().height) : 0;
    if (pb !== lastPB) {
      document.body.style.paddingBottom = pb ? (pb + "px") : "";
      lastPB = pb;
    }
  }

  // 외부 링크 보안 보강
  function hardenExternal() {
    $$('.site-footer a[target="_blank"]').forEach(function (a) {
      if (!/noopener|noreferrer/.test(a.rel)) a.rel = (a.rel ? a.rel + " " : "") + "noopener noreferrer";
    });
  }

  function init() {
    highlightMobileTab();
    applyBodyBottomPadding();
    hardenExternal();
  }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();

  window.addEventListener("resize", function () {
    if (rafId) cancelAnimationFrame(rafId);
    rafId = requestAnimationFrame(function () {
      highlightMobileTab();
      applyBodyBottomPadding();
    });
  });
})();
