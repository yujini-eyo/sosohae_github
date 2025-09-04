<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style>
  .field{margin-bottom:12px;}
  label{display:block; margin-bottom:6px; color:#555;}
  input[type=text], textarea{width:100%; padding:10px; border:1px solid #ddd; border-radius:8px;}
  textarea{min-height:220px; resize:vertical;}
  .btn{display:inline-block; padding:10px 16px; border:1px solid #ddd; border-radius:8px; text-decoration:none; color:#333;}
  .btn.primary{background:#f6a96d; color:#fff; border-color:#f6a96d;}
  .actions{display:flex; gap:8px; margin-top:16px;}
  .muted{color:#777; font-size:12px;}
</style>

<h2>게시글 작성</h2>

<form method="post"
      action="${ctx}/admin/board/addNewArticle.do"
      enctype="multipart/form-data"
      accept-charset="UTF-8">

  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  </c:if>

  <div class="field">
    <label for="title">제목</label>
    <input type="text" id="title" name="title" required maxlength="200" />
  </div>

  <div class="field">
    <label for="content">내용</label>
    <textarea id="content" name="content" required></textarea>
  </div>

  <div class="field">
    <label for="imageFile">첨부 이미지(선택)</label>
    <input type="file" id="imageFile" name="imageFile" accept="image/*" />
    <p class="muted">최대 업로드 크기 및 허용 확장자는 서버 설정을 따릅니다.</p>
  </div>

  <div class="actions">
    <button type="submit" class="btn primary">등록</button>
    <a class="btn" href="${ctx}/admin/board/listArticles.do">취소</a>
  </div>
</form>
