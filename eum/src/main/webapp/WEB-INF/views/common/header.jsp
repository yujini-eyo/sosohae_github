<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:set var="userDisplayName"
       value="${empty sessionScope.member ? '' : (empty sessionScope.member.name ? (empty sessionScope.member.nickname ? sessionScope.member.id : sessionScope.member.nickname) : sessionScope.member.name)}" />

<header id="header" class="site-header" data-eum-header role="banner" aria-label="상단 헤더">
  <div class="eum-container">
    <div class="eum-header-row">
      <!-- 좌측: 햄버거 + 상단 내비 -->
      <div class="eum-left">
        <button id="hamburger" class="eum-hamburger" type="button"
                aria-label="메뉴 열기" aria-controls="drawer" aria-expanded="false">☰</button>

        <!-- 비어있어도 JS가 자동 보충 -->
        <nav id="leftArea" class="eum-topnav" aria-label="서비스 메뉴">
          <a href="<c:url value='/about.do'/>">서비스 소개</a>
          <a href="<c:url value='/guide.do'/>">이용방법</a>
          <a href="<c:url value='/notice.do'/>">공지</a>
        </nav>
      </div>

      <!-- 가운데: 로고 -->
      <h1 class="eum-logo">
        <a href="<c:url value='/main.do'/>" title="메인으로">EuM:</a>
      </h1>

      <!-- 우측: 사용자 영역 (JS 렌더) -->
      <div id="rightArea"
           class="eum-right"
           aria-label="사용자 영역"
           data-ctx="${ctx}"
           data-auth="${not empty sessionScope.member}"
           data-username="<c:out value='${userDisplayName}'/>"
           data-mypage="/member/mypage.do"     <%-- 사용자명 클릭 → 마이페이지 --%>
           data-point="/point.do"              <%-- 포인트 --%>
           data-notify="/notify.do"            <%-- 알림 --%>
           data-logout="/member/logout.do"     <%-- ★ 로그아웃 (멤버 서버 매핑에 맞춤) --%>
           data-login="/member/loginForm.do"
           data-signup="/member/signupForm.do">
        <%-- JS가 렌더링합니다. --%>
      </div>
    </div>
  </div>
</header>

<!-- 드로어 & 백드롭 -->
<div id="drawerBackdrop" class="eum-drawer-backdrop" hidden></div>

<aside id="drawer" class="eum-drawer" aria-label="모바일 메뉴" aria-hidden="true">
  <header class="eum-drawer-header">
    <strong>메뉴</strong>
    <button id="drawerClose" class="icon-btn" type="button" aria-label="닫기">✕</button>
  </header>

  <nav id="drawerMenu" class="drawer-links" aria-label="사이드 내비게이션" aria-live="polite" aria-busy="false"></nav>

  <noscript>
    <nav class="drawer-links" aria-label="사이드 내비게이션">
      <a href="<c:url value='/main.do'/>">홈</a>
      <a href="<c:url value='/about.do'/>">서비스 소개</a>
      <a href="<c:url value='/guide.do'/>">이용방법</a>
      <a href="<c:url value='/notice.do'/>">공지</a>
      <a href="<c:url value='/board/listArticles.do'/>">도움 주기</a>
      <a href="<c:url value='/help/write.do'/>">도움 요청하기</a>
      <a href="<c:url value='/chat.do'/>">채팅</a>
    </nav>
  </noscript>
</aside>
