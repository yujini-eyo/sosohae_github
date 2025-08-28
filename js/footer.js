/* footer.js - footer 동적 렌더 (+모바일 하단 탭바) */
(function (global) {
  function renderFooter(options) {
    try {
      const opts = options || {};
      const mount =
        typeof opts.mount === "string"
          ? document.querySelector(opts.mount)
          : (opts.mount || document.body);

      if (!mount) {
        console.warn("renderFooter: mount element not found.");
        return;
      }

      const siteName = opts.siteName || "EuM:";
      const tagline = opts.tagline || "";
      const links = Array.isArray(opts.links) ? opts.links : [];
      const year = opts.year || new Date().getFullYear();

      // (선택) 기존 mount 내용을 비울지 여부 — 기본 true (기존 동작 유지)
      const replace = opts.replace !== false;

      const footer = document.createElement("footer");
      footer.className = "site-footer";
      footer.setAttribute("role", "contentinfo");

      const navHTML = links.length
        ? `<nav class="footer-nav" aria-label="푸터 링크">
            ${links.map((l) =>
              `<a class="footer-link" href="${(l && l.href) || "#"}">${
                (l && l.text) || ""
              }</a>`
            ).join("")}
           </nav>`
        : "";

      footer.innerHTML = `
        <div class="footer-inner">
          ${navHTML}
          <div class="footer-copy">&copy; ${year} ${siteName}${tagline ? ` | ${tagline}` : ""}</div>
        </div>
      `;

      if (replace) mount.innerHTML = "";
      mount.appendChild(footer);

      // ====== 모바일 하단 탭바 (옵션) ======
      // 사용: mobileTabbar: true, mobileLinks: [{id,text,href}]
      if (opts.mobileTabbar !== false) {
        const defaultMobileLinks = [
          { id: "write", text: "요청하기",  href: "/write.html" },
          { id: "give",    text: "도움주기", href: "/give.html" },
          { id: "chat",    text: "채팅",     href: "/chat.html" },
          { id: "mypage",      text: "내정보",   href: "/mypage.html" },
        ];
        const mLinks = Array.isArray(opts.mobileLinks) && opts.mobileLinks.length
          ? opts.mobileLinks
          : defaultMobileLinks;

        const tabbar = document.createElement("nav");
        tabbar.className = "mobile-tabbar";
        tabbar.setAttribute("aria-label", "하단 빠른메뉴");
        tabbar.innerHTML = mLinks.map((l) =>
          `<a class="tabbar-item" href="${l.href}" data-id="${l.id}"><span class="tabbar-label">${l.text}</span></a>`
        ).join("");

        // 바디에 붙여 고정 (mount와 무관하게 항상 최하단 고정)
        document.body.appendChild(tabbar);

        // 활성 상태 표시
        const setActive = () => {
          const path = location.pathname + location.hash;
          tabbar.querySelectorAll(".tabbar-item").forEach(a => {
            const href = a.getAttribute("href") || "";
            const matchHash = href.startsWith("#") && location.hash === href;
            const matchPath = !href.startsWith("#") && path.startsWith(href);
            a.classList.toggle("is-active", matchHash || matchPath);
          });
        };
        setActive();
        window.addEventListener("hashchange", setActive);
        window.addEventListener("popstate", setActive);
        tabbar.addEventListener("click", (e) => {
          const a = e.target.closest("a.tabbar-item");
          if (!a) return;
          tabbar.querySelectorAll(".tabbar-item").forEach(el => el.classList.remove("is-active"));
          a.classList.add("is-active");
        });

        // 반응형 표시/숨김 (600px 기준)
        const mql = window.matchMedia("(max-width: 600px)");
        const apply = () => {
          if (mql.matches) {
            tabbar.style.display = "flex";
            document.body.classList.add("has-tabbar");
          } else {
            tabbar.style.display = "none";
            document.body.classList.remove("has-tabbar");
          }
        };
        apply();
        mql.addEventListener ? mql.addEventListener("change", apply) : mql.addListener(apply);

        // 전역 접근자 (선택)
        global.Footer = Object.assign(global.Footer || {}, {
          setActive: setActive
        });
      }
    } catch (e) {
      console.error("renderFooter error:", e);
    }
  }

  global.renderFooter = renderFooter;
})(window);
