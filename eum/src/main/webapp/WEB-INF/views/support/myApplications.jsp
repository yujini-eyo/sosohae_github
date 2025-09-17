<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/myApplications.css'/>" />
<script defer src="<c:url value='/resources/js/myApplications.js'/>"></script>

<div class="wrap">
  <h2 class="page-header">내 신청 내역</h2>
  <div class="card">
    <div class="app-list">
      <%--
        [더미 데이터]
        아래는 화면 디자인 확인을 위한 더미 데이터 목록입니다.
        (실제 배포 시에는 삭제하고 <c:forEach> 태그 사용)
      --%>
      <a href="#" class="app-item">
        <div class="app-item-header">
          <div class="app-title">마트 심부름 요청</div>
          <div class="app-status">선정됨</div>
        </div>
        <div class="app-content">시간 괜찮습니다. 오후 3시 이후로 가능해요.</div>
        <div class="app-meta">신청일시: 2025-09-15 10:30</div>
      </a>

      <a href="#" class="app-item">
        <div class="app-item-header">
          <div class="app-title">무거운 짐 옮기기 도와주세요!</div>
          <div class="app-status">신청</div>
        </div>
        <div class="app-content">
          안녕하세요. 저는 근처에 거주중인 학생입니다. 도와드릴게요!
        </div>
        <div class="app-meta">신청일시: 2025-09-14 16:50</div>
      </a>

      <a href="#" class="app-item">
        <div class="app-item-header">
          <div class="app-title">강아지 산책 도와주실 분 찾아요</div>
          <div class="app-status">거절됨</div>
        </div>
        <div class="app-content">안녕하세요. 강아지를 좋아해서 신청합니다!</div>
        <div class="app-meta">신청일시: 2025-09-13 09:20</div>
      </a>
    </div>
  </div>
</div>