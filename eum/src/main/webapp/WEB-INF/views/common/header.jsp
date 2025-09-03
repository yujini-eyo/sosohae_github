<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- header.css / header.script.js / header.patch.script.js 는 layout.jsp 에서 include 권장 -->
<header class="site-header" role="banner" aria-label="상단 헤더">
  <div class="container">
    <div class="header-container">
      <!-- 좌측: 햄버거 + 상단 내비 -->
      <div class="header-left">
        <button
          id="hamburger"
          class="hamburger"
          type="button"
          aria-label="메뉴 열기"
          aria-controls="drawer"
          aria-expanded="false">☰</button>

        <nav id="leftArea" class="topnav" aria-label="서비스 메뉴">
          <a href="<c:url value='/about.do'/>">서비스 소개</a>
          <a href="<c:url value='/guide.do'/>">이용방법</a>
          <a href="<c:url value='/notice.do'/>">공지</a>
        </nav>
      </div>

      <!-- 가운데: 로고 -->
      <h1 class="logo">
        <a href="<c:url value='/main.do'/>" title="메인으로">EuM:</a>
      </h1>

      <!-- 우측: 사용자 영역 (JS가 버튼 렌더링) -->
      <c:set var="userDisplayName"
             value="${empty sessionScope.member ? '' : (empty sessionScope.member.name ? (empty sessionScope.member.nickname ? sessionScope.member.id : sessionScope.member.nickname) : sessionScope.member.name)}" />
      <div
        id="rightArea"
        class="header-right"
        aria-label="사용자 영역"
        data-auth="${not empty sessionScope.member}"
        data-username="<c:out value='${userDisplayName}'/>"
        data-ctx="${ctx}">
        <%-- JS가 렌더링하므로 초기 내용은 비워둡니다. --%>
      </div>
    </div>
  </div>
</header>

<!-- 드로어 & 백드롭(모바일 메뉴) -->
<div id="drawerBackdrop" class="drawer-backdrop" hidden></div>

<aside id="drawer" class="drawer" aria-label="모바일 메뉴" aria-hidden="true">
  <header>
    <strong>메뉴</strong>
    <button id="drawerClose" class="icon-btn" type="button" aria-label="닫기">✕</button>
  </header>

  <nav aria-label="드로어 내비게이션">
    <a href="<c:url value='/main.do'/>">홈</a>
    <a href="<c:url value='/about.do'/>">서비스 소개</a>
    <a href="<c:url value='/guide.do'/>">이용방법</a>
    <a href="<c:url value='/notice.do'/>">공지</a>
    <a href="<c:url value='/board/listArticles.do'/>">도움 주기</a>
    <a href="<c:url value='/help/write.do'/>">도움 요청하기</a>
    <a href="<c:url value='/chat.do'/>">채팅</a>
  </nav>
</aside>
