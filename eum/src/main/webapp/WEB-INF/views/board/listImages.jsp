<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<tiles:putAttribute name="pageHead">
	<style>
.wrapper {
	max-width: 1100px;
	margin: 0 auto
}

.grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 20px;
	margin: 40px 0
}

.item {
	width: 100%;
	aspect-ratio: 4/3;
	border: 1px solid #eee;
	border-radius: 12px;
	overflow: hidden
}

.item img {
	width: 100%;
	height: 100%;
	object-fit: cover;
	display: block
}
</style>
</tiles:putAttribute>


<div class="wrapper">
	<h1 style="margin: 16px 0">이미지 목록</h1>
	<div class="grid">
		<c:forEach var="img" items="${imageFileList}">
			<div class="item">
				<img alt="이미지"
					src="<c:url value='/download.do'>
<c:param name='articleNO' value='${img.articleNO}'/>
<c:param name='imageFileName' value='${img.imageFileName}'/>
</c:url>">
			</div>
		</c:forEach>
	</div>
	<p>
		<a class="btn" href="<c:url value='/board/listArticles.do'/>">목록으로</a>
	</p>
</div>