<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<tiles:putAttribute name="pageHead">
	<!-- 필요 시 상세 페이지 전용 CSS 연결 -->
</tiles:putAttribute>


<div class="container">
	<section class="board" aria-labelledby="viewTitle">
		<header class="board-header">
			<div>
				<h1 id="viewTitle" class="board-title">
					<c:out value="${article.title}" />
				</h1>
				<p class="desc">
					글번호 <strong><c:out value="${article.articleNO}" /></strong> · 작성일
					<strong><fmt:formatDate value="${article.writeDate}"
							pattern="yyyy-MM-dd HH:mm" /></strong>
				</p>
			</div>
			<div class="cta">
				<a class="btn ghost" href="<c:url value='/board/listArticles.do'/>">목록</a>
			</div>
		</header>


		<article class="card" style="padding: 20px">
			<div class="article-body"
				style="white-space: pre-wrap; line-height: 1.7">
				<c:out value="${article.content}" />
			</div>


			<c:if test="${not empty article.imageFileName}">
				<div style="margin-top: 16px">
					<img alt="첨부 이미지" style="max-width: 100%; height: auto"
						src="<c:url value='/download.do'>
<c:param name='articleNO' value='${article.articleNO}'/>
<c:param name='imageFileName' value='${article.imageFileName}'/>
</c:url>" />
				</div>
			</c:if>


			<footer class="article-actions"
				style="margin-top: 24px; display: flex; gap: 8px; justify-content: flex-end">
				<!-- 수정 -->
				<form action="<c:url value='/board/modArticle.do'/>" method="post"
					enctype="multipart/form-data"
					style="display: inline-flex; gap: 8px; align-items: center">
					<input type="hidden" name="articleNO" value="${article.articleNO}" />
					<input type="text" name="title" value="${article.title}"
						placeholder="제목" required style="min-width: 240px" /> <input
						type="file" name="imageFileName" accept="image/*" />
					<button class="btn" type="submit">수정</button>
				</form>
				<!-- 삭제 -->
				<form action="<c:url value='/board/removeArticle.do'/>"
					method="post" onsubmit="return confirm('정말 삭제하시겠습니까?');">
					<input type="hidden" name="articleNO" value="${article.articleNO}" />
					<button class="btn danger" type="submit">삭제</button>
				</form>
			</footer>
		</article>
	</section>
</div>