<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/myRequests.css'/>" />
<script defer src="<c:url value='/resources/js/myRequests.js'/>"></script>

<div class="wrap">
	<h2 class="page-header">내 요청</h2>
  <div class="card">
    <div class="req-list">
      <%--
        [더미 데이터]
        아래는 화면 디자인 확인을 위한 더미 데이터 목록입니다.
        (실제 배포 시에는 삭제하고 <c:forEach> 태그 사용)
      --%>
      <a href="#" class="req-item">
        <div class="req-item-header">
          <div class="req-title">전등 교체 도와주실 분 구해요</div>
          <div class="req-applicants">지원자 <span class="req-applicants-count">3명</span></div>
        </div>
        <div class="req-meta">작성일: 2025-09-16 11:00</div>
      </a>

      <a href="#" class="req-item">
        <div class="req-item-header">
          <div class="req-title">마을 행사 봉사자 구합니다.</div>
          <div class="req-applicants">지원자 <span class="req-applicants-count selected">1명 선정</span></div>
        </div>
        <div class="req-meta">작성일: 2025-09-15 14:00</div>
      </a>

      <div class="req-item">
        <div class="req-item-header">
          <div class="req-title">제발 다 되어라</div>
          <div class="req-applicants">지원자 <span class="req-applicants-count">0명</span></div>
        </div>
        <div class="req-meta">작성일: 2025-09-14 18:30</div>
      </div>
    </div>
  </div>
</div>