<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 세션 체크: 관리자만 접근 -->
<c:if test="${empty sessionScope.adminUser}">
  <c:url var="loginUrl" value="/admin/auth/login.do"/>
  <c:redirect url="${loginUrl}"/>
</c:if>

<section class="admin-section" aria-labelledby="memberLookupTitle" style="max-width:920px;margin:0 auto; padding:24px 16px;">
  <h1 id="memberLookupTitle" style="font-size:1.5rem; margin-bottom:16px;">회원 정보 조회</h1>

  <!-- 검색 폼 -->
  <form id="lookupForm" onsubmit="return false;" aria-label="회원 검색 폼"
        style="display:flex; gap:8px; align-items:center; margin-bottom:16px;">
    <label for="memberId" class="sr-only" style="position:absolute; left:-9999px;">회원 ID</label>
    <input id="memberId" name="memberId" type="text" placeholder="회원 ID 입력"
           style="flex:1; padding:10px 12px; border:1px solid #ddd; border-radius:10px;" />
    <button id="btnSearch" type="button"
            style="padding:10px 14px; border-radius:10px; border:0; background:#F6A96D; color:#fff; cursor:pointer;">
      조회
    </button>
    <button id="btnClear" type="button"
            style="padding:10px 14px; border-radius:10px; border:1px solid #ddd; background:#fff; cursor:pointer;">
      초기화
    </button>
    <a href="<c:url value='/admin/auth/listMembers.do'/>" style="margin-left:auto;">← 목록</a>
  </form>

  <!-- 안내/오류 -->
  <div id="status" role="status" aria-live="polite" style="margin-bottom:12px; color:#806A5A;"></div>

  <!-- 결과 카드 -->
  <article id="resultCard" aria-labelledby="resultTitle"
           style="display:none; border:1px solid #FFE1CB; border-radius:16px; background:#fff; box-shadow:0 4px 10px rgba(0,0,0,.06);">
    <header style="padding:14px 16px; border-bottom:1px solid #FFE1CB;">
      <h2 id="resultTitle" style="margin:0; font-size:1.125rem;">회원 기본 정보</h2>
    </header>

    <div style="padding:16px;">
      <dl style="display:grid; grid-template-columns:140px 1fr; row-gap:10px; column-gap:12px;">
        <dt style="color:#806A5A;">회원 ID</dt>
        <dd id="f_id" style="margin:0;">-</dd>

        <dt style="color:#806A5A;">이름</dt>
        <dd id="f_name" style="margin:0;">-</dd>

        <dt style="color:#806A5A;">이메일</dt>
        <dd id="f_email" style="margin:0;">-</dd>

        <dt style="color:#806A5A;">권한</dt>
        <dd id="f_role" style="margin:0;">-</dd>

        <dt style="color:#806A5A;">상태</dt>
        <dd id="f_status" style="margin:0;">-</dd>
      </dl>
    </div>
  </article>
</section>

<script>
(function(){
  'use strict';

  function $(id){ return document.getElementById(id); }
  function on(el, ev, fn){
    if(!el) return;
    if(el.addEventListener) el.addEventListener(ev, fn, false);
    else if(el.attachEvent) el.attachEvent('on' + ev, fn);
    else el['on' + ev] = fn;
  }
  function setText(el, txt){
    if(!el) return;
    if('textContent' in el) el.textContent = txt;
    else el.innerText = txt;
  }

  var statusBox  = $('status');
  var resultCard = $('resultCard');

  var f = {
    id:     $('f_id'),
    name:   $('f_name'),
    email:  $('f_email'),
    role:   $('f_role'),
    status: $('f_status')
  };

  function setStatus(msg, isError){
    setText(statusBox, msg || '');
    if(statusBox) statusBox.style.color = isError ? '#c0392b' : '#806A5A';
  }

  function fillResult(m){
    setText(f.id,     (m && (m.id || m.userId)) || '-');
    setText(f.name,   (m && m.name)   || '-');
    setText(f.email,  (m && m.email)  || '-');
    setText(f.role,   (m && m.role)   || '-');
    setText(f.status, (m && m.status) || '-');
  }

  function showResult(show){
    if(resultCard) resultCard.style.display = show ? 'block' : 'none';
  }

  // IE9 호환 XHR GET
  function xhrGet(url, cb){
    try{
      var x = new XMLHttpRequest();
      x.open('GET', url, true);
      try { x.setRequestHeader('Accept','application/json'); } catch(_){}
      if (x.overrideMimeType) { try { x.overrideMimeType('application/json'); } catch(_){} }
      x.onreadystatechange = function(){
        if(x.readyState === 4){ cb(null, x); }
      };
      x.onerror = function(){ cb(new Error('network')); };
      x.send(null);
    }catch(e){ cb(e); }
  }

  function loadMember(id){
    setStatus('조회 중입니다…', false);
    showResult(false);

    var url = '<c:url value="/admin/api/members/" />' + encodeURIComponent(id);
    xhrGet(url, function(err, res){
      if(err){ setStatus('조회 중 오류가 발생했습니다.', true); return; }
      if(res.status === 404){ setStatus('해당 ID의 회원을 찾을 수 없습니다.', true); return; }
      if(res.status < 200 || res.status >= 300){ setStatus('요청 실패: ' + res.status, true); return; }

      var m;
      try{ m = JSON.parse(res.responseText || '{}'); }
      catch(_){ setStatus('응답 파싱 오류', true); return; }

      fillResult(m);
      showResult(true);
      setStatus('조회 완료', false);
    });
  }

  on($('btnSearch'), 'click', function(){
    var v = $('memberId') ? $('memberId').value : '';
    var id = v.replace(/^\s+|\s+$/g, '');
    if(!id){ setStatus('회원 ID를 입력하세요.', true); return; }
    loadMember(id);
  });

  on($('btnClear'), 'click', function(){
    if($('memberId')) $('memberId').value = '';
    setStatus('', false);
    showResult(false);
  });

  // Enter 키로 조회
  on($('memberId'), 'keydown', function(e){
    e = e || window.event;
    var key = e.keyCode || e.which;
    if(key === 13){
      var btn = $('btnSearch');
      if(btn && btn.click) btn.click();
      if(e.preventDefault) e.preventDefault();
      return false;
    }
  });

  // ?memberId=xxx 자동 조회
  (function(){
    var qs = window.location ? (window.location.search || '') : '';
    if(qs.length > 1){
      var parts = qs.substring(1).split('&');
      for(var i=0;i<parts.length;i++){
        var kv = parts[i].split('=');
        if(kv[0] === 'memberId'){
          var initId = decodeURIComponent(kv[1] || '');
          if(initId){
            if($('memberId')) $('memberId').value = initId;
            loadMember(initId);
          }
          break;
        }
      }
    }
  })();

})();
</script>
