<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<!-- 페이지 전용 CSS/JS -->
<tiles:putAttribute name="pageHead">
	<link rel="stylesheet"
		href="<c:url value='/resources/css/articleForm.css'/>" />
</tiles:putAttribute>


<div class="container">
	<section id="main" class="board" aria-labelledby="boardTitle">
		<header class="board-header">
			<div>
				<h1 id="boardTitle" class="board-title">글쓰기</h1>
				<p class="desc">제목과 내용을 입력하고 이미지를 선택한 후 등록하세요.</p>
			</div>
			<div class="cta">
				<a class="btn ghost" href="<c:url value='/board/listArticles.do'/>">목록</a>
			</div>
		</header>


		<form id="articleForm" class="card"
			action="<c:url value='/board/addNewArticle.do'/>" method="post"
			enctype="multipart/form-data">
			<fieldset class="fieldset">
				<label for="title">제목</label> <input id="title" name="title"
					type="text" required maxlength="200" placeholder="제목을 입력하세요" />
			</fieldset>


			<fieldset class="fieldset">
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="10" required
					placeholder="본문을 입력하세요"></textarea>
			</fieldset>


			<fieldset class="fieldset">
				<label for="imageFileName">이미지 첨부 (선택)</label> <input
					id="imageFileName" name="imageFileName" type="file"
					accept="image/*" />
			</fieldset>


			<div class="form-actions">
				<button class="btn ghost" type="reset">초기화</button>
				<button class="btn primary" type="submit">등록</button>
			</div>
		</form>
	</section>
</div>