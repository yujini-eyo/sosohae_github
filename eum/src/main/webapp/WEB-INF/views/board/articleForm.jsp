<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 분리된 CSS/JS -->
<link rel="stylesheet" href="<c:url value='/resources/css/articleForm.css'/>" />
<script defer src="<c:url value='/resources/js/articleForm.js'/>"></script>

<!-- 본문만: Tiles 레이아웃이 <html> 헤더/푸터를 감쌉니다 -->
<main class="wrap">
  <h1>도움 요청</h1>

  <!-- 글등록 폼 (ArticleVO 매핑: title, content, imageFileName) -->
  <form id="articleForm" method="post" action="<c:url value='/board/addNewArticle.do'/>" enctype="multipart/form-data">

    <!-- ===== 위저드 카드 ===== -->
    <section class="card" id="wizard">

      <!-- 1. 무엇을 도와드릴까요? -->
      <div class="hd" data-step="1" tabindex="0">
        <span class="stepnum">1</span>
        <span class="label">무엇을 도와드릴까요?</span>
        <span class="hint">클릭하여 선택하세요.</span>
      </div>
      <div class="bd step active" data-step="1">
        <p class="kicker">한 가지를 선택하세요.</p>
        <div class="grid cols-3" id="typeBtns">
          <button type="button" class="btn" data-val="동행">동행</button>
          <button type="button" class="btn" data-val="말벗">말벗</button>
          <button type="button" class="btn" data-val="장보기">장보기</button>
          <button type="button" class="btn" data-val="정리정돈">정리정돈</button>
          <button type="button" class="btn" data-val="산책/보행">산책/보행</button>
          <button type="button" class="btn" data-val="기타">기타</button>
        </div>
      </div>

      <!-- 2. 어디에서 필요하신가요? -->
      <div class="hd" data-step="2" tabindex="0">
        <span class="stepnum">2</span>
        <span class="label">어디에서 필요하신가요?</span>
        <span class="hint">클릭하여 선택하세요.</span>
      </div>
      <div class="bd step" data-step="2">
        <p class="kicker">대전 지역구를 선택하세요.</p>
        <div class="grid cols-4" id="regionBtns">
          <button class="btn" data-val="서울">서울</button><button class="btn" data-val="경기">경기</button>
          <button class="btn" data-val="인천">인천</button><button class="btn" data-val="강원">강원</button>
          <button class="btn" data-val="충북">충북</button><button class="btn" data-val="충남">충남</button>
          <button class="btn" data-val="대전">대전</button><button class="btn" data-val="전북">전북</button>
          <button class="btn" data-val="전남">전남</button><button class="btn" data-val="광주">광주</button>
          <button class="btn" data-val="경북">경북</button><button class="btn" data-val="경남">경남</button>
          <button class="btn" data-val="대구">대구</button><button class="btn" data-val="부산">부산</button>
          <button class="btn" data-val="울산">울산</button><button class="btn" data-val="제주">제주</button>
        </div>
      </div>

      <!-- 3. 언제 도와드릴까요? -->
      <div class="hd" data-step="3" tabindex="0">
        <span class="stepnum">3</span>
        <span class="label">언제 도와드릴까요?</span>
        <span class="hint">클릭하여 선택하세요.</span>
      </div>
      <div class="bd step" data-step="3">
        <p class="kicker">날짜와 시간을 버튼으로 고르세요.</p>
        <div class="grid cols-3" id="dayBtns"></div>
        <div style="height:8px"></div>
        <div class="grid cols-4" id="timeBtns">
          <button class="btn" data-val="오전 9시">오전 9시</button>
          <button class="btn" data-val="오전 10시">오전 10시</button>
          <button class="btn" data-val="오전 11시">오전 11시</button>
          <button class="btn" data-val="오후 1시">오후 1시</button>
          <button class="btn" data-val="오후 2시">오후 2시</button>
          <button class="btn" data-val="오후 3시">오후 3시</button>
          <button class="btn" data-val="오후 4시">오후 4시</button>
          <button class="btn" data-val="오후 5시">오후 5시</button>
        </div>
      </div>

      <!-- 4. 긴급도 -->
      <div class="hd" data-step="4" tabindex="0">
        <span class="stepnum">4</span>
        <span class="label">긴급도</span>
        <span class="hint">클릭하여 선택하세요.</span>
      </div>
      <div class="bd step" data-step="4">
        <div class="grid cols-3" id="urgBtns">
          <button class="btn" data-val="일반">일반</button>
          <button class="btn" data-val="긴급">긴급</button>
        </div>
      </div>

      <!-- 5. 확인 / 제목 -->
      <div class="hd" data-step="5" tabindex="0">
        <span class="stepnum">5</span>
        <span class="label">확인 / 제목(선택)</span>
        <span class="hint">클릭하여 선택하세요.</span>
      </div>
      <div class="bd step" data-step="5">
        <p class="kicker">자동으로 제목이 만들어집니다. 한 단어 정도 추가할 수 있어요(선택).</p>
        <div class="field">
          <!-- ArticleVO.title 에 그대로 매핑 -->
          <input type="text" id="title" name="title" placeholder="자동 생성된 제목" />
          <button class="btn" id="regen" type="button">제목 다시 만들기</button>
        </div>
        <p class="tiny">연락처/개인정보는 프로필을 사용합니다. 글 등록 후 채팅으로 연결됩니다.</p>
      </div>
    </section>

    <!-- 요약 카드 -->
    <section class="card" style="margin-top:14px">
      <div class="hd" tabindex="-1" style="cursor:default">
        <span class="stepnum">✓</span>
        <span class="label">요약</span>
      </div>
      <div class="bd">
        <div class="sum" id="summary">
          <span class="chip">유형: -</span>
          <span class="chip">지역: -</span>
          <span class="chip">날짜: -</span>
          <span class="chip">시간: -</span>
          <span class="chip">긴급도: 일반</span>
          <span class="chip">권장 포인트: 100 P</span>
        </div>
        <div class="tiny" style="margin-top:8px">기준 100P/시간 · 긴급 시 +30%</div>
      </div>
    </section>

    <!-- 상세 내용 + 이미지 업로드 (ArticleVO.content, imageFileName) -->
    <section class="card" style="margin-top:14px">
      <div class="hd" tabindex="-1" style="cursor:default">
        <span class="stepnum">✎</span>
        <span class="label">상세 내용</span>
      </div>
      <div class="bd">
        <!-- 아래 textarea는 사용자가 쓰는 상세내용 -->
        <textarea id="contentDetail" rows="8" placeholder="상세 요청 내용을 작성해 주세요. 예) 병원 동행, 접수/수납 도움, 예상 소요 1시간 등"></textarea>

        <!-- 서버 제출용 필드들 -->
        <textarea id="content" name="content" hidden></textarea>
        <input type="file" name="imageFile" accept="image/*" />
      </div>
    </section>

    <!-- 하단 고정 바 -->
    <div class="dock">
      <div class="bar">
        <div class="tiny">단계 진행: <b id="prog">1 / 5</b></div>
        <div>
          <button class="cta secondary" type="button" id="prev">이전</button>
          <button class="cta primary"   type="button" id="next">다음</button>
          <button class="cta primary hide" type="button" id="submit">등록하기</button>
        </div>
      </div>
    </div>
  </form>
</main>

