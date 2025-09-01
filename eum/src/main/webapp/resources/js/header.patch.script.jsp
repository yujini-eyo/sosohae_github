<%@ page language="java" pageEncoding="UTF-8" %>
<%-- 선택: 환경 맞춤 패치/오버라이드 전용. 필요 없으면 내용 비워도 됩니다. --%>
(function(){
  "use strict";

  // 예) 900px 이하에서 헤더 일부 숨김
  function applyResponsive(){
    var w = window.innerWidth || document.documentElement.clientWidth;
    var right = document.querySelector(".header-right");
    if (!right) return;
    if (w <= 900) {
      right.classList.add("is-compact");
    } else {
      right.classList.remove("is-compact");
    }
  }

  window.addEventListener("resize", applyResponsive);
  document.addEventListener("DOMContentLoaded", applyResponsive);
})();
