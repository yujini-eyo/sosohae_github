<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/about.css'/>" />

<div class="page-about" role="region" aria-labelledby="aboutTitle">
  <div class="container">
    <!-- Hero -->
    <section class="hero" aria-labelledby="aboutTitle">
      <h1 id="aboutTitle">소소한 도움 – 서비스 소개</h1>
      <p class="subtitle">
        전구 교체, 약국 심부름, 가벼운 동행처럼 <strong>작지만 꼭 필요한 도움</strong>을
        <strong>근처 이웃</strong>과 빠르게 연결하는 지역 기반 매칭 플랫폼입니다.
      </p>
      <div class="cta" role="group" aria-label="빠른 액션">
        <button class="btn primary" type="button" data-href="${ctx}/help/write.do">도움 요청하기</button>
        <button class="btn ghost"   type="button" data-href="${ctx}/board/listArticles.do">요청 살펴보기</button>
      </div>
    </section>

    <!-- 1. 서비스 한눈에 -->
    <section id="intro" aria-labelledby="introTitle">
      <h2 id="introTitle">서비스 한눈에 보기</h2>
      <div class="cards">
        <article class="card">
          <h3>작은 도움의 연결</h3>
          <ul class="list">
            <li>생활 속 불편을 동네 이웃과 매칭</li>
            <li>카테고리: 동행·장보기·말벗·청소·간단수리 등</li>
            <li>소액 보상 또는 봉사 포인트 기반</li>
          </ul>
        </article>
        <article class="card">
          <h3>지역 단위 매칭</h3>
          <ul class="list">
            <li>구/동 단위 검색·추천</li>
            <li>리스트/카드/지도 보기 제공</li>
            <li>모바일 최적화 UI</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- 2. 왜 필요할까요 -->
    <section id="why" aria-labelledby="whyTitle">
      <h2 id="whyTitle">왜 필요할까요?</h2>
      <div class="cards">
        <article class="card">
          <h3>고립감/불편의 해소</h3>
          <ul class="list">
            <li>사소하지만 혼자 하기 어려운 일들</li>
            <li>비싼 전문 서비스 대신 근거리 이웃 도움</li>
          </ul>
        </article>
        <article class="card">
          <h3>지역 공동체 회복</h3>
          <ul class="list">
            <li>이웃 간 신뢰와 정서적 지지 형성</li>
            <li>봉사 문화 확산과 재참여 유도</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- 3. 핵심 기능 -->
    <section id="features" aria-labelledby="featTitle">
      <h2 id="featTitle">핵심 기능</h2>
      <div class="cards">
        <article class="card">
          <h3>간편 요청/신청</h3>
          <ul class="list">
            <li>사진·카테고리·지역·희망일시 입력</li>
            <li>도우미는 상세에서 신청/취소</li>
          </ul>
        </article>
        <article class="card">
          <h3>보기 모드 3종</h3>
          <ul class="list">
            <li>리스트 / 카드뷰(쇼핑몰형) / 지도(당근형)</li>
            <li>모바일 큰 버튼/큰 글자</li>
          </ul>
        </article>
        <article class="card">
          <h3>실시간 소통</h3>
          <ul class="list">
            <li>매칭된 쌍만 채팅 가능</li>
            <li>사진 공유·위치 전송·신고 지원</li>
          </ul>
        </article>
        <article class="card">
          <h3>평가/배지</h3>
          <ul class="list">
            <li>서로 별점 + 한줄평</li>
            <li>1365 인증·활동 횟수 배지</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- 4. 이렇게 연결됩니다 -->
    <section id="flow" aria-labelledby="flowTitle">
      <h2 id="flowTitle">이렇게 연결됩니다</h2>
      <div class="steps">
        <article class="step"><h3>1) 글 작성</h3><p>어르신이 필요한 도움을 간단히 등록</p></article>
        <article class="step"><h3>2) 신청</h3><p>도우미가 지원, 프로필/이력 확인</p></article>
        <article class="step"><h3>3) 매칭</h3><p>1명 수락 시 매칭 완료(다른 신청 자동 불가)</p></article>
        <article class="step"><h3>4) 수행/평가</h3><p>채팅·위치 공유로 진행, 완료 후 서로 평가</p></article>
      </div>
      <div class="box" style="margin-top:12px;">
        <ul class="list">
          <li>요청 제한: 하루 1건, 08:00~20:00 등록 권장</li>
          <li>‘비상 요청’은 상단 고정 + 관리자 알림</li>
        </ul>
      </div>
    </section>

    <!-- 5. 신뢰와 안전 -->
    <section id="trust" aria-labelledby="trustTitle">
      <h2 id="trustTitle">신뢰와 안전</h2>
      <div class="cards">
        <article class="card">
          <h3>본인/기관 인증</h3>
          <ul class="list">
            <li>휴대폰 문자 인증 필수</li>
            <li>도우미: 청소년/기관 인증(선택·권장), 1365 연동</li>
          </ul>
        </article>
        <article class="card">
          <h3>개인정보 최소화</h3>
          <ul class="list">
            <li>매칭 전 닉네임/대략 위치만 노출</li>
            <li>매칭 후에만 상세 정보 공유</li>
          </ul>
        </article>
        <article class="card">
          <h3>위험 대응</h3>
          <ul class="list">
            <li>채팅 신고·차단, SOS 알림</li>
            <li>위치 공유는 매칭/수행 시간 내 임시 권한</li>
          </ul>
        </article>
      </div>
    </section>

    <!-- 6. 누가 사용하나요 -->
    <section id="forwhom" aria-labelledby="whoTitle">
      <h2 id="whoTitle">누가 사용하나요</h2>
      <div class="cards">
        <article class="card">
          <h3>어르신/1인가구</h3>
          <p class="note">전구 교체·장보기·동행 등 생활 불편이 있는 분들</p>
        </article>
        <article class="card">
          <h3>도우미(이웃)</h3>
          <p class="note">시간을 나누고 싶은 주민, 청소년·대학생, 봉사자</p>
        </article>
        <article class="card">
          <h3>보호자</h3>
          <p class="note">대신 글 작성·진행 상황 열람(계정 연동)</p>
        </article>
        <article class="card">
          <h3>지역 기관</h3>
          <p class="note">복지관·청소년수련관 등 1365 등록 기관과 연계</p>
        </article>
      </div>
    </section>

    <!-- FAQ -->
    <section id="faq" aria-labelledby="faqTitle" style="margin:24px 0 40px;">
      <h2 id="faqTitle">자주 묻는 질문</h2>
      <details>
        <summary>비용이 드나요?</summary>
        <p>도움은 소액 보상 또는 봉사 포인트 기반입니다. 금전 요구가 과도하면 즉시 신고해주세요.</p>
      </details>
      <details>
        <summary>누구나 도우미가 될 수 있나요?</summary>
        <p>기본 가입과 문자 인증 후 가능합니다. 추가 인증(1365/청소년/기관)은 신뢰 향상에 도움이 됩니다.</p>
      </details>
      <details>
        <summary>위치 정보는 안전한가요?</summary>
        <p>위치 공유는 매칭/수행 시간 내에서만 임시로 허용되며, 만료 시 자동 중지됩니다.</p>
      </details>
    </section>

    <!-- CTA -->
    <section aria-label="바로 시작하기" style="text-align:center; margin-bottom:40px;">
      <button class="btn primary" type="button" data-href="${ctx}/help/write.do">지금 도움 요청하기</button>
      <button class="btn ghost"   type="button" data-href="${ctx}/board/listArticles.do">요청 목록 보기</button>
    </section>
  </div>
</div>

<script defer src="<c:url value='/resources/js/about.js'/>"></script>
