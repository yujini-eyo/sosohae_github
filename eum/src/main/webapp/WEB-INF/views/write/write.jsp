<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="cpath" value="${pageContext.request.contextPath}" />
<a href="${cpath}/write/list.do">목록</a>
<a href="${cpath}/write">글쓰기</a> <!-- /write, /write/ 모두 커버 -->

<!-- 여기부터 업로드한 board.html의 <body> 내부 핵심 콘텐츠를 옮겨 붙이면 됩니다. -->
<!-- 헤더/푸터/공통 링크는 base.jsp가 담당하니, 본문만 두면 돼요. -->

<section class="write" aria-labelledby="boardTitle">
  <header class="write-header">
    <h1 id="writeTitle">도움 매칭 게시판</h1>
    <p class="desc">요청/봉사/공지 탭으로 필요한 글을 찾아보세요.</p>

    <!-- 탭/필터/툴바 등: board.html의 마크업을 그대로 이동 -->
  </header>

  <!-- 예: panel-req/vol/notice 테이블들 -->
  <div id="panel-req" role="tabpanel" aria-labelledby="tab-req">
    <div class="table-wrap">
      <table>
        <colgroup>
          <col style="width:72px"><col><!-- 제목 auto -->
          <col style="width:96px"><col style="width:110px"><col style="width:110px">
          <col style="width:180px"><col style="width:90px"><col style="width:100px">
          <col style="width:88px"><col style="width:140px">
        </colgroup>
        <thead>...</thead>
        <tbody id="tbody-req"></tbody>
      </table>
    </div>
    <nav class="pagination" id="pagination-req"></nav>
  </div>

  <!-- panel-vol, panel-notice 도 동일하게 board.html에서 복사 -->
</section>

<!-- board.html의 <style>은 /resources/css/board.css 로, <script>는 /resources/js/board.js 로 분리 권장 -->
<link rel="stylesheet" href="${cpath}/resources/css/write.css"/>
<script src="${cpath}/resources/js/write.js"></script>
