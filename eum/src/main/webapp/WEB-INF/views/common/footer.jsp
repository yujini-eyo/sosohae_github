<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy" var="currentYear"/>

<footer class="site-footer" role="contentinfo">
  <!-- 데스크톱/태블릿 전용(모바일에서는 숨김) -->
  <div class="container footer-desktop">
    <div class="footer-top">
      <a class="footer-brand" href="<c:url value='/main.do'/>" aria-label="홈으로">EuM:</a>
      <p class="footer-tagline">따뜻한 연결을 만드는 커뮤니티</p>
    </div>

    <!-- 요청하신 단순 <p> 정보 블럭 -->
    <div class="footer-contact-plain">
      <p>e-mail: <a href="mailto:admin@test.com">admin@test.com</a></p>
      <p>회사주소: 대전시 둔산동</p>
      <p>찾아오는 길: <a href="<c:url value='/map.do'/>">약도</a></p>
    </div>

    <div class="footer-bottom">
      <p class="footer-copy">© ${currentYear} EuM. All rights reserved.</p>
    </div>
  </div>

  <!-- 모바일 전용 하단 탭바 -->
  <nav class="mobile-tabbar" aria-label="하단 메뉴(모바일)">
    <a class="tab" href="<c:url value='/write.do'/>" data-tab="write" aria-label="도움 요청하기">
      <span class="tab-label">요청하기</span>
    </a>
    <a class="tab" href="<c:url value='/give.do'/>" data-tab="give" aria-label="도움 주기">
      <span class="tab-label">도움주기</span>
    </a>
    <a class="tab" href="<c:url value='/chat.do'/>" data-tab="chat" aria-label="채팅">
      <span class="tab-label">채팅</span>
    </a>
    <a class="tab" href="<c:url value='/mypage.do'/>" data-tab="me" aria-label="내 정보">
      <span class="tab-label">내정보</span>
    </a>
  </nav>
</footer>
