<!-- /WEB-INF/views/admin/board/adminWriteForm.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style>
  .field{margin-bottom:12px;}
  label{display:block; margin-bottom:6px; color:#555;}
  input[type=text], textarea{width:100%; padding:10px; border:1px solid #ddd; border-radius:8px; box-sizing:border-box;}
  textarea{min-height:220px; resize:vertical;}
  .btn{display:inline-block; padding:10px 16px; border:1px solid #ddd; border-radius:8px; text-decoration:none; color:#333; background:#fff;}
  .btn.primary{background:#f6a96d; color:#fff; border-color:#f6a96d;}
  .actions{display:flex; gap:8px; margin-top:16px; flex-wrap:wrap;}
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

<form id="writeForm" method="post"
      action="${ctx}/admin/board/adminaddNewArticle.do"
      enctype="multipart/form-data" accept-charset="UTF-8" autocomplete="off" novalidate>

  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  </c:if>

  <input type="hidden" name="parentNO" value="0"/>

  <div class="field">
    <label for="title">제목</label>
    <input type="text" id="title" name="title" required maxlength="200"
           value="<c:out value='${title}'/>" />
    <div class="row-hint">
      <span class="muted">최대 200자</span>
      <span class="muted" id="titleCount">0 / 200</span>
    </div>
  </div>

  <div class="field">
    <label for="content">내용</label>
    <textarea id="content" name="content" required><c:out value="${content}"/></textarea>
  </div>

  <div class="field">
    <label for="imageFile">첨부 이미지(선택)</label>
    <input type="file" id="imageFile" name="imageFile" accept=".jpg,.jpeg,.png,.gif,.webp" />
    <p class="muted">허용: JPG/PNG/GIF/WebP · 최대 10MB</p>
    <div class="preview" id="imgPreview" style="display:none;">
      <img alt="미리보기"/>
    </div>
  </div>

  <c:if test="${sessionScope.userRole eq 'ADMIN' or (sessionScope.member ne null and sessionScope.member.role eq 'ADMIN')}">
    <div class="field inline-field">
      <input type="checkbox" id="isNotice" name="isNotice" value="1" />
      <label for="isNotice" style="margin:0;">공지사항으로 등록</label>
    </div>
  </c:if>

  <div class="actions">
    <button type="submit" class="btn primary">등록</button>
    <a class="btn" href="${ctx}/admin/board/adminList.do">취소</a>
  </div>
</form>

<script>
(function(){
  var $title = document.getElementById('title');
  var $count = document.getElementById('titleCount');
  var $file  = document.getElementById('imageFile');
  var $prev  = document.getElementById('imgPreview');
  var MAX_MB = 10;
  var MAX_BYTES = MAX_MB * 1024 * 1024;
  var ALLOWED = ['jpg','jpeg','png','gif','webp'];

  function updateCount(){
    if(!$title || !$count) return;
    var len = ($title.value || '').replace(/^\s+|\s+$/g,'').length;
    $count.textContent = len + ' / 200';
  }
  if($title){ $title.addEventListener('input', updateCount); updateCount(); }

  if($file){
    $file.addEventListener('change', function(){
      var f = this.files && this.files[0];
      var img = $prev.querySelector('img');
      if(!f){
        $prev.style.display='none';
        if(img) img.removeAttribute('src');
        return;
      }
      var parts = f.name.split('.');
      var ext = (parts.length>1 ? parts.pop() : '').toLowerCase();
      if(ALLOWED.indexOf(ext) === -1){
        alert('허용되지 않은 파일 형식입니다.');
        this.value = '';
        $prev.style.display='none';
        if(img) img.removeAttribute('src');
        return;
      }
      if(f.size > MAX_BYTES){
        alert('파일이 너무 큽니다. 최대 ' + MAX_MB + 'MB 까지 업로드 가능합니다.');
        this.value = '';
        $prev.style.display='none';
        if(img) img.removeAttribute('src');
        return;
      }
      var reader = new FileReader();
      reader.onload = function(e){
        if(img){ img.src = e.target.result; }
        $prev.style.display='block';
      };
      reader.readAsDataURL(f);
    });
  }

  var form = document.getElementById('writeForm');
  if(form){
    form.addEventListener('submit', function(e){
      if(!$title.value.replace(/^\s+|\s+$/g,'').length){
        e.preventDefault();
        alert('제목을 입력하세요.');
        $title.focus();
        return;
      }
      var $content = document.getElementById('content');
      if(!$content.value.replace(/^\s+|\s+$/g,'').length){
        e.preventDefault();
        alert('내용을 입력하세요.');
        $content.focus();
      }
    });
  }
})();
</script>
