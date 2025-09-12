<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- 관리자 표시용 이름 계산 (member가 있으면 name/nickname/id 우선순위) --%>
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
    <c:set var="adminDisplayName" value="관리자"/>
  </c:otherwise>
</c:choose>

<%-- 접속(로그인) 시각: 세션에 loginTime(Date)이 있으면 사용, 없으면 세션 생성 시각 --%>
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

    <!-- 가운데: 브랜드 (클릭 → 관리자 메인) -->
    <a class="admin-brand"
       href="${ctx}/admin/main.do"
       title="관리자 메인으로">EuM-admin</a>


    <!-- 우측 메타: 회원정보, 접속시간, 로그아웃 -->
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
      <a class="admin-btn admin-btn-ghost" href="${ctx}/admin/logout.do">로그아웃</a>
    </div>
  </div>
</header>
