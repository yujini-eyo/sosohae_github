<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%-- 
  viewArticle.jsp
  - 상세 페이지
  - 비로그인: 글만 보기 + "로그인하고 도와주기" 버튼 노출
  - 로그인 & 작성자 아님: "도와주기" 신청 폼 노출 (POST /support/apply.do)
  - 로그인 & 작성자: 같은 페이지 하단에 "지원자 목록" 표시 + "선정" 버튼 제공
  - 컨트롤러에서 article, (작성자일 경우) applicants, msg/err 플래시 메시지를 모델로 제공한다고 가정
--%>

<%-- 컨텍스트 경로 필요시 주석 해제하여 사용 가능
<c:set var="ctx" value="${pageContext.request.contextPath}" />
--%>

<!-- 분리된 CSS/JS -->
<link rel="stylesheet" href="<c:url value='/resources/css/viewArticle.css'/>" />
<script defer src="<c:url value='/resources/js/viewArticle.js'/>"></script>

<%-- 현재 로그인 사용자 ID (세션에 member가 없으면 null) --%>
<c:set var="me"
	value="${sessionScope.member != null ? sessionScope.member.id : null}" />

<%-- 플래시 메시지 영역: 등록/신청/선정 결과 안내 --%>
<c:if test="${not empty msg}">
	<div class="alert success" role="status" style="margin-bottom: 12px">${msg}</div>
</c:if>
<c:if test="${not empty err}">
	<div class="alert danger" role="alert" style="margin-bottom: 12px">${err}</div>
</c:if>

<section class="article-view">
	<%-- 1) 글 헤더(제목/메타)와 3) 본문(개행 보존)을 감싸는 새로운 컨테이너 추가 --%>
	<div class="article-body-container">
		<header class="article-header">
			<h1 style="margin: 0 0 8px 0">
				<c:out value="${article.title}" />
			</h1>
			<div class="meta" style="color: #666; font-size: 0.9rem">
				글번호 #
				<c:out value="${article.articleNO}" />
				· 작성자 <strong><c:out value="${article.id}" /></strong> · 작성일
				<fmt:formatDate value="${article.writeDate}"
					pattern="yyyy-MM-dd HH:mm" />
			</div>
		</header>

		<%-- 2) 첨부 이미지(있을 때만) --%>
		<c:if test="${not empty article.imageFileName}">
			<figure style="margin: 16px 0">
				<img alt="첨부 이미지"
					style="max-width: 100%; height: auto; border-radius: 12px"
					src="<c:url value='/board/download.do'>
											<c:param name='imageFileName' value='${article.imageFileName}'/>
											<c:param name='articleNO' value='${article.articleNO}'/>
										</c:url>" />
			</figure>
		</c:if>

