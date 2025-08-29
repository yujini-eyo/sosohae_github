<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>" />

<%
  // 필요 시 요청 인코딩 지정 (GET인 경우 의미 없을 수 있음)
  request.setCharacterEncoding("UTF-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- JS가 기대하는 DOM 구조/ID를 그대로 유지합니다. -->
<header class="site-header" role="banner">
  <div class="container">
    <div class="header-container">
      <!-- 좌측: 햄버거 -->
      <div class="header-left">
        <button id="hamburger"
                class="hamburger"
                type="button"
                aria-label="메뉴 열기"
                aria-expanded="false"
                aria-controls="drawer">
          &#9776;
        </button>
      </div>

      <!-- 가운데: 로고 -->
      <h1 class="logo">
        <a href="<c:url value='/main.do'/>" title="홈으로">소소한 도움</a>
      </h1>

      <!-- 우측: 사용자 영역(로그인/로그아웃/포인트 등은 JS가 렌더링) -->
      <div id="rightArea" class="header-right" aria-label="사용자 영역"></div>
    </div>

    <!-- 상단 네비게이션(데스크탑에서 노출) -->
    <nav id="topnav" class="topnav" aria-label="상단 메뉴">
      <a href="<c:url value='/main.do'/>">홈</a>
      <a href="<c:url value='/about.do'/>">서비스 소개</a>
      <a href="<c:url value='/guide.do'/>">이용방법</a>
      <a href="<c:url value='/notice.do'/>">공지</a>
      <a href="<c:url value='/give.do'/>">도움 주기</a>
      <a href="<c:url value='/write.do'/>">도움 요청하기</a>
      <a href="<c:url value='/chat.do'/>">채팅</a>
    </nav>
  </div>
</header>

<!-- 드로어(모바일 메뉴) & 백드롭 -->
<div id="drawerBackdrop" class="drawer-backdrop" hidden></div>
<aside id="drawer" class="drawer" aria-hidden="true" aria-label="모바일 메뉴">
  <header class="drawer-header">
    <strong>메뉴</strong>
    <button id="drawerClose" class="icon-btn" type="button" aria-label="닫기">✕</button>
  </header>
  <nav aria-label="드로어 내비게이션"><!-- header.script.jsp 또는 header.js가 항목을 렌더링 --></nav>
</aside>
