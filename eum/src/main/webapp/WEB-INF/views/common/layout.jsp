<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <tiles:importAttribute name="title" scope="request" ignore="true" />
  <title>${empty title ? 'EuM:' : title}</title>

  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- 전역 폰트 -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" />

  <!-- 공통 CSS -->
  <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>" />
  <link rel="stylesheet" href="<c:url value='/resources/css/footer.css'/>" />

  <!-- 전역 토큰/레이아웃 -->
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
      min-height:100dvh; display:flex; flex-direction:column;
    }
    main#content{ max-width: var(--maxw); margin: 0 auto; padding: 24px 16px; box-sizing: border-box; flex:1 0 auto; }
    footer.site-footer{ position: static; }
  </style>

  <!-- (선택) 페이지별 head 리소스 -->
  <tiles:insertAttribute name="pageHead" ignore="true" />
</head>

<body>
  <!-- 공통 헤더 -->
  <tiles:insertAttribute name="header" ignore="true" />

  <!-- 본문 -->
  <main id="content" role="main">
    <tiles:insertAttribute name="body" ignore="true" />
  </main>

  <!-- 공통 푸터 -->
  <tiles:insertAttribute name="footer" ignore="true" />

  <!-- 🔧 브리지: 컨텍스트/링크 기본값 주입 (header.script.js 전에 실행) -->
  <script>
    window.EUM_CTX = '${ctx}';
    (function () {
      var ra = document.getElementById('rightArea');
      if (!ra) return;
      if (!ra.dataset.mypage) ra.dataset.mypage = '<c:url value="/member/mypage.do"/>';
      if (!ra.dataset.point)  ra.dataset.point  = '<c:url value="/member/point.do"/>';
      if (!ra.dataset.notify) ra.dataset.notify = '<c:url value="/notify.do"/>';
      if (!ra.dataset.logout) ra.dataset.logout = '<c:url value="/logout.do"/>';
      if (!ra.dataset.login)  ra.dataset.login  = '<c:url value="/member/loginForm.do"/>';
      if (!ra.dataset.signup) ra.dataset.signup = '<c:url value="/member/signupForm.do"/>';
    })();
  </script>

  <!-- 공통 스크립트 -->
  <script defer src="<c:url value='/resources/js/header.script.js'/>"></script>
  <script defer src="<c:url value='/resources/js/footer.script.js'/>"></script>
  <script defer src="<c:url value='/resources/js/footer.pad.js'/>"></script>

  <!-- (선택) 페이지별 스크립트 -->
  <tiles:insertAttribute name="pageScripts" ignore="true" />
</body>
</html>
