<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="a" value="${article}" />

<style>
  .meta{color:#666; font-size:13px; margin-bottom:10px;}
  .actions{margin-top:20px; display:flex; gap:8px;}
  .btn{display:inline-block; padding:8px 14px; border:1px solid #ddd; border-radius:8px; text-decoration:none; color:#333;}
  .btn.primary{background:#f6a96d; color:#fff; border-color:#f6a96d;}
  .content{white-space:pre-wrap; background:#fff; padding:16px; border:1px solid #eee; border-radius:8px;}
  .img-wrap{margin:16px 0;}
  img{max-width:100%; height:auto; border:1px solid #eee; border-radius:8px;}
</style>

<h2>${a.title}</h2>
<div class="meta">
  글번호 ${a.articleNO} · 작성자 ${a.id}
  <c:if test="${not empty a.writeDate}"> · 작성일 ${a.writeDate}</c:if>
</div>

<c:if test="${not empty a.imageFileName}">
  <div class="img-wrap">
    <!-- 프로젝트의 이미지 다운로드 매핑에 맞춰 조정 -->
    <img alt="첨부 이미지"
         src="${ctx}/download.do?articleNO=${a.articleNO}&imageFileName=${a.imageFileName}"
         onerror="this.style.display='none'">
  </div>
</c:if>

<div class="content">${a.content}</div>

<div class="actions">
  <a class="btn" href="${ctx}/admin/board/listArticles.do">목록</a>

  <form method="post" action="${ctx}/admin/board/removeArticle.do"
        onsubmit="return confirm('정말 삭제할까요?');" style="display:inline;">
    <input type="hidden" name="articleNO" value="${a.articleNO}">
    <c:if test="${not empty _csrf}">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </c:if>
    <button type="submit" class="btn">삭제</button>
  </form>
</div>
