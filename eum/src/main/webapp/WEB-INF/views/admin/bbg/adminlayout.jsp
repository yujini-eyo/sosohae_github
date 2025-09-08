<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1"/>
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <%-- Tiles 속성을 요청 범위로 가져와 기본값 처리 --%>
  <tiles:importAttribute name="title" ignore="true"/>
  <tiles:importAttribute name="body_class" ignore="true"/>

  <title><c:out value="${title}" default="관리자"/></title>

  <!-- 공통 레이아웃 CSS -->
  <link rel="stylesheet" href="${ctx}/resources/css/admin/board/layout.css"/>

  <!-- 하위 뷰에서 head에 뭐 넣고 싶을 때 -->
  <tiles:insertAttribute name="extra_head" ignore="true"/>

  <!-- 로그인 등 특정 페이지에서만 상단 글씨를 가리기 위한 간단 CSS -->
  <style>
    .page-top-occluder{display:none}
    .page-auth .page-top-occluder{
      display:block;
      position:fixed; left:0; top:0; width:100%; height:140px;
      background:
        radial-gradient(1200px 240px at 50% -60px, #FFE8C2 0%, rgba(255,232,194,.55) 35%, rgba(255,232,194,0) 60%),
        linear-gradient(180deg, #FFF8F2 0%, rgba(255,248,242,0) 100%);
      z-index:999; pointer-events:none;
    }
  </style>

  <%-- (필요 시) CSRF 메타
  <c:if test="${not empty _csrf}">
    <meta name="_csrf_parameter" content="${_csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta name="_csrf" content="${_csrf.token}"/>
  </c:if>
  --%>
</head>
<body class="${body_class}">
  <!-- 필요 페이지에서만 보이는 상단 가림막 (Tiles에서 body_class=page-auth 줄 때) -->
  <div class="page-top-occluder" aria-hidden="true"></div>

  <header class="ad-header">
    <div class="container">
      <!-- 기존 header 조각을 그대로 사용 (없으면 무시) -->
      <tiles:insertAttribute name="header" ignore="true"/>

      <!-- 전역 네비게이션 (관리자 전용 링크) -->
      <nav class="nav" aria-label="관리자 전역 메뉴">
        <a href="${ctx}/admin/board/listArticles.do">게시글 목록</a>
        <a href="${ctx}/admin/board/writeForm.do">글 작성</a>

        <span class="nav-spacer"></span>

        <c:choose>
          <c:when test="${sessionScope.userRole eq 'ADMIN' || (sessionScope.member ne null && sessionScope.member.role eq 'ADMIN')}">
            <span class="nav-user">
              관리자: <strong>${sessionScope.userId != null ? sessionScope.userId : (sessionScope.member != null ? sessionScope.member.id : 'admin')}</strong>
            </span>
            <a href="${ctx}/admin/logout.do" class="logout">로그아웃</a>
          </c:when>
          <c:otherwise>
            <a href="${ctx}/admin/login.do" class="login">관리자 로그인</a>
          </c:otherwise>
        </c:choose>
      </nav>
    </div>
  </header>

  <main class="ad-main" role="main">
    <div class="container">
      <tiles:insertAttribute name="body"/>
    </div>
  </main>

  <footer class="ad-footer">
    <div class="container">
      <tiles:insertAttribute name="footer" ignore="true"/>
    </div>
  </footer>

  <!-- 하위 뷰에서 body 끝에 스크립트를 꽂고 싶을 때 -->
  <tiles:insertAttribute name="extra_script" ignore="true"/>
</body>
</html>
