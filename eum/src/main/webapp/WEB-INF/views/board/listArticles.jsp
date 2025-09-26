<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/board.css'/>" />
<script defer src="<c:url value='/resources/js/board.js'/>"></script>

<!--
  기대하는 Model 속성
  - list : List<ArticleVO> — 필터 결과 목록
  - p    : Map<String,Object> — 선택 필터(값 유지용: p.svcType, p.region, p.urgency, p.q)
-->

<div class="board-list">

	<!-- ===== 필터 폼 ===== -->
	<form method="get" action="<c:url value='/board/listArticles.do'/>"
		class="filters">

		<!-- 유형 -->
		<label class="sr-only" for="svcType">유형</label> <select name="svcType"
			id="svcType">
			<option value="">유형 전체</option>
			<option value="동행" <c:if test="${p.svcType=='동행'}">selected</c:if>>동행</option>
			<option value="장보기" <c:if test="${p.svcType=='장보기'}">selected</c:if>>장보기</option>
			<option value="산책/보행"
				<c:if test="${p.svcType=='산책/보행'}">selected</c:if>>산책/보행</option>
			<option value="말벗" <c:if test="${p.svcType=='말벗'}">selected</c:if>>말벗</option>
			<option value="정리정돈"
				<c:if test="${p.svcType=='정리정돈'}">selected</c:if>>정리정돈</option>
			<option value="기타" <c:if test="${p.svcType=='기타'}">selected</c:if>>기타</option>
		</select>

		<!-- 지역 -->
		<label class="sr-only" for="region">지역</label> <select name="region"
			id="region">
			<option value="">지역 전체</option>
			<option value="서울" <c:if test="${p.region=='서울'}">selected</c:if>>서울</option>
			<option value="대전 동구"
				<c:if test="${p.region=='대전 동구'}">selected</c:if>>대전 동구</option>
			<option value="대전 중구"
				<c:if test="${p.region=='대전 중구'}">selected</c:if>>대전 중구</option>
			<option value="대전 서구"
				<c:if test="${p.region=='대전 서구'}">selected</c:if>>대전 서구</option>
			<option value="대전 유성구"
				<c:if test="${p.region=='대전 유성구'}">selected</c:if>>대전 유성구</option>
			<option value="대전 대덕구"
				<c:if test="${p.region=='대전 대덕구'}">selected</c:if>>대전 대덕구</option>
		</select>

		<!-- 긴급도 -->
		<label class="sr-only" for="urgency">긴급도</label> <select
			name="urgency" id="urgency">
			<option value="">긴급 전체</option>
			<option value="일반" <c:if test="${p.urgency=='일반'}">selected</c:if>>일반</option>
			<option value="긴급" <c:if test="${p.urgency=='긴급'}">selected</c:if>>긴급</option>
		</select>

		<!-- 검색어 -->
		<label class="sr-only" for="q">검색어</label> <input type="text" id="q"
			name="q" value="${p.q}" placeholder="제목/내용 검색" />

		<button class="btn primary">검색</button>
		<!-- 초기화 버튼(필요시) -->
		<a class="btn" href="<c:url value='/board/listArticles.do'/>">초기화</a>
	</form>

	<hr />

	<!-- ===== 목록 렌더 ===== -->
	<c:choose>
		<c:when test="${empty list}">
			<p class="muted">표시할 게시글이 없습니다.</p>
		</c:when>
		<c:otherwise>
			<ul class="articles">
				<c:forEach var="a" items="${list}" varStatus="st">
					<li class="item">
						<!-- 공지 배지 --> <c:if test="${a.isNotice}">
							<span class="badge">공지</span>
						</c:if> <!-- 제목: 상세 링크 --> <a class="title"
						href="<c:url value='/board/viewArticle.do'>
                        <c:param name='articleNO' value='${a.articleNO}'/>
                    </c:url>">
							<c:out value="${a.title}" />
					</a> <!-- 메타: 유형/지역/요청시각/긴급/포인트/작성일 -->
						<div class="meta">
							<c:if test="${not empty a.svcType}">
								<span class="chip">유형: <c:out value="${a.svcType}" /></span>
							</c:if>
							<c:if test="${not empty a.region}">
								<span class="chip">지역: <c:out value="${a.region}" /></span>
							</c:if>
							<c:if test="${not empty a.reqAt}">
								<span class="chip"> 요청시각: <fmt:formatDate
										value="${a.reqAt}" pattern="MM/dd HH:mm" />
								</span>
							</c:if>
							<c:if test="${not empty a.urgency}">
								<span class="chip">긴급도: <c:out value="${a.urgency}" /></span>
							</c:if>
							<c:if test="${a.points ne null}">
								<span class="chip">포인트: <c:out value="${a.points}" />P
								</span>
							</c:if>
							<span class="time"> <fmt:formatDate value="${a.writeDate}"
									pattern="yyyy-MM-dd" />
							</span>
						</div>

					</li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>

	<!-- 페이지네이션 UI -->
	<c:if test="${total > 0}">
		<nav class="pagination">
			<ul>
				<li class="${!hasPrev ? 'disabled' : ''}"><a
					href="<c:url value='/board/listArticles.do'>
                   <c:param name='page' value='${prevPage}'/>
                   <c:param name='size' value='${size}'/>
                 </c:url>">&laquo;</a>
				</li>

				<c:forEach var="i" begin="${page-5 > 1 ? page-5 : 1}"
					end="${page+5 < totalPages ? page+5 : totalPages}">
					<li class="${i == page ? 'active' : ''}"><a
						href="<c:url value='/board/listArticles.do'>
                     <c:param name='page' value='${i}'/>
                     <c:param name='size' value='${size}'/>
                   </c:url>">${i}</a>
					</li>
				</c:forEach>

				<li class="${!hasNext ? 'disabled' : ''}"><a
					href="<c:url value='/board/listArticles.do'>
                   <c:param name='page' value='${nextPage}'/>
                   <c:param name='size' value='${size}'/>
                 </c:url>">&raquo;</a>
				</li>
			</ul>
			<div class="pagestat">
				<span>${page} / ${totalPages}</span> <span>총 ${total}건</span>
			</div>
		</nav>
	</c:if>



	<div class="write-btn-box right">
		<a href="<c:url value='/board/articleForm.do'/>" class="btn primary">글쓰기</a>
	</div>
</div>

