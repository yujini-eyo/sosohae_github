/* footer.js - footer 동적 렌더 */
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

      const siteName = opts.siteName || "소소한 도움";
      const tagline = opts.tagline || "";
      const links = Array.isArray(opts.links) ? opts.links : [];
      const year = opts.year || new Date().getFullYear();

      const footer = document.createElement("footer");
      footer.className = "site-footer";
      footer.setAttribute("role", "contentinfo");

      const navHTML = links.length
        ? `<nav class="footer-nav" aria-label="푸터 링크">
            ${links
              .map(
                (l) =>
                  `<a class="footer-link" href="${(l && l.href) || "#"}">${
                    (l && l.text) || ""
                  }</a>`
              )
              .join("")}
           </nav>`
        : "";

      footer.innerHTML = `
        <div class="footer-inner">
          ${navHTML}
          <div class="footer-copy">&copy; ${year} ${siteName}${
        tagline ? ` | ${tagline}` : ""
      }</div>
        </div>
      `;

      // 기존 mount 내용 비우고 푸터 삽입
      mount.innerHTML = "";
      mount.appendChild(footer);
    } catch (e) {
      console.error("renderFooter error:", e);
    }
  }

  // 전역 노출
  global.renderFooter = renderFooter;
})(window);
