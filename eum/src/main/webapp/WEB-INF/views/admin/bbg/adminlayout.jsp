<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1"/>
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <title><tiles:getAsString name="title"/></title>

  <!-- 공통 레이아웃 CSS -->
  <link rel="stylesheet" href="${ctx}/resources/css/admin/layout.css"/>

  <!-- 하위 뷰에서 head에 뭐 넣고 싶을 때 -->
  <tiles:insertAttribute name="extra_head" ignore="true"/>

  <!-- (필요 시) CSRF 메타
  <c:if test="${not empty _csrf}">
    <meta name="_csrf_parameter" content="${_csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta name="_csrf" content="${_csrf.token}"/>
  </c:if>
  -->
</head>
<body>
  <header class="ad-header">
    <div class="container">
      <!-- 기존 header 조각을 그대로 사용 -->
      <tiles:insertAttribute name="header"/>

      <!-- 전역 네비게이션 (관리자 전용 링크) -->
      <nav class="nav">
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

  <main class="ad-main">
    <div class="container">
      <tiles:insertAttribute name="body"/>
    </div>
  </main>

  <footer class="ad-footer">
    <div class="container">
      <tiles:insertAttribute name="footer"/>
    </div>
  </footer>

  <!-- 하위 뷰에서 body 끝에 스크립트를 꽂고 싶을 때 -->
  <tiles:insertAttribute name="extra_script" ignore="true"/>
</body>
</html>
