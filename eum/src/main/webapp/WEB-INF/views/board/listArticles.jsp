<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<head>
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value='/resources/css/board.css'/>">
  <!-- JS: 반드시 c:url 사용, defer 권장 -->
  <script defer src="<c:url value='/resources/js/board.js'/>"></script>
</head>

<div class="container">
  <section id="main" class="board" aria-labelledby="boardTitle">
    <header class="board-header">
      <div>
        <h1 id="boardTitle" class="board-title">도움 매칭 게시판</h1>
        <p class="desc">이거 위로 어케 올려</p>
      </div>

      <nav class="tabs" role="tablist" aria-label="게시판 카테고리">
        <button class="tab-btn" role="tab" aria-selected="true"  aria-controls="panel-req"    id="tab-req"    data-tab="req">도움 요청</button>
        <button class="tab-btn" role="tab" aria-selected="false" aria-controls="panel-vol"    id="tab-vol"    data-tab="vol">봉사자 모집</button>
        <button class="tab-btn" role="tab" aria-selected="false" aria-controls="panel-notice" id="tab-notice" data-tab="notice">공지사항</button>
      </nav>

      <div class="toolbar" aria-label="검색 및 필터">
        <div class="filters">
          <label class="field" aria-label="검색어">🔎
            <input id="search" type="search" placeholder="제목, 내용, 작성자 검색" autocomplete="off" />
          </label>
          <label class="field" aria-label="서비스 유형">🧭
            <select id="svc">
              <option value="">전체 유형</option>
              <option value="hospital">동행</option>
              <option value="shopping">장보기</option>
              <option value="walk">산책/보행 보조</option>
              <option value="talk">말벗</option>
              <option value="clean">청소/정리</option>
            </select>
          </label>
          <label class="field" aria-label="지역">📍
            <select id="region">
              <option value="">전체 지역</option>
              <option>서울</option><option>부산</option><option>대구</option><option>인천</option>
              <option>광주</option><option>대전</option><option>울산</option><option>세종</option>
              <option>경기</option><option>강원</option><option>충북</option><option>충남</option>
              <option>전북</option><option>전남</option><option>경북</option><option>경남</option><option>제주</option>
            </select>
          </label>
          <label class="field" aria-label="긴급도">⏱️
            <select id="urgency">
              <option value="">전체 긴급도</option>
              <option value="urgent">긴급</option>
              <option value="normal">일반</option>
            </select>
          </label>
          <label class="field" aria-label="정렬">↕️
            <select id="sort">
              <option value="recent">최신순</option>
              <option value="views">조회순</option>
              <option value="title">제목순</option>
            </select>
          </label>
        </div>
        <div class="cta">
          <!-- 글쓰기 버튼 -->
		  <a class="btn primary" href="/eum/board/articleForm.do" style="text-decoration:none;">글쓰기</a>
		  
        </div>
      </div>
    </header>
    
    <!-- 봉사자 모집 -->
    <div id="panel-vol" role="tabpanel" aria-labelledby="tab-vol" hidden>
      <div class="table-wrap">
        <table aria-describedby="desc-vol">
          <caption id="desc-vol" class="sr-only" style="position:absolute;left:-9999px;">봉사자 모집 목록</caption>
          <thead>
            <tr>
              <th scope="col">번호</th>
              <th scope="col">제목</th>
              <th scope="col">봉사자</th>
              <th scope="col">가능지역</th>
              <th scope="col">가능시간대</th>
              <th scope="col">보유기술</th>
              <th scope="col">매칭</th>
              <th scope="col">조회</th>
              <th scope="col">액션</th>
            </tr>
          </thead>
          <tbody id="tbody-vol"></tbody>
        </table>
      </div>
      <nav class="pagination" id="pagination-vol" aria-label="봉사자 모집 페이지"></nav>
    </div>

    <!-- 공지사항 -->
    <div id="panel-notice" role="tabpanel" aria-labelledby="tab-notice" hidden>
      <div class="table-wrap">
        <table aria-describedby="desc-notice">
          <caption id="desc-notice" class="sr-only" style="position:absolute;left:-9999px;">공지사항 목록</caption>
          <thead>
            <tr>
              <th scope="col">번호</th>
              <th scope="col">제목</th>
              <th scope="col">작성일</th>
              <th scope="col">조회</th>
            </tr>
          </thead>
          <tbody id="tbody-notice"></tbody>
        </table>
      </div>
      <nav class="pagination" id="pagination-notice" aria-label="공지사항 페이지"></nav>
    </div>
  </section>
</div>
