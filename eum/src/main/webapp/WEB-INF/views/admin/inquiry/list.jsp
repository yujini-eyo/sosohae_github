<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container">
  <h2>1:1 문의 목록</h2>

  <form method="get" action="${pageContext.request.contextPath}/admin/inquiry/list.do" class="form-inline" style="margin-bottom:10px;">
    <select name="field">
      <option value="">전체</option>
      <option value="member_id" ${q.field == 'member_id' ? 'selected' : ''}>회원ID</option>
      <option value="title"     ${q.field == 'title' ? 'selected' : ''}>제목</option>
    </select>
    <input type="text" name="keyword" value="${q.keyword}" placeholder="검색어"/>
    <select name="status">
      <option value="">상태 전체</option>
      <option value="OPEN"     ${q.status == 'OPEN' ? 'selected' : ''}>OPEN</option>
      <option value="ANSWERED" ${q.status == 'ANSWERED' ? 'selected' : ''}>ANSWERED</option>
    </select>
    <select name="sort">
      <option value="created_at"  ${q.sort == 'created_at' ? 'selected' : ''}>등록일</option>
      <option value="answered_at" ${q.sort == 'answered_at' ? 'selected' : ''}>답변일</option>
    </select>
    <select name="order">
      <option value="DESC" ${q.order != 'ASC' ? 'selected' : ''}>내림차순</option>
      <option value="ASC"  ${q.order == 'ASC' ? 'selected' : ''}>오름차순</option>
    </select>
    <input type="number" name="size" min="1" value="${page.size}" style="width:70px"/>
    <button type="submit">검색</button>
  </form>

  <table border="1" cellpadding="6" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th>ID</th><th>회원ID</th><th>제목</th><th>상태</th><th>등록일</th><th>답변일</th><th>보기</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="r" items="${page.rows}">
      <tr>
        <td>${r.inquiryId}</td>
        <td>${r.memberId}</td>
        <td>${r.title}</td>
        <td>${r.status}</td>
        <td>${r.createdAt}</td>
        <td>${r.answeredAt}</td>
        <td><a href="${pageContext.request.contextPath}/admin/inquiry/view.do?id=${r.inquiryId}">상세</a></td>
      </tr>
    </c:forEach>
    <c:if test="${empty page.rows}">
      <tr><td colspan="7">데이터가 없습니다.</td></tr>
    </c:if>
    </tbody>
  </table>

  <div style="margin-top:10px;">
    <c:set var="totalPages" value="${page.totalPages}"/>
    <c:forEach begin="1" end="${totalPages}" var="p">
      <c:choose>
        <c:when test="${p == page.page}">
          <strong>[${p}]</strong>
        </c:when>
        <c:otherwise>
          <a href="?page=${p}&size=${page.size}&field=${q.field}&keyword=${q.keyword}&status=${q.status}&sort=${q.sort}&order=${q.order}">[${p}]</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>
  </div>
</div>
