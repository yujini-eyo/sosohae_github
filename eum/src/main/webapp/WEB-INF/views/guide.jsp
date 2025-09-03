<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/guide.css'/>" />

<div class="page-guide" role="region" aria-labelledby="guideTitle">
  <div class="container">
    <!-- Hero -->
    <section class="hero" aria-labelledby="guideTitle">
      <h1 id="guideTitle">소소한 도움 – 이용방법 가이드</h1>
      <p class="subtitle">
        전구 교체, 약국 심부름, 병원 동행처럼 <strong>작지만 꼭 필요한 도움</strong>을
        <strong>근처 이웃</strong>과 연결하는 방법을 단계별로 안내합니다.
      </p>
      <%-- 빠른 액션 CTA 제거됨 --%>
    </section>

    <%-- ✅ TOC(목차) 완전히 제거되었습니다. --%>

    <!-- 1. 시작하기 -->
    <section id="start" aria-labelledby="startTitle">
      <h2 id="startTitle">1) 시작하기 – 회원 유형 선택</h2>
      <div class="cards">
        <article class="card">
          <h3>어르신(요청자)</h3>
          <ul class="list">
            <li>필수: <strong>보호자 전화</strong>, <strong>비상 연락망</strong></li>
            <li>선택: 특이사항(병력/복용약)</li>
            <li><span class="badge">문자 본인인증</span> 후 글 작성 가능</li>
          </ul>
          <div class="card-cta">
            <button class="btn outline" type="button" data-href="${ctx}/member/memberForm.do">회원가입</button>
          </div>
        </article>
        <article class="card">
          <h3>도우미(봉사자)</h3>
          <ul class="list">
            <li>선택: <strong>1365 연동</strong>, 청소년/기관 인증</li>
            <li>활동 이력과 <span class="badge">배지</span>가 프로필에 반영</li>
          </ul>
          <div class="card-cta">
            <button class="btn outline" type="button" data-href="${ctx}/member/memberForm.do">회원가입</button>
          </div>
        </article>
      </div>
    </section>

    <!-- 2. 어르신 플로우 -->
    <section id="elder" aria-labelledby="elderTitle">
      <h2 id="elderTitle">2) 어르신 – 도움 요청하기</h2>
      <div class="steps">
        <article class="step"><h3>1) 글 작성</h3><p>카테고리/제목/지역/희망일시/포인트(선택)/사진(선택) 입력</p></article>
        <article class="step"><h3>2) 등록</h3><p>등록 후 도우미의 신청을 기다립니다(‘비상 요청’은 상단 고정)</p></article>
        <article class="step"><h3>3) 신청자 확인</h3><p>프로필·활동 이력을 보고 1명을 선택해 수락</p></article>
        <article class="step"><h3>4) 진행</h3><p>채팅/전화로 일정 조율 → 완료 후 서로 평가</p></article>
      </div>
      <div class="box hint">
        <ul class="list">
          <li>하루 <strong>1건</strong>만 등록 가능(중복/스팸 방지)</li>
          <li>권장 작성 시간: <strong>08:00~20:00</strong></li>
        </ul>
      </div>
    </section>

    <!-- 3. 도우미 플로우 -->
    <section id="helper" aria-labelledby="helperTitle">
      <h2 id="helperTitle">3) 도우미 – 신청하기</h2>
      <div class="steps">
        <article class="step"><h3>1) 목록 확인</h3><p>리스트/카드/지도 보기에서 내 주변 요청을 탐색</p></article>
        <article class="step"><h3>2) 상세 열람</h3><p>제목·세부내용·희망일시·지역·포인트 확인</p></article>
        <article class="step"><h3>3) 신청/취소</h3><p>신청 후 사정이 바뀌면 취소 가능</p></article>
        <article class="step"><h3>4) 매칭</h3><p>수락되면 채팅 가능, 다른 도우미는 신청 불가</p></article>
      </div>
    </section>

    <!-- 4. 매칭/진행 -->
    <section id="match" aria-labelledby="matchTitle">
      <h2 id="matchTitle">4) 매칭 · 진행 · 완료</h2>
      <div class="cards">
        <article class="card">
          <h3>매칭</h3>
          <ul class="list">
            <li>어르신이 <strong>1명 수락</strong> 시 <span class="badge">MATCHED</span></li>
            <li>다른 신청은 자동으로 불가 처리</li>
          </ul>
        </article>
        <article class="card">
          <h3>진행/완료</h3>
          <ul class="list">
            <li>예정 시간에 방문·수행</li>
            <li>완료 시 <span class="badge">CLOSED</span>, 취소 시 <span class="badge">CANCELLED</span></li>
          </ul>
        </article>
        <article class="card">
          <h3>평가/배지</h3>
          <ul class="list">
            <li>서로 별점(1~5) + 한줄평 1회</li>
            <li>활동 횟수·1365 인증 배지 부여</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- 5. 채팅/위치 -->
    <section id="chat" aria-labelledby="chatTitle">
      <h2 id="chatTitle">5) 채팅 · 위치 공유</h2>
      <div class="cards">
        <article class="card">
          <h3>채팅</h3>
          <ul class="list">
            <li><strong>매칭된 쌍</strong>만 이용 가능</li>
            <li>사진 공유, 신고/차단 제공</li>
          </ul>
        </article>
        <article class="card">
          <h3>위치 공유</h3>
          <ul class="list">
            <li>매칭 시 + 수행 시간 내 <strong>임시 권한</strong></li>
            <li>만료 시 자동 중지</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- 6. 정책/안내 -->
    <section id="policy" aria-labelledby="policyTitle">
      <h2 id="policyTitle">6) 작성 제한 · 안전 안내</h2>
      <div class="cards">
        <article class="card">
          <h3>작성 제한</h3>
          <ul class="list">
            <li>하루 1건 요청(어르신 기준)</li>
            <li>작성 권장 시간: 08:00~20:00</li>
            <li>‘비상 요청’은 상단 우선 노출 + 관리자 알림</li>
          </ul>
        </article>
        <article class="card">
          <h3>안전 수칙</h3>
          <ul class="list">
            <li>금전 요구는 즉시 신고, 플랫폼 내 채팅 우선</li>
            <li>개인정보는 매칭 후 필요한 범위에 한해 공유</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- FAQ -->
    <section id="faq" aria-labelledby="faqTitle" style="margin:24px 0 40px;">
      <h2 id="faqTitle">자주 묻는 질문</h2>
      <details>
        <summary>요청은 하루 몇 번까지 가능한가요?</summary>
        <p>중복/스팸 방지를 위해 <strong>1일 1건</strong>만 가능합니다. 긴급한 상황은 ‘비상 요청’을 사용해 주세요.</p>
      </details>
      <details>
        <summary>도우미 인증은 꼭 필요한가요?</summary>
        <p>기본 가입과 문자 인증으로 활동 가능하며, 1365·청소년·기관 인증을 추가하면 신뢰도가 올라갑니다.</p>
      </details>
      <details>
        <summary>위치 정보는 안전한가요?</summary>
        <p>위치 공유는 매칭/수행 시간 내에만 임시로 허용되며, 만료 시 자동 중지됩니다.</p>
      </details>
    </section>

    <!-- CTA (원하면 유지/삭제 선택 가능) -->
    <section aria-label="바로 시작하기" style="text-align:center; margin-bottom:40px;">
      <button class="btn primary" type="button" data-href="${ctx}/help/write.do">지금 도움 요청하기</button>
      <button class="btn ghost"   type="button" data-href="${ctx}/board/listArticles.do">요청 목록 보기</button>
    </section>
  </div>
</div>

<script defer src="<c:url value='/resources/js/guide.js'/>"></script>
