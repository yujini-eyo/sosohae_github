<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 페이지 전용 CSS/JS (레이아웃에서 묶어서 넣는 구조면 tiles.xml에 등록해도 됩니다) -->
<link rel="stylesheet" href="<c:url value='/resources/css/main.css'/>" />

<div class="page-home">
  <section class="hero">
    <div class="container">
      <h1>🌞 따뜻한 마음이 연결되는 곳</h1>
      <p>작은 도움이 필요한 어르신과, 마음을 나누는 이웃을 이어드립니다.</p>
      <button class="cta" type="button" data-href="${ctx}/board/articleForm.do">도움 요청하러 가기</button>
    </div>
  </section>

  <section class="container feature-section">
    <div class="cards">
      <div class="feature-card"><span>🧓</span><h3>도움이 필요해요</h3><p>생활 속 어려움을 요청</p></div>
      <div class="feature-card"><span>🤝</span><h3>도와드릴게요</h3><p>여유로운 시간 나눔</p></div>
      <div class="feature-card"><span>💬</span><h3>감사 후기</h3><p>마음을 주고받는 이야기</p></div>
    </div>
  </section>

  <section class="container">
    <div class="cta-card">
      지금 주변 어르신에게 필요한 도움을 나눠보세요.<br>
      <button class="auth-btn" type="button" data-href="${ctx}/board/listArticles.do">+ 도와주기</button>
    </div>
  </section>

  <section class="container">
    <div class="recent-posts" id="recentPosts"
         data-endpoint="${ctx}/board/api/recent?limit=3">
      <!-- 스켈레톤 (첫 로딩 표시) -->
      <div class="post-card skeleton"></div>
      <div class="post-card skeleton"></div>
      <div class="post-card skeleton"></div>
    </div>
  </section>
</div>

<script defer src="<c:url value='/resources/js/main.js'/>"></script>
<script defer src="<c:url value='/resources/js/main.recent.js'/>"></script>
