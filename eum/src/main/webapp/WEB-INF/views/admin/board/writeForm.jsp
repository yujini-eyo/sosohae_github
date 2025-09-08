<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style>
  .field{margin-bottom:12px;}
  label{display:block; margin-bottom:6px; color:#555;}
  input[type=text], textarea{width:100%; padding:10px; border:1px solid #ddd; border-radius:8px; box-sizing:border-box;}
  textarea{min-height:220px; resize:vertical;}
  .btn{display:inline-block; padding:10px 16px; border:1px solid #ddd; border-radius:8px; text-decoration:none; color:#333;}
  .btn.primary{background:#f6a96d; color:#fff; border-color:#f6a96d;}
  .actions{display:flex; gap:8px; margin-top:16px;}
  .muted{color:#777; font-size:12px;}
  .inline-field{display:flex; align-items:center; gap:8px;}
  .row-hint{display:flex; justify-content:space-between; align-items:center; font-size:12px; color:#777; margin-top:6px;}
  .error{margin:10px 0; padding:10px 12px; border-radius:8px; background:#fff4f4; color:#8f2d2d; border:1px solid #ffd7d7;}
  .preview{margin-top:8px;}
  .preview img{max-width:240px; height:auto; border:1px solid #eee; border-radius:8px;}
</style>

<h2>게시글 작성</h2>

<c:if test="${not empty formError}">
  <div class="error"><c:out value="${formError}"/></div>
</c:if>

<form id="writeForm"
      method="post"
      action="${ctx}/admin/board/addNewArticle.do"
      enctype="multipart/form-data"
      accept-charset="UTF-8"
      novalidate>

  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  </c:if>

  <%-- 컨트롤러에서 parentNO를 0으로 세팅하지 않는 경우를 대비한 안전장치 --%>
  <input type="hidden" name="parentNO" value="0"/>

  <div class="field">
    <label for="title">제목</label>
    <input type="text" id="title" name="title" required maxlength="200" />
    <div class="row-hint">
      <span class="muted">최대 200자</span>
      <span class="muted" id="titleCount">0 / 200</span>
    </div>
  </div>

  <div class="field">
    <label for="content">내용</label>
    <textarea id="content" name="content" required></textarea>
  </div>

  <div class="field">
    <label for="imageFile">첨부 이미지(선택)</label>
    <input type="file" id="imageFile" name="imageFile" accept=".jpg,.jpeg,.png,.gif,.webp" />
    <p class="muted">허용: JPG/PNG/GIF/WebP · 최대 10MB (서버 설정 기준)</p>
    <div class="preview" id="imgPreview" style="display:none;">
      <img alt="미리보기"/>
    </div>
  </div>

  <%-- ✅ ADMIN만 공지 체크 노출 --%>
  <c:if test="${sessionScope.userRole eq 'ADMIN' or (sessionScope.member ne null and sessionScope.member.role eq 'ADMIN')}">
    <div class="field inline-field">
      <input type="checkbox" id="isNotice" name="isNotice" value="1" />
      <label for="isNotice" style="margin:0;">공지사항으로 등록</label>
    </div>
  </c:if>

  <div class="actions">
    <button type="submit" class="btn primary">등록</button>
    <a class="btn" href="${ctx}/admin/board/listArticles.do">취소</a>
  </div>
</form>

<script>
(function(){
  const $title = document.getElementById('title');
  const $count = document.getElementById('titleCount');
  const $file  = document.getElementById('imageFile');
  const $prev  = document.getElementById('imgPreview');
  const MAX_MB = 10;
  const MAX_BYTES = MAX_MB * 1024 * 1024;
  const ALLOWED = ['jpg','jpeg','png','gif','webp'];

  // 제목 글자수 카운트
  function updateCount(){
    if(!$title || !$count) return;
    const len = $title.value.trim().length;
    $count.textContent = len + ' / 200';
  }
  $title && ($title.addEventListener('input', updateCount), updateCount());

  // 이미지 파일 검증 + 미리보기
  if($file){
    $file.addEventListener('change', function(){
      const f = this.files && this.files[0];
      if(!f){
        $prev.style.display='none';
        $prev.querySelector('img').src='';
        return;
      }
      const ext = (f.name.split('.').pop() || '').toLowerCase();
      if(!ALLOWED.includes(ext)){
        alert('허용되지 않은 파일 형식입니다. (허용: ' + ALLOWED.join(', ') + ')');
        this.value = '';
        $prev.style.display='none';
        return;
      }
      if(f.size > MAX_BYTES){
        alert('파일이 너무 큽니다. 최대 ' + MAX_MB + 'MB 까지 업로드 가능합니다.');
        this.value = '';
        $prev.style.display='none';
        return;
      }
      // 미리보기
      const reader = new FileReader();
      reader.onload = function(e){
        const img = $prev.querySelector('img');
        img.src = e.target.result;
        $prev.style.display='block';
      };
      reader.readAsDataURL(f);
    });
  }

  // 제출 전 간단 검증(추가)
  const form = document.getElementById('writeForm');
  form && form.addEventListener('submit', function(e){
    if(!$title.value.trim()){
      e.preventDefault();
      alert('제목을 입력하세요.');
      $title.focus();
      return;
    }
    if(!document.getElementById('content').value.trim()){
      e.preventDefault();
      alert('내용을 입력하세요.');
      document.getElementById('content').focus();
      return;
    }
  });
})();
</script>
