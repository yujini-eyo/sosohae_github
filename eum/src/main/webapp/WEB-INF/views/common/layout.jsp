<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>
    ${main != null && main.pageTitle != null
        ? main.pageTitle
        : (pageTitle != null ? pageTitle : 'Eum:')}
  </title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- 전역 폰트 -->
  <link href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" rel="stylesheet" />

  <!-- 공통 CSS (헤더/푸터) -->
  <link rel="stylesheet" href="<c:url value='/resources/css/header.css'/>" />
  <link rel="stylesheet" href="<c:url value='/resources/css/footer.css'/>" />

  <!-- (선택) 디자인 토큰 Fallback : 프로젝트 전역에서 이미 선언돼 있으면 삭제 가능 -->
  <style>
    :root{
      --bg:#FFF8F2; --text:#59463E; --muted:#806A5A;
      --primary:#F6A96D; --primary-600:#e98c45;
      --brand:#FFE8C2; --brand-200:#FADFCB;
      --card:#FFFFFF; --card-br:#FFE1CB;
      --shadow-soft:0 6px 16px rgba(0,0,0,0.08);
      --maxw:1200px; --header-h:64px;
    }
    body{
      margin:0; background:#fff; color:#222;
      font-family:'SUIT', system-ui, -apple-system, Segoe UI, Roboto, Arial, Apple SD Gothic Neo, 'Noto Sans KR', sans-serif;
    }
    main{ max-width: var(--maxw); margin: 0 auto; padding: 24px; box-sizing: border-box; }
  </style>

  <!-- (선택) 페이지별 추가 head 리소스 -->
  <tiles:insertAttribute name="head" ignore="true" />
</head>
<body>
  <%-- 공통 헤더 --%>
  <jsp:include page="/WEB-INF/views/common/header.jsp" />

  <%-- 페이지 본문 (Tiles body 또는 수동 include) --%>
  <main id="content" role="main">
    <tiles:insertAttribute name="body" ignore="true" />
    <%-- 예: <jsp:include page="${view}" /> --%>
  </main>

  <%-- 공통 푸터 (Tiles 정의에서 footer 경로를 지정해두었다면 자동 주입) --%>
  <tiles:insertAttribute name="footer" ignore="true" />

  <!-- JS: 외부 정적 .js 파일을 그대로 사용 -->
  <!-- 순서 유지: header → header.patch → footer → footer.pad -->
  <script src="${ctx}/resources/js/header.script.js"></script>
  <script src="${ctx}/resources/js/header.patch.script.js"></script>
  <script src="${ctx}/resources/js/footer.script.js"></script>
  <script src="${ctx}/resources/js/footer.pad.js"></script>
</body>
</html>
