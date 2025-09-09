<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 관리자 세션 없으면 로그인으로 리다이렉트 -->
<c:if test="${empty sessionScope.adminUser}">
	<c:url var="loginUrl" value="/admin/auth/login.do" />
	<c:redirect url="${loginUrl}" />
</c:if>

<section class="admin-dashboard"
	style="max-width: 1040px; margin: 0 auto; padding: 24px 16px;">
	<header
		style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
		<div>
			<h1 style="margin: 0; font-size: 1.6rem;">관리자 메인</h1>
			<p style="margin: 6px 0 0; color: #806A5A;">
				<strong>
          <c:choose>
            <c:when test="${not empty sessionScope.adminUser.name}">
              <c:out value="${sessionScope.adminUser.name}" />
            </c:when>
            <c:when test="${not empty sessionScope.adminUser.id}">
              <c:out value="${sessionScope.adminUser.id}" />
            </c:when>
            <c:otherwise>관리자</c:otherwise>
          </c:choose>
        </strong>님, 환영합니다.
			</p>
		</div>
		<div>
			<a href="<c:url value='/admin/logout.do'/>"
				style="display: inline-block; padding: 10px 14px; border: 1px solid #ddd; border-radius: 10px; background: #fff; color: #59463E; text-decoration: none;">
				로그아웃 </a>
		</div>
	</header>

	<div
		style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 14px;">
		<!-- 게시판 관리 -->
		<a href="<c:url value='/admin/board/listArticles.do'/>"
			style="display: block; border: 1px solid #FFE1CB; border-radius: 16px; background: #fff; padding: 16px; text-decoration: none; color: #59463E; box-shadow: 0 4px 10px rgba(0, 0, 0, .06);">
			<h3 style="margin: 0 0 8px;">게시판 관리</h3>
			<p style="margin: 0; color: #806A5A;">글 목록/등록/수정/삭제</p>
		</a>

		<!-- 회원 조회 -->
		<a href="<c:url value='/admin/auth/listMembers.do'/>"
			style="display: block; border: 1px solid #FFE1CB; border-radius: 16px; background: #fff; padding: 16px; text-decoration: none; color: #59463E; box-shadow: 0 4px 10px rgba(0, 0, 0, .06);">
			<h3 style="margin: 0 0 8px;">회원 목록</h3>
			<p style="margin: 0; color: #806A5A;">가입한 회원 전체 보기</p>
		</a>

		<!-- (선택) 회원 등록 -->
		<a href="<c:url value='/admin/members/createForm.do'/>"
			style="display: block; border: 1px solid #FFE1CB; border-radius: 16px; background: #fff; padding: 16px; text-decoration: none; color: #59463E; box-shadow: 0 4px 10px rgba(0, 0, 0, .06);">
			<h3 style="margin: 0 0 8px;">회원 등록</h3>
			<p style="margin: 0; color: #806A5A;">새 회원 추가</p>
		</a>
	</div>
</section>
