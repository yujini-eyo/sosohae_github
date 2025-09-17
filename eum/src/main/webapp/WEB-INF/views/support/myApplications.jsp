<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<section class="board">
  <h2>내 신청 내역</h2>

  <c:choose>
    <c:when test="${empty applications}">
      <p>신청 내역이 없습니다.</p>
    </c:when>
    <c:otherwise>
      <table class="table">
        <thead>
          <tr>
            <th>#</th>
            <th>요청ID</th>
            <th>상태</th>
            <th>신청일시</th>
            <th>메모</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${applications}" var="a" varStatus="st">
            <tr>
              <td>${st.count}</td>
              <td>${a.requestId}</td>
              <td>${a.status}</td>
              <td><fmt:formatDate value="${a.appliedAt}" pattern="yyyy-MM-dd HH:mm"/></td>
              <td><c:out value="${a.memo}"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</section>
