<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 헤더 -->
<header class="site-header" role="banner">
  <div class="container">
    <div class="header-container">
      <!-- 좌측: 햄버거 + 상단 네비 -->
      <div class="header-left">
        <button id="hamburger"
                class="hamburger"
                type="button"
                aria-label="메뉴 열기"
                aria-expanded="false"
                aria-controls="drawer">☰</button>

        <nav id="leftArea" class="topnav" aria-label="서비스">
          <a href="<c:url value='/about.do'/>">서비스 소개</a>
          <a href="<c:url value='/guide.do'/>">이용방법</a>
          <a href="<c:url value='/notice.do'/>">공지</a>
        </nav>
      </div>

      <!-- 가운데: 로고 -->
      <h1 class="logo">
        <a href="<c:url value='/main.do'/>" title="main">EuM:</a>
      </h1>

      <!-- 우측: 사용자 영역 (JS가 로그인/로그아웃 버튼 렌더링) -->
      <div id="rightArea" class="header-right" aria-label="사용자 영역"></div>
    </div>
  </div>
</header>

<!-- 드로어(모바일 메뉴) & 백드롭 -->
<div id="drawerBackdrop" class="drawer-backdrop" hidden></div>
<aside id="drawer" class="drawer" aria-hidden="true" aria-label="모바일 메뉴">
  <header class="drawer-header">
    <strong>메뉴</strong>
    <button id="drawerClose" class="icon-btn" type="button" aria-label="닫기">✕</button>
  </header>

  <nav aria-label="드로어 내비게이션">
    <a href="<c:url value='/main.do'/>">홈</a>
    <a href="<c:url value='/about.do'/>">서비스 소개</a>
    <a href="<c:url value='/guide.do'/>">이용방법</a>
    <a href="<c:url value='/notice.do'/>">공지</a>
    <a href="<c:url value='/give.do'/>">도움 주기</a>
    <a href="<c:url value='/write.do'/>">도움 요청하기</a>
    <a href="<c:url value='/chat.do'/>">채팅</a>
  </nav>
</aside>
