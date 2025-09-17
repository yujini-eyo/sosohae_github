<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="container">
	<h2>지원자 목록</h2>
	<c:choose>
		<c:when test="${empty applicants}">
			<p>아직 지원자가 없습니다.</p>
		</c:when>
		<c:otherwise>
			<table class="table">
				<thead>
					<tr>
						<th>#</th>
						<th>지원자 ID</th>
						<th>메시지</th>
						<th>상태</th>
						<th>신청시각</th>
						<th>선정</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="a" items="${applicants}" varStatus="s">
						<tr>
							<td>${s.index + 1}</td>
							<td><c:out value="${a.volunteerId}" /></td>
							<td><c:out value="${a.message}" /></td>
							<td><c:out value="${a.status}" /></td>
							<td><fmt:formatDate value="${a.createdAt}"
									pattern="yyyy-MM-dd HH:mm" /></td>
							<td>
								<form method="post" action="<c:url value='/support/select.do'/>">
									<input type="hidden" name="articleNO"
										value="${param.articleNO}" /> <input type="hidden"
										name="applicationId" value="${a.applicationId}" />
									<button type="submit" class="btn btn-primary"
										${a.status ne 'APPLIED' ? 'disabled' : ''}>선정</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
</div>