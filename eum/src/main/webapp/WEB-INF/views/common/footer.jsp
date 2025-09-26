<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isAuthed" value="${not empty sessionScope.member}" />

<footer id="eumFooter"
        class="site-footer"
        data-ctx="${ctx}"
        data-auth="${isAuthed}"
        data-about="/about.do"
        data-guide="/guide.do"
        data-login="/member/loginForm.do"
        data-signup="/member/signupForm.do"
        data-helpwrite="/help/write.do"
        data-boardlist="/board/listArticles.do"
        data-chat="/chat/list.do"
        data-mypage="/member/mypage.do">

  <!-- 데스크톱/태블릿만 보이도록: footer-desktop 클래스를 내부 래퍼에만 부여 -->
  <div class="footer-desktop">
    <div class="container">
      <div class="footer-top">
        <a class="footer-brand" href="<c:url value='/main.do'/>">EuM:</a>
        <p class="footer-tagline">함께 돕고 연결되는 공간</p>
      </div>

      <div class="footer-contact-plain">
        <p>문의: <a href="mailto:help@eum.local">help@eum.local</a></p>
        <p>고객센터: 1588-0000 (평일 09:00~18:00)</p>
      </div>

      <div class="footer-bottom">
        <p class="footer-copy">© EuM</p>
      </div>
    </div>
  </div>
</footer>

<link rel="stylesheet" href="<c:url value='/resources/css/footer.css'/>" />
<script src="<c:url value='/resources/js/footer.script.js'/>"></script>
<script src="<c:url value='/resources/js/footer.pad.js'/>"></script>