<%-- 		<article class="article-content"
			style="white-space: pre-wrap; line-height: 1.6; margin: 16px 0 24px">
			<c:out value="${article.content}" />
		</article>
	</div> --%>

	<%-- 3) 본문 (개행 보존) --%>
	<article class="article-content"
		style="white-space: pre-wrap; line-height: 1.6; margin: 16px 0 24px">
		<c:out value="${article.content}" />
	</article>

	<%-- 4) 상단 액션 바: 목록/로그인유도/도와주기폼/작성자 버튼 --%>
	<div class="actions"
		style="display: flex; gap: 8px; flex-wrap: wrap; margin-top: 12px">

		<%-- 4-1) 모두에게 보이는 "목록" 버튼 --%>
		<a class="btn ghost" href="<c:url value='/board/listArticles.do'/>">목록</a>

		<%-- 4-2) 비로그인: 로그인 유도 버튼만 노출 --%>
		<c:if test="${me == null}">
			<a class="btn primary" href="<c:url value='/member/loginForm.do'/>">
				로그인하고 도와주기 </a>
		</c:if>

		<%-- 4-3) 로그인 & 작성자 아님: 도와주기 신청 폼(POST /support/apply.do) --%>
		<c:if test="${me != null and me ne article.id}">
			<form action="<c:url value='/support/apply.do'/>" method="post"
				style="display: flex; gap: 8px; flex-wrap: wrap">
				<%-- 필수: 어떤 글에 대한 신청인지 --%>
				<input type="hidden" name="articleNO" value="${article.articleNO}" />
				<%-- 선택: 지원자가 남기는 메시지 --%>
				<input type="text" name="message" placeholder="간단한 메세지(선택)"
					style="min-width: 260px; padding: 8px 10px; border: 1px solid #ddd; border-radius: 8px" />
				<button type="submit" class="btn help">도와주기</button>
			</form>
		</c:if>

		<%-- 4-4) 로그인 & 작성자: 별도 페이지 이동 버튼(선택) --%>
		<c:if test="${me != null and me eq article.id}">
			<a class="btn secondary"
				href="<c:url value='/support/applicants.do'>
                                        <c:param name='articleNO' value='${article.articleNO}'/>
                                      </c:url>">
				지원자 목록(별도 페이지) </a>
		</c:if>
	</div>

	<%-- 5) (핵심) 작성자에게 "같은 페이지"에서 지원자 목록 바로 보여주기
        - 컨트롤러에서 me==article.id 일 때 applicants 를 모델에 담아온다고 가정
        - applicants 가 비어도 안내 문구 출력
  --%>
	<c:if test="${me != null and me eq article.id}">
		<section class="applicant-list" style="margin-top: 24px">
			<h2 style="font-size: 1.1rem; margin: 0 0 12px">지원자 목록</h2>

			<c:choose>
				<c:when test="${empty applicants}">
					<div
						style="padding: 12px; border: 1px dashed #ddd; border-radius: 8px; color: #666">
						아직 지원자가 없습니다.</div>
				</c:when>
				<c:otherwise>
					<table class="tbl" style="width: 100%; border-collapse: collapse">
						<thead>
							<tr>
								<th
									style="text-align: left; border-bottom: 1px solid #eee; padding: 8px">지원자</th>
								<th
									style="text-align: left; border-bottom: 1px solid #eee; padding: 8px">메시지</th>
								<th
									style="text-align: left; border-bottom: 1px solid #eee; padding: 8px">상태</th>
								<th
									style="text-align: left; border-bottom: 1px solid #eee; padding: 8px">신청일</th>
								<th
									style="text-align: left; border-bottom: 1px solid #eee; padding: 8px">액션</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="app" items="${applicants}">
								<tr>
									<td style="border-bottom: 1px solid #f5f5f5; padding: 8px">
										<%-- 지원자 아이디 --%> <c:out value="${app.volunteerId}" />
									</td>
									<td style="border-bottom: 1px solid #f5f5f5; padding: 8px">
										<%-- 지원자가 남긴 메시지 --%> <c:out value="${app.message}" />
									</td>
									<td style="border-bottom: 1px solid #f5f5f5; padding: 8px">
										<%-- APPLIED / SELECTED / REJECTED / WITHDRAWN --%> <c:out
											value="${app.status}" />
									</td>
									<td style="border-bottom: 1px solid #f5f5f5; padding: 8px">
										<%-- 신청일시 --%> <fmt:formatDate value="${app.createdAt}"
											pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td style="border-bottom: 1px solid #f5f5f5; padding: 8px">
										<%-- 상태에 따른 액션: APPLIED만 "선정" 버튼 노출 --%> <c:choose>
											<c:when test="${app.status eq 'APPLIED'}">
												<form action="<c:url value='/support/select.do'/>"
													method="post" style="display: inline">
													<%-- 어떤 신청(application)을 선정하는지 --%>
													<input type="hidden" name="applicationId"
														value="${app.applicationId}" />
													<button type="submit" class="btn primary"
														style="padding: 6px 10px">선정</button>
												</form>
											</c:when>
											<c:when test="${app.status eq 'SELECTED'}">
												<span class="badge"
													style="padding: 6px 10px; border: 1px solid #ddd; border-radius: 8px">
													선정됨 </span>
											</c:when>
											<c:otherwise>
												<span style="color: #999">-</span>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>
		</section>
	</c:if>
</section>
