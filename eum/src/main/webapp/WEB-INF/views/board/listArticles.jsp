<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
  <meta charset="UTF-8">
  <title>게시판 목록</title>
  <style>
    body { font-family: sans-serif; margin: 24px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #ddd; padding: 8px; }
    th { background: #f5f5f5; }
    .title a { text-decoration: none; }
  </style>
</head>
<body>
  <h1>DB 게시판 목록</h1>

  <c:choose>
    <c:when test="${not empty articlesList}">
      <table>
        <thead>
          <tr>
            <th style="width:100px">글번호</th>
            <th>제목</th>
            <th style="width:200px">작성일</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="a" items="${articlesList}">
            <tr>
              <td><c:out value="${a.articleNO}" /></td>
              <td class="title">
                <a href="<c:url value='/board/viewArticle.do'><c:param name='articleNO' value='${a.articleNO}'/></c:url>">
                  <c:out value="${a.title}" />
                </a>
              </td>
              <td><fmt:formatDate value="${a.writeDate}" pattern="yyyy-MM-dd HH:mm" /></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:when>
    <c:otherwise>
      <p>등록된 게시글이 없습니다.</p>
      <p><a href="<c:url value='/board/articleForm.do'/>">글쓰기</a></p>
    </c:otherwise>
  </c:choose>
</body>