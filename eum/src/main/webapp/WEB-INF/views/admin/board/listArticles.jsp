<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${ctx}/resources/css/admin/board/listArticles.css"/>

<div class="toolbar">
  <h2>관리자 게시글 목록</h2>
  <c:if test="${sessionScope.userRole eq 'ADMIN' || (sessionScope.member ne null && sessionScope.member.role eq 'ADMIN')}">
    <a class="btn primary" href="${ctx}/admin/board/writeForm.do">글 작성</a>
  </c:if>
</div>

<table class="board-table">
  <thead>
    <tr>
      <th>번호</th>
      <th>제목</th>
      <th>작성자</th>
      <th>작성일</th>
      <th class="right">관리</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="a" items="${articlesList}">
    <tr>
      <td>${a.articleNO}</td>
      <td>
        <a href="${ctx}/admin/board/viewArticle.do?articleNO=${a.articleNO}">
          ${a.title}
        </a>
        <c:if test="${not empty a.imageFileName}">
          <span class="img-flag">[첨부]</span>
        </c:if>
      </td>
      <td>${a.id}</td>
      <td><span class="muted">${a.writeDate}</span></td>
      <td class="right">
        <form class="inline js-delete-form" method="post" action="${ctx}/admin/board/removeArticle.do">
          <input type="hidden" name="articleNO" value="${a.articleNO}">
          <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          </c:if>
          <button type="submit" class="btn">삭제</button>
        </form>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty articlesList}">
    <tr><td colspan="5" class="muted empty">게시글이 없습니다.</td></tr>
  </c:if>
  </tbody>
</table>

<form class="pager-form" method="get">
  <label class="muted">페이지 크기:
    <input type="number" name="size" value="${param.size!=null ? param.size : 10}" min="1" max="100">
  </label>
  <label class="muted">
    페이지:
    <input type="number" name="page" value="${param.page!=null ? param.page : 1}" min="1">
  </label>
  <button class="btn" type="submit">적용</button>
</form>

<script src="${ctx}/resources/js/admin/board/listArticles.js"></script>
