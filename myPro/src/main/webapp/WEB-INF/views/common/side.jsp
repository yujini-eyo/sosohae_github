<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!-- Hamburger 버튼 -->
<button id="hamburger" aria-label="Toggle navigation">☰</button>

<!-- 사이드 메뉴 -->
<nav id="sidebar-left">
    <h2>메뉴</h2>
    <ul>
        <li><a class="no-underline" href="notice.html">공지사항&메모</a></li>
        <li><a class="no-underline" href="cart.html">장바구니</a></li>
        <li><a class="no-underline" href="my-bodytype.html">체형 / 퍼스널컬러</a></li>
        <li><a class="no-underline" href="style.html">스타일</a></li>
        <li><a class="no-underline" href="clothing.html">의류</a></li>
        <li><a class="no-underline" href="shoes.html">신발</a></li>
        <li><a class="no-underline" href="bag.html">가방</a></li>
        <li><a class="no-underline" href="beauty.html">뷰티</a></li>
        <li><a class="no-underline" href="digital-phone.html">디지털 / 핸드폰</a></li>
        <li><a class="no-underline" href="fashion-accessories.html">패션소품</a></li>
        <li><a class="no-underline" href="jewelry.html">주얼리</a></li>
        <li><a class="no-underline" href="big-size.html">빅사이즈</a></li>
        <li><a class="no-underline" href="tetris.html">미니게임 - 테트리스</a></li>
    </ul>
</nav>

<style>
    /* 공통 스타일 */
    #sidebar-left {
        width: 260px;
        background-color: #f9f9f9;
        border-right: 1px solid #ddd;
        height: 100vh;
        padding: 20px;
        box-sizing: border-box;
        font-size: 0.9rem;
        position: fixed;
        top: 95;
        left: 0;
        z-index: 2000;
        transition: transform 0.3s ease-in-out;
    }

    #sidebar-left h2 {
        margin-bottom: 15px;
        font-size: 1.2rem;
    }

    #sidebar-left ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    #sidebar-left ul li {
        margin-bottom: 10px;
    }

    #sidebar-left ul li a {
        text-decoration: none;
        color: #333;
        transition: color 0.2s;
    }

    #sidebar-left ul li a:hover,
    #sidebar-left ul li a:focus {
        color: #007bff;
    }

    .no-underline {
        text-decoration: none;
    }

    /* Hamburger 버튼 */
    #hamburger {
        display: none;
        position: fixed;
        top: 20px;
        left: 10px;
        font-size: 1.5rem;
        background-color: #000;
        color: #fff;
        border: none;
        border-radius: 4px;
        padding: 5px 10px;
        cursor: pointer;
        z-index: 4000;
    }

    /* 반응형 */
    @media (max-width: 1024px) {
        #sidebar-left {
            transform: translateX(-120%);
            width: 200px;
        }
        #hamburger { display: block; }
    }

    @media (max-width: 768px) {
        #sidebar-left {
            transform: translateX(-100%);
            width: 180px;
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

<script>
    // Hamburger 메뉴 토글
    const hamburger = document.getElementById('hamburger');
    const sidebar = document.getElementById('sidebar-left');

    hamburger.addEventListener('click', () => {
        sidebar.classList.toggle('active');
    });
</script>