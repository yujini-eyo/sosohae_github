<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tiles:putAttribute name="pageHead">
  <link rel="stylesheet" href="<c:url value='/resources/css/board.css'/>"/>
</tiles:putAttribute>

<tiles:putAttribute name="pageScripts">
  <script defer src="<c:url value='/resources/js/board.js'/>"></script>
</tiles:putAttribute>

<c:url var="imageUrl" value="/board/image.do">
  <c:param name="articleNO" value="${article.articleNO}"/>
  <c:param name="file"      value="${article.imageFileName}"/>
</c:url>

<c:url var="downloadUrl" value="/board/download.do">
  <c:param name="articleNO"     value="${article.articleNO}"/>
  <c:param name="imageFileName" value="${article.imageFileName}"/>
</c:url>

<div class="container">
  <section class="board" aria-labelledby="viewTitle">
    <header class="board-header">
      <div>
        <h1 id="viewTitle" class="board-title"><c:out value="${article.title}"/></h1>
        <p class="desc">
          글번호 <strong><c:out value="${article.articleNO}"/></strong> · 작성일
          <strong><fmt:formatDate value="${article.writeDate}" pattern="yyyy-MM-dd HH:mm"/></strong>
        </p>
      </div>
      <div class="cta">
        <c:if test="${not empty article.imageFileName}">
          <img src="${imageUrl}" alt="첨부 이미지" style="max-height:64px"/>
        </c:if>
        <a class="btn ghost" href="<c:url value='/board/listArticles.do'/>">목록</a>
      </div>
    </header>

    <article class="card" style="padding:20px">
      <div class="article-body" style="white-space:pre-wrap;line-height:1.7">
        <c:out value="${article.content}"/>
        
        <!-- 도와주기 버튼 -->
        <c:url var="applyUrl" value="/volunteer/apply.do">
  <c:param name="reqId" value="${article.articleNO}"/>
</c:url>

<!-- 도와주기 CTA -->
<div class="help-cta">
  <a class="btn help" href="${applyUrl}" role="button" aria-label="도와주기">도와주기</a>
</div>
       <!--  JSP – POST 전송형(바로 신청 처리) 바로 처리하는 엔드포인트가 있다면 이걸로 교체. -->
     <%--   <form action="<c:url value='/volunteer/apply.do'/>" method="post" class="help-cta">
  <input type="hidden" name="reqId" value="${article.articleNO}"/>
  <button type="submit" class="btn help" aria-label="도와주기">도와주기</button>
</form> --%>
       </div>

      <c:if test="${not empty article.imageFileName}">
        <div style="margin-top:16px">
          <img alt="첨부 이미지" style="max-width:100%;height:auto" src="${downloadUrl}"/>
        </div>
      </c:if>

      <footer class="article-actions" style="margin-top:24px;display:flex;gap:8px;justify-content:flex-end">
        <!-- 수정 -->
        <form action="<c:url value='/board/modArticle.do'/>" method="post" enctype="multipart/form-data"
              style="display:inline-flex;gap:8px;align-items:center">
          <input type="hidden" name="articleNO" value="${article.articleNO}"/>
          <input type="hidden" name="originalFileName" value="${article.imageFileName}"/>
          <input type="text" name="title" value="${article.title}" placeholder="제목" required style="min-width:240px"/>
          <textarea name="content" placeholder="내용" required style="min-width:240px">${article.content}</textarea>
          <input type="file" name="imageFile" accept="image/*"/>
          <button class="btn" type="submit">수정</button>
        </form>

        <!-- 삭제 -->
        <form action="<c:url value='/board/removeArticle.do'/>" method="post"
              onsubmit="return confirm('정말 삭제하시겠습니까?');">
          <input type="hidden" name="articleNO" value="${article.articleNO}"/>
          <button class="btn danger" type="submit">삭제</button>
        </form>
      </footer>
    </article>
  </section>
</div>
