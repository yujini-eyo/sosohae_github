<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% request.setCharacterEncoding("UTF-8");%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
 </head>
 <body>
 <style>
    .header {
        position: fixed; /* ✅ 상단 고정 */
        top: 0;
        left: 0;
        width: 100%;
        background-color: #000;
        color: #fff;
        z-index: 1000;
    }

    body {
        padding-top: 60px;
    }

    .header-container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 20px;
    }

    .logo {
        font-family: 'Fugaz One', cursive;
        font-size: 2rem;
        text-decoration: none;
        color: #fff;
    }

    .logo:hover {
        color: #ff6600;
    }

    .nav a {
        margin-left: 15px;
        text-decoration: none;
        color: #fff;
        font-size: 1rem;
    }

    .nav a:hover {
        color: #ff6600;
    }

    .welcome {
        margin-right: 10px;
        font-size: 1rem;
        color: #fff;
    }

    .hamburger {
        display: flex;
        flex-direction: column;
        cursor: pointer;
        gap: 4px;
    }

    .hamburger span {
        width: 25px;
        height: 3px;
        background-color: #fff;
    }
    
    h3 {
    	font-family: 'Fugaz One', cursive;
        font-size: 1rem;
        text-decoration: none;
    	color : #fff;
    	z-index: 1500;
    }
</style>
<header class="header">
    <div class="header-container">
        <!-- ✅ Tiles 정의(/main) 로 이동하도록 c:url 적용 -->
        <a href="<c:url value='/main.do' />" class="logo">BAGUNI SPACE</a>

        <nav class="nav">
            
            <c:choose>
          <c:when test="${isLogOn == true  && member!= null}">
            <h3>환영합니다. ${member.name }님! </h3>
            <a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a>
          </c:when>
          <c:otherwise>
	        <a href="${contextPath}/member/loginForm.do"><h3>로그인</h3></a>
	      </c:otherwise>
	   </c:choose>     
        </nav>

        <div class="hamburger" id="hamburger">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</header>

<script>
    document.getElementById('hamburger').addEventListener('click', function () {
        var sidebar = document.getElementById('sidebar-left'); 
        if (sidebar) { 
            sidebar.classList.toggle('active');
        }
    });
</script>


</body>
</html>