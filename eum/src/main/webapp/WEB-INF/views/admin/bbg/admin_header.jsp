<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="nav">
  <a href="${ctx}/admin/board/listArticles.do"><strong>관리자 게시판</strong></a>
  <c:if test="${sessionScope.userRole eq 'ADMIN' || (sessionScope.member ne null && sessionScope.member.role eq 'ADMIN')}">
    <a href="${ctx}/admin/board/writeForm.do">글 작성</a>
  </c:if>
</div>
