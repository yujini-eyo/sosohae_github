<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title><tiles:insertAttribute name="title" /></title>
<style>
@import
	url('https://fonts.googleapis.com/css2?family=Fugaz+One&display=swap');

/* ===== 기본 설정 ===== */
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	margin: 0;
	padding: 0;
	background-color: #ffffff;
	color: #ffffff;
	line-height: 1.6;
}

:root {
  --bg-color: #fff;
  --text-color: #000;
}

.dark-theme {
  --bg-color: #000;
  --text-color: #fff;
}
/* ===== 헤더 ===== */
.header {
	width: 100%;
	background: #000;
	color: #fff;
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20px 40px;
	box-sizing: border-box;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 2100;
}

.header a.logo {
	font-family: 'Fugaz One', cursive;
	font-size: 2rem;
	color: #fff;
	text-decoration: none;
	text-shadow: 1px 1px black;
	transition: color 0.3s ease;
}

.header a.logo:hover {
	color: #ccc;
}

.header-right {
	display: flex;
	align-items: center;
	gap: 15px;
}

.header-right a {
	color: #fff;
	text-decoration: none;
	transition: color 0.3s ease;
}

.header-right a:hover {
	color: var(- -color-accent);
}

.cart-link {
	display: flex;
	align-items: center;
	gap: 5px;
	font-weight: 600;
}

.cart-count {
	background: #d9534f;
	color: #fff;
	border-radius: 50%;
	font-size: 0.7rem;
	padding: 2px 6px;
}

/* Hamburger 버튼 */
#hamburger {
	display: none;
	position: fixed;
	top: 10px;
	left: 10px;
	font-size: 1.5rem;
	background-color: #000;
	color: #ffffff;
	border: none;
	border-radius: 4px;
	padding: 5px 10px;
	cursor: pointer;
	z-index: 4000;
}

/* ===== 사이드바 ===== */
#sidebar-left {
	width: 260px;
	background-color: #f9f9f9;
	border-right: 1px solid #ddd;
	height: 100vh;
	position: fixed;
	top: 95px;
	left: 0;
	padding: 20px;
	box-sizing: border-box;
	z-index: 2000;
	transition: transform 0.3s ease-in-out;
}

#sidebar-left nav ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

#sidebar-left nav ul li {
	margin-bottom: 14px;
}

#sidebar-left nav ul li a {
	text-decoration: none;
	color: #333;
	font-size: 1rem;
}

#sidebar-left nav ul li a:hover, #sidebar-left nav ul li a:focus {
	color: var(- -color-accent);
}

/* ===== 컨텐츠 ===== */
#content {
	margin-left: 280px;
	padding: 120px 20px 20px 20px;
	box-sizing: border-box;
	text-align: left;
}



/* ===== 반응형 ===== */
@media ( max-width : 1024px) {
	#sidebar-left {
		transform: translateX(-120%);
		width: 200px;
	}
	#content {
		margin-left: 0;
		padding-top: 100px;
	}
	#hamburger {
		display: block;
	}
}

@media ( max-width : 768px) {
	#sidebar-left {
		width: 180px;
		transform: translateX(-100%);
	}
	#sidebar-left.active {
		transform: translateX(0);
	}
	#hamburger {
		top: 8px;
		left: 8px;
		font-size: 1.2rem;
		padding: 4px 8px;
	}
}
</style>
</head>
<body>
	<!-- ===== 헤더 ===== -->
	<div class="header">
		<a href="main.do" class="logo">BAGUNI SPACE</a>
		<div class="header-right" id="headerMenu"></div>
	</div>
	<button id="hamburger" aria-label="Toggle navigation">☰</button>

	<!-- ===== 사이드바 ===== -->
	<div id="sidebar-left">
		<tiles:insertAttribute name="side" />
	</div>

	<!-- ===== 본문 ===== -->
	<div id="content">
		<tiles:insertAttribute name="body" />
	</div>

	<!-- ===== 푸터 ===== -->
	<div id="footer">
		<tiles:insertAttribute name="footer" />
	</div>

</body>
</html>
