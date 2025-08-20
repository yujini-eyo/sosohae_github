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
<title>BAGUNI SPACE - 회원가입</title>
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
    
    .signup-container {
        background: rgba(255,255,255,0.95);
        padding: 40px 30px;
        border-radius: 20px;
        box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        width: 380px;
        text-align: center;
    }
    @keyframes fadeIn {
        from {opacity: 0; transform: translateY(-20px);}
        to {opacity: 1; transform: translateY(0);}
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
    <form id="signupPage" action="${contextPath}/member/signupForm.do" method="post">
        아이디: <input type="text" name="id" required><br>
        비밀번호: <input type="password" name="pwd" required><br>
        이름: <input type="text" name="username" required><br>
        <button type="submit">회원가입</button>
    </form>
    <div class="login-link">
        이미 계정이 있으신가요? <a href="login.do">로그인</a>
    </div>
</div>

</body>
</html>
