<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1"/>
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <%-- Tiles 속성 기본값 --%>
  <tiles:importAttribute name="title" ignore="true"/>
  <tiles:importAttribute name="body_class" ignore="true"/>

  <title><c:out value="${empty title ? '관리자' : title}"/></title>

  <%-- 관리자 고정 헤더 스타일/레이아웃 --%>
  <link rel="stylesheet" href="${ctx}/resources/css/admin/admin_header.css"/>
  <link rel="stylesheet" href="${ctx}/resources/css/admin/board/layout.css"/>

  <%-- 하위 뷰 head 삽입 포인트 --%>
  <tiles:insertAttribute name="extra_head" ignore="true"/>

  <%-- (선택) 인증 화면 상단 가림막 --%>
  <style>
    .page-top-occluder{display:none}
    .page-auth .page-top-occluder{
      display:block; position:fixed; left:0; top:0; width:100%; height:140px;
      background:
        radial-gradient(1200px 240px at 50% -60px, #FFE8C2 0%, rgba(255,232,194,.55) 35%, rgba(255,232,194,0) 60%),
        linear-gradient(180deg, #FFF8F2 0%, rgba(255,248,242,0) 100%);
      z-index:999; pointer-events:none;
    }
  </style>
</head>

<%
  // 세션 로그인 이름/시간 계산 (JSP 스크립틀릿 사용 피하고 싶으면 EL로만도 가능)
%>
<body class="${body_class}">
  <div class="page-top-occluder" aria-hidden="true"></div>

  <%-- ===== 관리자 고정 헤더 ===== --%>
  <%
    // JSTL로 계산하지만, 가독성을 위해 미리 생각: 아래에서는 EL/JSTL만 사용
  %>
  <c:choose>
    <c:when test="${not empty sessionScope.member}">
      <c:set var="adminDisplayName"
             value="${empty sessionScope.member.name
                      ? (empty sessionScope.member.nickname
                          ? sessionScope.member.id
                          : sessionScope.member.nickname)
                      : sessionScope.member.name}"/>
    </c:when>
    <c:otherwise>
      <c:set var="adminDisplayName" value="${empty sessionScope.userId ? '관리자' : sessionScope.userId}"/>
    </c:otherwise>
  </c:choose>

  <c:set var="loginMillis"
         value="${sessionScope.loginTime ne null ? sessionScope.loginTime.time : session.creationTime}" />

  <header class="admin-header" role="banner" aria-label="관리자 상단 헤더">
    <div class="admin-header__inner">
      <!-- 좌측: 사이드바 토글 -->
      <button id="adminSidebarToggle"
              class="admin-btn-icon"
              type="button"
              aria-label="사이드바 토글"
              aria-pressed="false">☰</button>

      <!-- 가운데: 브랜드(관리자 메인으로) -->
      <a class="admin-brand" href="${ctx}/admin/main.do" title="관리자 메인으로">EuM-admin</a>

      <!-- 우측: 회원정보/접속시간/로그아웃 -->
      <div id="adminMeta"
           class="admin-meta"
           data-ctx="${ctx}"
           data-username="<c:out value='${adminDisplayName}'/>"
           data-login-at="<c:out value='${loginMillis}'/>">
        <span class="admin-user">
          <a class="admin-link-name" href="${ctx}/member/mypage.do" title="회원 정보 보기">
            <c:out value="${adminDisplayName}"/>
          </a>
        </span>
        <span class="admin-dot">•</span>
        <span class="admin-clock" id="adminClock">접속 —:—:—</span>
        <span class="admin-dot">•</span>
        <c:choose>
          <c:when test="${sessionScope.userRole eq 'ADMIN' || (sessionScope.member ne null && sessionScope.member.role eq 'ADMIN')}">
            <a class="admin-btn admin-btn-ghost" href="${ctx}/admin/logout.do">로그아웃</a>
          </c:when>
          <c:otherwise>
            <a class="admin-btn" href="${ctx}/admin/login.do">관리자 로그인</a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </header>

  <%-- ===== 본문 ===== --%>
  <main class="ad-main" role="main">
    <div class="container">
      <tiles:insertAttribute name="body"/>
    </div>
  </main>

  <%-- ===== 푸터 ===== --%>
  <footer class="ad-footer">
    <div class="container">
      <tiles:insertAttribute name="footer" ignore="true"/>
    </div>
  </footer>

  <%-- 헤더 스크립트(고정헤더 여백/토글/접속시간 표기) --%>
  <script defer src="${ctx}/resources/js/admin/admin_header.js"></script>

</body>
</html>
