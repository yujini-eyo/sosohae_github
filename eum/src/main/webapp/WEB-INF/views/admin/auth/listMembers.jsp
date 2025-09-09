<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 세션 체크: 관리자만 -->
<%-- <c:if test="${empty sessionScope.adminUser}">
	<c:redirect url="${ctx}/admin/auth/login.do" />
</c:if> --%>
<!-- ✅ 변경 후 -->
<c:if test="${empty sessionScope.adminUser}">
  <c:url var="loginUrl" value="/admin/auth/login.do"/>
  <c:redirect url="${loginUrl}"/>
</c:if>

<h1 style="margin: 16px 0;">회원 목록</h1>

<table border="1" cellspacing="0" cellpadding="8"
	style="width: 100%; border-collapse: collapse;">
	<thead style="background: #faf3eb;">
		<tr>
			<th style="width: 140px;">ID</th>
			<th style="width: 120px;">이름</th>
			<th style="width: 220px;">이메일</th>
			<th style="width: 140px;">전화</th>
			<th style="width: 180px;">가입일</th>
			<th>비고</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty membersList}">
				<c:forEach var="m" items="${membersList}">
					<tr>
						<td><c:out value="${m.id}" /></td>
						<td><c:out value="${m.name}" /></td>
						<td><c:out value="${m.email}" /></td>
						<td><c:out value="${m.phone}" /></td>
						<td><c:choose>
								<c:when test="${not empty m.createdAt}">
									<fmt:formatDate value="${m.createdAt}"
										pattern="yyyy-MM-dd HH:mm" />
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose></td>
						<td>
							<%-- <a href="<c:url value='/admin/auth/listMembers.do?memberId=${m.id}'/>">조회</a> --%>
							<!-- ✅ 변경 후 -->
							<c:url var="lookupUrl" value="/admin/members/lookup.do">
								<c:param name="memberId" value="${m.id}" />
							</c:url> <a href="${lookupUrl}">조회</a>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="6" style="text-align: center; color: #777;">등록된
						회원이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

<div style="margin-top: 14px;">
	<a href="<c:url value='/admin/main.do'/>">← 관리자 메인</a>
</div>
