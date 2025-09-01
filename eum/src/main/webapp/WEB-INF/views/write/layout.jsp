<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="cpath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title><tiles:insertAttribute name="title" ignore="true"/></title>

  <!-- 공통 폰트/스타일 -->
  <link href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${cpath}/resources/css/header.css"/>
  <link rel="stylesheet" href="${cpath}/resources/css/footer.css"/>

  <tiles:insertAttribute name="head" ignore="true"/>
</head>
<body style="margin:0">

  <header>
    <tiles:insertAttribute name="header" ignore="true"/>
  </header>

  <main>
    <tiles:insertAttribute name="body"/>
  </main>

  <footer>
    <tiles:insertAttribute name="footer" ignore="true"/>
  </footer>

  <script src="${cpath}/resources/js/header.js"></script>
  <script src="${cpath}/resources/js/footer.js"></script>
  <tiles:insertAttribute name="scripts" ignore="true"/>
</body>
</html>
