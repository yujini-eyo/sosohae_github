/* footer.pad.js — 추가 패딩/플랫폼 이슈 대응(옵션) */
(function () {
  "use strict";

  // iOS 소프트 키보드/뷰포트 변화 시 하단 패딩 재계산 보조
  var ticking = false;
  function onViewportChange() {
    if (ticking) return;
    ticking = true;
    requestAnimationFrame(function () {
      try {
        var bar = document.querySelector(".mobile-tabbar");
        if (!bar) return;
        if (window.matchMedia("(max-width: 900px)").matches &&
            getComputedStyle(bar).display !== "none") {
          var pb = Math.ceil(bar.getBoundingClientRect().height);
          document.body.style.paddingBottom = pb ? (pb + "px") : "";
        }
      } finally { ticking = false; }
    });
  }

  // visualViewport 지원 시 활용
  if (window.visualViewport) {
    visualViewport.addEventListener("resize", onViewportChange);
    visualViewport.addEventListener("scroll", onViewportChange);
  }
})();
