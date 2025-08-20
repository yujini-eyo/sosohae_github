<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: linear-gradient(135deg, #74ebd5 0%, #9face6 100%);
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 0;
    }
    .signup-container {
        background: rgba(255,255,255,0.95);
        padding: 40px 30px;
        border-radius: 20px;
        box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        width: 380px;
        text-align: center;
    }
    .signup-container h2 {
        margin-bottom: 25px;
        font-size: 1.6rem;
        color: #333;
    }
    .signup-container input {
        width: 100%;
        padding: 14px 15px;
        margin: 12px 0;
        border: 1px solid #ddd;
        border-radius: 10px;
        font-size: 1rem;
        box-sizing: border-box;
    }
    .signup-container button {
        width: 100%;
        padding: 14px;
        margin-top: 20px;
        border: none;
        border-radius: 10px;
        font-size: 1rem;
        cursor: pointer;
        background: linear-gradient(45deg, #6c63ff, #5146d9);
        color: white;
        font-weight: bold;
    }
    .signup-container button:hover {
        opacity: 0.9;
    }
    .login-link {
        margin-top: 20px;
        font-size: 0.9rem;
    }
    .login-link a {
        color: #6c63ff;
        text-decoration: none;
        font-weight: bold;
    }
    .login-link a:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>

<div class="signup-container">
    <h2>회원가입</h2>
    <form action="/member/addMember.do" method="post">
        아이디: <input type="text" name="id" required><br>
        비밀번호: <input type="password" name="pwd" required><br>
        이름: <input type="text" name="username" required><br>
        <button type="submit">회원가입</button>
    </form>
    <div class="login-link">
        이미 계정이 있으신가요? <a href="login.jsp">로그인</a>
    </div>
</div>

</body>
</html>
