<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <!-- Tiles 'title'를 변수로 가져와서 안전하게 사용 -->
  <tiles:importAttribute name="title" scope="request" ignore="true" />
  <title>${empty title ? 'EuM:' : title}</title>

  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- 전역 폰트 -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" />

  <!-- 공통 CSS -->
  <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>" />
  <link rel="stylesheet" href="<c:url value='/resources/css/footer.css'/>" />

  <!-- (선택) 전역 토큰 폴백: 프로젝트 전역에서 이미 선언되어 있으면 삭제 가능 -->
  <style>
    :root{
      --bg:#FFF8F2; --text:#59463E; --muted:#806A5A;
      --primary:#F6A96D; --primary-600:#e98c45;
      --brand:#FFE8C2; --brand-200:#FADFCB;
      --card:#FFFFFF; --card-br:#FFE1CB;
      --shadow-soft:0 6px 16px rgba(0,0,0,0.08);
      --maxw:1200px; --header-h:72px;
    }
    html, body { height:100%; }
    body{
      margin:0; background:#fff; color:#222;
      font-family:'SUIT', system-ui, -apple-system, Segoe UI, Roboto, Arial, Apple SD Gothic Neo, 'Noto Sans KR', sans-serif;
    }
    main#content{ max-width: var(--maxw); margin: 0 auto; padding: 24px 16px; box-sizing: border-box; }
  </style>

  <!-- (선택) 페이지별 head 리소스 주입: 각 definition에서 name="pageHead"로 전달 -->
  <tiles:insertAttribute name="pageHead" ignore="true" />
</head>

<body>
  <!-- 공통 헤더 (Tiles) -->
  <tiles:insertAttribute name="header" ignore="true" />

  <!-- 본문 -->
  <main id="content" role="main">
    <tiles:insertAttribute name="body" ignore="true" />
  </main>

  <!-- 공통 푸터 (Tiles) -->
  <tiles:insertAttribute name="footer" ignore="true" />

  <!-- 공통 스크립트 -->
  <script defer src="<c:url value='/resources/js/header.script.js'/>"></script>
  <script defer src="<c:url value='/resources/js/header.patch.script.js'/>"></script>
  <script defer src="<c:url value='/resources/js/footer.script.js'/>"></script>
  <script defer src="<c:url value='/resources/js/footer.pad.js'/>"></script>

  <!-- (선택) 페이지별 스크립트 주입: 각 definition에서 name="pageScripts"로 전달 -->
  <tiles:insertAttribute name="pageScripts" ignore="true" />
</body>
</html>
