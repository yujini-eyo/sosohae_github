<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 로그인</title>
  <style>
    body{font-family:system-ui,-apple-system,Segoe UI,Roboto,sans-serif;background:#FFF8F2;margin:0;}
    .wrap{max-width:420px;margin:60px auto;padding:24px;background:#fff;border:1px solid #f0e6dc;border-radius:12px;}
    h2{margin:0 0 16px 0;}
    label{display:block;margin-bottom:6px;color:#555;}
    input[type=text], input[type=password]{width:100%;padding:10px;border:1px solid #ddd;border-radius:8px;margin-bottom:12px;}
    .btn{width:100%;padding:12px;border:0;border-radius:8px;background:#f6a96d;color:#fff;font-weight:700;cursor:pointer;}
    .btn:hover{background:#e98c45;}
    .muted{color:#777;font-size:12px;margin-top:10px;text-align:center;}
  </style>
</head>
<body>
<div class="wrap">
  <h2>관리자 로그인</h2>
  <form method="post" action="${ctx}/admin/doLogin.do" accept-charset="UTF-8">
    <c:if test="${not empty _csrf}">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </c:if>

    <label for="id">아이디</label>
    <input type="text" id="id" name="id" required autofocus/>

    <label for="password">비밀번호</label>
    <input type="password" id="password" name="password" required/>

    <button type="submit" class="btn">로그인</button>

    <p class="muted">관리자 전용 페이지입니다.</p>
  </form>
</div>
</body>
</html>
