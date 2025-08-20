<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>BAGUNI SPACE - 로그인</title>
<style>
    body {
        margin: 0;
        padding: 0;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: #ffffff;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .login-container {
        background: rgba(255,255,255,0.95);
        padding: 50px 40px;
        border-radius: 20px;
        box-shadow: 0 15px 30px rgba(0,0,0,0.2);
        width: 360px;
        text-align: center;
        animation: fadeIn 0.7s ease;
    }

    @keyframes fadeIn {
        from {opacity: 0; transform: translateY(-20px);}
        to {opacity: 1; transform: translateY(0);}
    }

    .login-container h3 {
        margin-bottom: 30px;
        font-size: 1.8rem;
        color: #333;
        letter-spacing: 1px;
    }

    .login-container input {
        width: 100%;
        padding: 14px 15px;
        margin: 12px 0;
        border: 1px solid #ddd;
        border-radius: 10px;
        font-size: 1rem;
        transition: all 0.3s ease;
        box-sizing: border-box;
    }


    .login-container button {
        width: 100%;
        padding: 14px;
        margin-top: 20px;
        border: none;
        border-radius: 10px;
        font-size: 1rem;
        cursor: pointer;
        background: #000;
        color: white;
        font-weight: bold;
        transition: all 0.3s ease;
    }

    .login-container button:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.2);
    }

    .signup-link {
        margin-top: 25px;
        font-size: 0.9rem;
        color: #555;
        line-height: 1.5;
    }

    .signup-link a {
        color: #6c63ff;
        font-weight: bold;
        text-decoration: none;
        transition: all 0.2s ease;
    }

    .signup-link a:hover {
        text-decoration: underline;
        color: #5146d9;
    }
    
</style>
</head>
<body>

<div class="login-container">
    <h3>로그인</h3>
    <form id="loginPage" method="post" action="${contextPath}/member/login.do">
        <!-- 회원가입과 동일하게 userId 로 통일 -->
        <input type="text" name="id" placeholder="아이디" required>
        <input type="password" name="pwd" placeholder="비밀번호" required>
        <button type="submit">로그인</button>
    </form>

    <div class="signup-link">
        계정이 없으신가요? <a href="signupForm.do">회원가입</a><br>
        <a href="find-id.jsp">아이디 찾기</a> | 
        <a href="find-password.jsp">비밀번호 찾기</a>
    </div>
</div>

</body>
</html>
