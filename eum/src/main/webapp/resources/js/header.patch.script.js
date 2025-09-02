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
