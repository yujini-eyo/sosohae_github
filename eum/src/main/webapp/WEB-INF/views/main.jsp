<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 페이지 전용 스타일 -->
<style>
/* SUIT 폰트 */
@import url('https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css');

/* ✅ 이 페이지 내부(.page-home)에서만 쓰는 테마 변수 */
.page-home{
  --bg:#FFF8F2; --text:#59463E; --muted:#806A5A;
  --primary:#F6A96D; --primary-600:#e98c45;
  --brand:#FFE8C2; --brand-200:#FADFCB;
  --card:#FFFFFF; --card-br:#FFE1CB;
  --hero-l:#FFEEE3; --hero-r:#FFF5E6;
  --cta:#FFF0D5; --footer:#FFECD6;
  --shadow: 0 8px 24px rgba(0,0,0,0.08);
  --shadow-soft: 0 4px 10px rgba(0,0,0,0.06);
  --radius: 20px; --maxw: 1100px;
  --focus: 0 0 0 3px rgba(246,169,109,.35);

  /* 🔧 섹션 간격 (여기만 조절하면 됩니다) */
  --section-gap: 96px;        /* 데스크톱/태블릿 */
  --section-gap-sm: 60px;     /* 모바일 */
}

/* ✅ 섹션 간격: 전역이 아니라 이 페이지 내부에서만 */
.page-home section + section{ margin-top: var(--section-gap); }
@media (max-width:600px){
  .page-home section + section{ margin-top: var(--section-gap-sm); }
}

/* 레이아웃 유틸 */
.page-home .container{
  max-width: var(--maxw);
  margin: 0 auto;
  width: 100%;
}

/* 링크/포커스도 이 페이지 안에서만 */
.page-home a{ color: inherit; text-decoration: none; }
.page-home a:focus, .page-home button:focus{
  outline: none; box-shadow: var(--focus); border-radius: 10px;
}

/* 히어로 */
.page-home .hero{
  background: linear-gradient(90deg, var(--hero-l), var(--hero-r));
  padding: 60px 0; text-align: center;
  border-bottom: 1px solid var(--brand-200);
  border-radius: var(--radius);
}
.page-home .hero h1{
  font-family: 'SUIT', system-ui, -apple-system, Segoe UI, Roboto, sans-serif;
  font-size: clamp(24px, 3vw, 34px);
  font-weight: 700;            /* 톤 다운 */
  margin-bottom: 10px;
  /* 필요하면 색도 더 밝게
  color: color-mix(in srgb, var(--text) 60%, white 40%);
  */
}
.page-home .hero p{ color: var(--muted); margin-bottom: 18px; }
.page-home .hero .cta{
  background: #FFB88A; color: #fff; border: 0;
  padding: 14px 22px; border-radius: 999px; font-weight: 700; cursor: pointer;
}
.page-home .hero .cta:hover{ background: #ffa06a; }

/* 특징 */
.page-home .feature-section{ padding: 32px 0; }
.page-home .cards{ display: grid; gap: 18px; grid-template-columns: repeat(12, 1fr); }
.page-home .feature-card{
  grid-column: span 4; background: #fff; border: 2px solid var(--card-br);
  border-radius: var(--radius); padding: 22px; text-align: center; box-shadow: var(--shadow-soft);
}

/* CTA 카드 */
.page-home .cta-card{
  background: var(--cta); margin: 36px 0; padding: 28px;
  border-radius: var(--radius); text-align: center;
}
.page-home .auth-btn{
  margin-top: 12px; background: var(--primary); color: #fff; border: 0;
  padding: 12px 18px; border-radius: 999px; font-weight: 800; cursor: pointer;
}
.page-home .auth-btn:hover{ background: var(--primary-600); }

/* 최근 글 */
.page-home .recent-posts{
  display: grid; gap: 16px; grid-template-columns: repeat(12, 1fr); margin-bottom: 12px;
}
.page-home .post-card{
  grid-column: span 4; background: #fff; border: 2px solid #FCE0CA;
  border-radius: var(--radius); padding: 18px; box-shadow: var(--shadow-soft);
}

/* 반응형 */
@media (max-width: 600px){
  .page-home .feature-card, .page-home .post-card{ grid-column: span 12; }
}

/* === spacing override: 더 넉넉한 간격 (이 파일 맨 아래 덮어쓰기) === */
.page-home{
  --section-gap: clamp(80px, 7vw, 110px);
  --section-gap-sm: 52px;
}
/* 헤더 아래 히어로 약간 띄우고, 내부 패딩 확대 */
.page-home .hero{
  margin-top: 12px;
  padding: clamp(64px, 8vw, 96px) 0;
}
/* 특징 섹션/그리드 간격 확대 */
.page-home .feature-section{ padding: 44px 0; }
.page-home .cards{ gap: 24px; }
.page-home .feature-card{ padding: 26px; }
/* CTA 카드 여백/패딩 확대 */
.page-home .cta-card{ margin: 52px 0; padding: 32px; }
/* 최근 글 카드 간격/마진 보정 */
.page-home .recent-posts{ gap: 20px; margin-bottom: 20px; }

/* 모바일 미세 조정 */
@media (max-width: 600px){
  .page-home .hero{ margin-top: 8px; padding: 52px 0; }
  .page-home .feature-section{ padding: 38px 0; }
  .page-home .cards{ gap: 18px; }
  .page-home .cta-card{ margin: 44px 0; padding: 28px; }
  .page-home .recent-posts{ gap: 16px; margin-bottom: 16px; }
}
</style>

<!-- 본문 시작 -->
<div class="page-home">
  <section class="hero">
    <div class="container">
      <h1>🌞 따뜻한 마음이 연결되는 곳</h1>
      <p>작은 도움이 필요한 어르신과, 마음을 나누는 이웃을 이어드립니다.</p>
      <button class="cta" type="button" onclick="location.href='${ctx}/help/write.do'">도움 요청하러 가기</button>
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
      <button class="auth-btn" type="button" onclick="location.href='${ctx}/board/listArticles.do'">+ 도와주기</button>
    </div>
  </section>

  <section class="container">
    <div class="recent-posts">
      <div class="post-card"><h4>🛒 마트 심부름</h4><p>서울 양천구 목동 · 300P</p></div>
      <div class="post-card"><h4>💊 약 타기 요청</h4><p>서울 성북구 길음동 · 200P</p></div>
      <div class="post-card"><h4>🪜 전등 교체</h4><p>서울 중랑구 망우동 · 150P</p></div>
    </div>
  </section>
</div>
<!-- 본문 끝 -->
