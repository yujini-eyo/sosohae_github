<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>로그인 - 소소한 도움</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!-- SUIT 폰트 -->
<link
	href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css"
	rel="stylesheet">

<!-- 공통 변수 -->
<style>
:root { 
	--bg: #FFF8F2;
	--text: #59463E;
	--muted: #806A5A;
	--primary: #F6A96D;
	--primary-600: #e98c45;
	--brand: #FFE8C2;
	--brand-200: #FADFCB;
	--card: #FFFFFF;
	--card-br: #FFE1CB;
	--shadow-soft: 0 4px 10px rgba(0, 0, 0, 0.06);
	--radius: 20px;
	--maxw: 1100px;
	--header-h: 72px;
	--footer-h: 90px; /* 초기값(실측으로 곧 대체) */
	--focus: 0 0 0 3px rgba(246, 169, 109, .35);
}

* {
	box-sizing: border-box;
	margin: 0;
	padding: 0;
}

html, body {
	height: 100%;
}

body {
	font-family: 'SUIT', system-ui, -apple-system, Segoe UI, Roboto,
		sans-serif;
	background: var(--bg);
	color: var(--text);
	display: flex;
	flex-direction: column;
	overflow: hidden; /* 스크롤 제거 */
}

a {
	color: inherit;
	text-decoration: none
}

a:focus, button:focus {
	outline: none;
	box-shadow: var(--focus);
	border-radius: 10px;
}

/* 한 화면 레이아웃 */
main.content {
	flex: 1;
	height: calc(100dvh -var(--header-h) -var(--footer-h));
	display: grid;
	place-items: center;
	padding: 0 16px;
}

.content-inner {
	width: 100%;
	max-width: 420px;
	transform-origin: center center; /* 축소 기준 */
}

/* ====== ✅ 로그인 박스는 "그대로" 유지 ====== */
h1 {
	font-size: 28px;
	font-weight: 900;
	text-align: center;
	margin-bottom: 18px;
}

.login-box {
	background: #fff;
	padding: 30px 24px;
	border-radius: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
	border: 2px solid var(--card-br);
	width: 100%;
	max-width: 380px;
	margin: 0 auto;
}

.login-box input {
	width: 100%;
	padding: 14px;
	margin-bottom: 16px;
	font-size: 18px;
	border-radius: 12px;
	border: 2px solid #FFE1CB;
}

.login-box button {
	width: 100%;
	padding: 14px;
	font-size: 18px;
	font-weight: 700;
	background: #FFB88A;
	color: #fff;
	border: none;
	border-radius: 30px;
	cursor: pointer;
	margin-top: 10px;
}

.sub-buttons {
	margin-top: 20px;
	text-align: center;
}

.sub-buttons a {
	font-size: 16px;
	color: #7A5A48;
	text-decoration: none;
	margin: 0 10px;
}

.sub-buttons a:hover {
	text-decoration: underline;
}
</style>
</head>
<body>

	<!-- 본문 -->
	<main class="content">
		<div class="content-inner" id="fitBox">
			<h1>로그인</h1>
			
			<form class="login-box" method="post" action="${contextPath}/member/login.do" autocomplete="on">
				<c:if test="${not empty _csrf}">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</c:if>
				
				<input type="text" id="loginId" name="id" placeholder="아이디" autocomplete="username" required />
				<input type="password" id="loginPw" name="password" placeholder="비밀번호" autocomplete="current-password" required />
					
				<button type="submit" id="doLogin">로그인</button>
				
				<div class="sub-buttons">
					<a href="${contextPath}/member/memberForm.do">회원가입</a> |
					<a href="#">아이디 · 비밀번호 찾기</a>
				</div>
			</form>
		</div>
	</main>

	<c:if test="${result=='loginFailed'}">
		<script>
			alert("아이디 또는 비밀번호가 올바르지 않습니다.");
			document.getElementById('loginId')?.focus();
		</script>
	</c:if>
</body>
</html>