/* header.patch.script.js — 가벼운 반응형/패치 전용 */
(function () {
  "use strict";

  function $(s, r) { return (r || document).querySelector(s); }

  function applyResponsive() {
    var right = $("#rightArea");
    if (!right) return;
    var w = window.innerWidth || document.documentElement.clientWidth;
    if (w <= 900) right.classList.add("is-compact");
    else right.classList.remove("is-compact");
  }

  function init() { applyResponsive(); }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();

  window.addEventListener("resize", applyResponsive);
})();
