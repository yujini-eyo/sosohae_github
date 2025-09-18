<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/viewArticle.css'/>" />
<link rel="stylesheet" href="<c:url value='/resources/css/viewArticle2.css'/>" />
<link rel="stylesheet" href="<c:url value='/resources/css/viewArticle3.css'/>" />

<%-- <script defer src="<c:url value='/resources/js/board.js'/>"></script> --%>

<!--
  기대하는 Model 속성
  - article : ArticleVO (id, title, content, writeDate, svcType, region, reqAt, urgency, points, ...)
  - me      : 로그인 사용자 id (String) — 컨트롤러에서 넣어줌
  - alreadyApplied : boolean — 나(me)가 이 글에 이미 지원했는지
  - applicants : List<SupportApplicationVO> — 글 작성자일 때만 내려옴
  - msg     : 플래시 메시지 (선택)
-->
<div class="board-view">

	<!-- 플래시 메시지 -->
	<c:if test="${not empty msg}">
		<div class="alert">${msg}</div>
	</c:if>

	<!-- 글 헤더: 제목/메타 정보 -->
	<header class="article-hd">
		<h1 class="title">
			<c:out value="${article.title}" />
			<c:if test="${article.isNotice}">
				<span class="badge">공지</span>
			</c:if>
		</h1>
		<p class="meta">
			작성자: <b><c:out value="${article.id}" /></b> 
			· 등록일:
			<fmt:formatDate value="${article.writeDate}"
				pattern="yyyy-MM-dd HH:mm" />
			<c:if test="${not empty article.svcType}">
        · 유형: <c:out value="${article.svcType}" />
			</c:if>
			<c:if test="${not empty article.region}">
        · 지역: <c:out value="${article.region}" />
			</c:if>
			<c:if test="${not empty article.reqAt}">
        · 요청시각: <fmt:formatDate value="${article.reqAt}"
					pattern="yyyy-MM-dd HH:mm" />
			</c:if>
			<c:if test="${not empty article.urgency}">
        · 긴급도: <c:out value="${article.urgency}" />
			</c:if>
			<c:if test="${article.points ne null}">
        · 권장 포인트: <c:out value="${article.points}" />P
      </c:if>
		</p>
	</header>

	<!-- 본문: 줄바꿈 보존 + XSS 방지 -->
	<article class="article-body" style="white-space: pre-wrap">
		<c:out value="${article.content}" />
	</article>

	<hr />

	<!-- ===== 지원하기 영역 ===== -->
	<!-- 내가 작성자가 아니면 지원과 관련한 영역을 보여준다 -->
	<c:if test="${article.id ne me}">
		<section class="apply-area">
			<h3>지원하기</h3>

			<!-- 이미 지원한 경우 안내만 -->
			<c:if test="${alreadyApplied}">
				<p class="muted">이미 이 글에 지원했습니다.</p>
			</c:if>

			<!-- 아직 지원하지 않았다면 지원 폼 노출 -->
			<c:if test="${not alreadyApplied}">
				<form action="<c:url value='/support/apply.do'/>" method="post"
					class="apply-form">
					<!-- 필수: 글 번호 전달 -->
					<input type="hidden" name="articleNO" value="${article.articleNO}" />

					<!-- 선택: 지원 메시지 -->
					<label for="applyMessage" class="sr-only">지원 메시지</label>
					<textarea id="applyMessage" name="message" rows="3"
						placeholder="간단한 자기소개/가능 시간 등을 적어주세요."></textarea>

					<div class="form-actions">
						<button type="submit" class="btn primary">지원하기</button>
						<a class="btn" href="<c:url value='/board/listArticles.do'/>">목록</a>
					</div>
				</form>
			</c:if>
		</section>
	</c:if>

	<!-- ===== 신청자 목록(작성자 전용) ===== -->
	<c:if test="${article.id eq me}">
		<section class="applicants-area">
			<h3>신청자 목록</h3>

			<!-- 신청자 없을 때 -->
			<c:if test="${empty applicants}">
				<p class="muted">아직 신청자가 없습니다.</p>
			</c:if>

			<!-- 신청자 카드 반복 -->
			<c:forEach var="app" items="${applicants}">
				<div class="card applicant">
					<div class="row">
						<div>
							<b>신청자</b> :
							<c:out value="${app.volunteerId}" />
						</div>
						<div>
							<b>상태</b> :
							<c:out value="${app.status}" />
						</div>
						<div>
							<b>신청일</b> :
							<fmt:formatDate value="${app.createdAt}"
								pattern="yyyy-MM-dd HH:mm" />
						</div>
					</div>

					<!-- 메시지 본문: 줄바꿈 보존 -->
					<c:if test="${not empty app.message}">
						<pre class="msg" style="white-space: pre-wrap">
							<c:out value="${app.message}" />
						</pre>
					</c:if>

					<!-- 상태 변경 버튼들 -->
					<div class="actions">
						<!-- 선정 -->
						<form action="<c:url value='/support/select.do'/>" method="post"
							style="display: inline">
							<input type="hidden" name="applicationId"
								value="${app.applicationId}" /> <input type="hidden"
								name="articleNO" value="${article.articleNO}" />
							<button class="btn">선정</button>
						</form>

						<!-- 거절 -->
						<form action="<c:url value='/support/reject.do'/>" method="post"
							style="display: inline">
							<input type="hidden" name="applicationId"
								value="${app.applicationId}" /> <input type="hidden"
								name="articleNO" value="${article.articleNO}" />
							<button class="btn">거절</button>
						</form>
					</div>
				</div>
			</c:forEach>
		</section>
	</c:if>

</div>

