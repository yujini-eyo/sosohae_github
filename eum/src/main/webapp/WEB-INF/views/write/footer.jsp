<%@ page contentType="text/html; charset=UTF-8"%>
<div id="footer-root"></div>
<script>
  if (typeof renderFooter === 'function') {
    renderFooter({ mount: '#footer-root', siteName: '소소한 도움', tagline: '따뜻한 연결, 함께 만드는 우리 동네' });
  }
</script>
