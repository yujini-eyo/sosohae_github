<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>글 목록 (AJAX)</title>

<link rel="stylesheet" href="<c:url value='/resources/css/listArticlesAjax.css'/>" />

<!-- jQuery (CDN) -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
  <div class="ajax-wrap">
    <div class="hd">
      <h2>게시글 목록 (AJAX)</h2>
      <div class="status" id="status">불러오는 중...</div>
    </div>

    <table aria-label="게시글 목록">
      <thead>
        <tr>
          <th style="width:80px;">번호</th>
          <th>제목</th>
          <th style="width:140px;">작성자</th>
          <th style="width:160px;">등록일</th>
          <th style="width:120px;">지역</th>
          <th style="width:100px;">긴급도</th>
        </tr>
      </thead>
      <tbody id="rows">
        <tr><td class="empty" colspan="6">데이터를 불러오는 중입니다...</td></tr>
      </tbody>
    </table>
  </div>
	
	<script defer src="<c:url value='/resources/js/listArticlesAjax.js'/>"></script>
</body>
</html>
