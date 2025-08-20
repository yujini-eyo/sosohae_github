<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>하단 부분</title>

<style>
#footer {
	position: fixed;
	bottom: 0; /* 화면 하단 */
	left: 0; /* 왼쪽부터 */
	width: 100%; /* 화면 가로 전체 차지 */
	clear: both;
	padding: 0px;
	border: 1px solid #bcbcbc;
	background-color: #000;
	color: #fff; /* 글씨 흰색 */
	text-align: center; /* 가운데 정렬 */
}

.footer-container h4 {
	font-size: 15px;
	text-align: center;
	background-color: #000;
	color: #ffffff;
	justify-content: center;
	align-items: center;
}
</style>
</head>
<body>
	<div class="footer-container">
		<h4>e-mail:moamoa@test.com</h4>
		<h4>회사주소:대전광역시 서구 둔산동</h4>
		<h4>
			찾아오는 길:<a href="#map">약도</a>
		</h4>
	</div>
</body>
</html>