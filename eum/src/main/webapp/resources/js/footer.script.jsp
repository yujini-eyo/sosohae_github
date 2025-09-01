<%@ page language="java" contentType="application/javascript; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="CTX" value="${pageContext.request.contextPath}" />

(function(){
  "use strict";

  var CTX = "<c:out value='${CTX}'/>";

  function $(sel, root){ return (root||document).querySelector(sel); }
  function $all(sel, root){ return (root||document).querySelectorAll(sel); }
  function stripCtx(path){
    if (!path) return "/";
    return path.indexOf(CTX) === 0 ? (path.slice(CTX.length) || "/") : path;
  }
  function isMobile(){
    return window.matchMedia("(max-width: 900px)").matches;
  }

  // 모바일 탭바 활성 탭 강조
  function highlightMobileTab(){
    var bar = $(".mobile-tabbar");
    if (!bar) return;
    var cur = stripCtx(location.pathname);
    $all(".mobile-tabbar .tab").forEach(function(a){
      try{
        var p = stripCtx(a.pathname);
        if (p === cur || (p !== "/" && cur.indexOf(p) === 0)) a.classList.add("active");
        else a.classList.remove("active");
      }catch(e){}
    });
  }

  // 탭바가 보일 때 본문이 가려지지 않도록 padding-bottom 적용
  var lastPB = null, rafId = null;
  function applyBodyBottomPadding(){
    var bar = $(".mobile-tabbar");
    if (!bar) return;
    var show = isMobile();
    var pb = show ? (bar.getBoundingClientRect().height + Math.max(8, (window.visualViewport ? 0 : 0))) : 0;
    if (pb !== lastPB){
      document.body.style.paddingBottom = (pb ? (pb + "px") : "");
      lastPB = pb;
    }
  }

  // 외부 새창 링크 보안 속성 보강
  function hardenExternal(){
    $all('a[target="_blank"]').forEach(function(a){
      if (!/noopener|noreferrer/.test(a.rel)) a.rel = (a.rel ? a.rel + " " : "") + "noopener noreferrer";
    });
  }

  function init(){
    highlightMobileTab();
    applyBodyBottomPadding();
    hardenExternal();
  }

  if (document.readyState === "loading"){
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }

  window.addEventListener("resize", function(){
    if (rafId) cancelAnimationFrame(rafId);
    rafId = requestAnimationFrame(function(){
      highlightMobileTab();
      applyBodyBottomPadding();
    });
  });

})();
