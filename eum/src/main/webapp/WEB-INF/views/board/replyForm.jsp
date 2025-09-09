<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<!-- 현재 컨트롤러는 parentNO=0 으로 저장하므로, 답글 기능은 추후 구현 예정 -->
<tiles:putAttribute name="pageHead">
	<style>
.muted {
	color: #777;
	font-size: .95rem
}
</style>
</tiles:putAttribute>


<div class="container">
	<section class="board" aria-labelledby="replyTitle">
		<header class="board-header">
			<div>
				<h1 id="replyTitle" class="board-title">답글 작성</h1>
				<p class="desc muted">현재 버전은 일반 글쓰기와 저장 로직이 동일합니다.</p>
			</div>
			<div class="cta">
				<a class="btn ghost" href="<c:url value='/board/listArticles.do'/>">목록</a>
			</div>
		</header>


		<form class="card" action="<c:url value='/board/addNewArticle.do'/>"
			method="post" enctype="multipart/form-data">
			<input type="hidden" name="parentNO" value="${param.parentNO}" />


			<fieldset class="fieldset">
				<label for="title2">제목</label> <input id="title2" name="title"
					type="text" required maxlength="200" placeholder="제목을 입력하세요" />
			</fieldset>


			<fieldset class="fieldset">
				<label for="content2">내용</label>
				<textarea id="content2" name="content" rows="8" required
					placeholder="본문을 입력하세요"></textarea>
			</fieldset>


			<fieldset class="fieldset">
				<label for="image2">이미지 (선택)</label> <input id="image2"
					name="imageFileName" type="file" accept="image/*" />
			</fieldset>


			<div class="form-actions">
				<button class="btn ghost" type="reset">초기화</button>
				<button class="btn primary" type="submit">등록</button>
			</div>
		</form>
	</section>
</div>