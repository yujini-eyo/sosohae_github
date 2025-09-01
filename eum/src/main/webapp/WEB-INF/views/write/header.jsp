<%@ page contentType="text/html; charset=UTF-8"%>
<div class="container">
  <div class="header-container">
    <div class="header-left">
      <button class="hamburger" id="hamburger" aria-label="메뉴 열기">☰</button>
      <nav class="topnav" id="topnav" aria-label="상단 내비게이션">
        <a href="${pageContext.request.contextPath}/about.do">서비스 소개</a>
        <a href="${pageContext.request.contextPath}/guide.do">이용방법</a>
        <a href="${pageContext.request.contextPath}/notice.do">공지</a>
      </nav>
    </div>
    <div class="logo"><a href="${pageContext.request.contextPath}/">소소한 도움</a></div>
    <div class="header-right" id="rightArea">
      <span class="user-name">홍길동 님</span>
      <button class="icon-btn" id="notifyBtn" aria-label="알림">🔔</button>
      <button class="info-btn" onclick="location.href='${pageContext.request.contextPath}/mypage.do'">내정보</button>
      <button class="auth-btn btn-ghost" onclick="alert('로그아웃 처리')">로그아웃</button>
    </div>
  </div>
</div>
