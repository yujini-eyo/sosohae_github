<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<style>
:root { -
	-bg: #FFF8F2; -
	-text: #59463E; -
	-muted: #806A5A; -
	-primary: #F6A96D; -
	-primary-600: #e98c45; -
	-brand: #FFE8C2; -
	-brand-200: #FADFCB; -
	-card: #FFFFFF; -
	-card-br: #FFE1CB; -
	-shadow-soft: 0 4px 10px rgba(0, 0, 0, 0.06); -
	-radius: 20px; -
	-maxw: 1100px; -
	-header-h: 72px; -
	-footer-h: 90px; /* 초기값(실측으로 곧 대체) */ -
	-focus: 0 0 0 3px rgba(246, 169, 109, .35);
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
	background: var(- -bg);
	color: var(- -text);
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
	box-shadow: var(- -focus);
	border-radius: 10px;
}

/* 한 화면 레이아웃 */
main.content {
	flex: 1;
	height: calc(100dvh - var(- -header-h)- var(- -footer-h));
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
	border: 2px solid var(- -card-br);
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

<meta charset="UTF-8">
<title>로그인창</title>
<c:choose>
	<c:when test="${result=='loginFailed' }">
		<script>
			window.onload = function() {
				alert("아이디나 비밀번호가 틀립니다.다시 로그인 하세요!");
			}
		</script>
	</c:when>
</c:choose>
</head>

<body>
	<form name="frmLogin" method="post"
		action="${contextPath}/member/login.do">
		<table border="1" width="80%" align="center">
			<tr align="center">
				<td>아이디</td>
				<td>비밀번호</td>
			</tr>
			<tr align="center">
				<td><input type="text" name="id" value="" size="20"></td>
				<td><input type="password" name="pwd" value="" size="20">
				</td>
			</tr>
			<tr align="center">
				<td colspan="2"><input type="submit" value="로그인"> <input
					type="reset" value="다시입력"></td>
			</tr>
		</table>
	</form>
</body>
</html>
