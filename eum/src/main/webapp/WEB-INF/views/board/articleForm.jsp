<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<!-- 스타일 자바스크립트 -->
<link rel="stylesheet" href="<c:url value='/resources/css/articleForm.css' />">

  <a href="#main" class="skip-link" style="position:absolute;left:-9999px;top:auto;width:1px;height:1px;overflow:hidden">본문으로 건너뛰기</a>

  <div class="container">
    <section id="main" class="board" aria-labelledby="boardTitle">
      <header class="board-header">
        <div>
          <h1 id="boardTitle" class="board-title">글쓰기</h1>
          <p class="desc">얘 왜 자꾸 내려와</p>
        </div>
        <nav class="tabs" role="tablist" aria-label="글 유형 선택">
          <button class="tab-btn" id="tab-req" role="tab" aria-selected="true" aria-controls="panel-req" data-tab="req">도움 요청 글쓰기</button>
          <button class="tab-btn" id="tab-vol" role="tab" aria-selected="false" aria-controls="panel-vol" data-tab="vol">봉사자 모집 글쓰기</button>
        </nav>
      </header>

      <div class="form-wrap">
        <!-- 도움 요청 글쓰기 -->
        <form id="panel-req" role="tabpanel" aria-labelledby="tab-req">
          <!-- 기본 정보 -->
          <section class="fieldset">
            <div class="legend">기본 정보</div>
            <div class="form-grid">
              <div class="form-row">
                <label for="req-title">제목 <span class="badge">필수</span></label>
                <input id="req-title" name="title" type="text" placeholder="예) 병원 진료 동행 부탁드립니다" required />
              </div>
              <div class="form-row">
                <label for="req-region">지역 <span class="badge">필수</span></label>
                <select id="req-region" name="region" required>
                  <option value="">선택하세요</option>
                </select>
              </div>
              <div class="form-row">
                <label for="req-type">요청 유형 <span class="badge">필수</span></label>
                <select id="req-type" name="type" required>
                  <option value="">선택하세요</option>
                  <option value="hospital">동행</option>
                  <option value="shopping">장보기</option>
                  <option value="walk">산책/보행 보조</option>
                  <option value="talk">말벗</option>
                  <option value="clean">청소/정리</option>
                </select>
              </div>
              <div class="form-row">
                <label for="req-wish">희망 일시</label>
                <input id="req-wish" name="wish" type="datetime-local" />
              </div>
              <div class="form-row">
                <label>긴급도</label>
                <div class="row-inline" role="group" aria-label="긴급도">
                  <label class="row-inline"><input type="radio" name="urgency" value="urgent"> 긴급</label>
                  <label class="row-inline"><input type="radio" name="urgency" value="normal" checked> 일반</label>
                </div>
              </div>
              <div class="form-row">
                <label for="req-contact">연락 방법</label>
                <input id="req-contact" name="contact" type="text" placeholder="예) 010-1234-5678 / 카카오톡 yoonhelper" />
              </div>
            </div>
          </section>

          <!-- 내용 -->
          <section class="fieldset">
            <div class="legend">상세 내용</div>
            <div class="form-row">
              <label for="req-content">내용 <span class="badge">필수</span></label>
              <textarea id="req-content" name="content" placeholder="필요하신 도움, 장소, 시간, 기타 참고사항을 적어주세요." required></textarea>
              <div class="hint" id="req-counter">0 / 1000</div>
            </div>
          </section>

          <!-- 첨부 -->
          <section class="fieldset">
            <div class="legend">첨부 이미지</div>
            <div class="form-row">
              <label for="req-files">이미지 (최대 3장)</label>
              <input id="req-files" name="files" type="file" accept="image/*" multiple />
              <div class="preview" id="req-preview" aria-live="polite"></div>
              <div class="hint">개인정보(주민등록증 등)가 노출되지 않도록 유의해주세요.</div>
            </div>
          </section>

          <div class="actions">
            <button type="button" class="btn ghost" onclick="location.href='listArticles.do'">목록</button>
            <button type="reset" class="btn ghost">초기화</button>
              <button type="submit">등록</button>
          </div>
        </form>
  
  <script>
    // ===== 공통 데이터(board.html과 동일 지역/유형) =====
    const regions = ['서울','부산','대구','인천','광주','대전','울산','세종','경기','강원','충북','충남','전북','전남','경북','경남','제주'];

    // 셀렉트 채우기
    const reqRegion = document.getElementById('req-region');
    const volArea = document.getElementById('vol-area');
    regions.forEach(r => {
      reqRegion.insertAdjacentHTML('beforeend', `<option>${r}</option>`);
      volArea.insertAdjacentHTML('beforeend', `<option>${r}</option>`);
    });

    // ===== 탭 전환 =====
    const tabs = document.querySelectorAll('.tab-btn');
    const panels = { req: document.getElementById('panel-req'), vol: document.getElementById('panel-vol') };
    tabs.forEach(btn => btn.addEventListener('click', () => {
      const next = btn.dataset.tab; if (!next) return;
      tabs.forEach(b => b.setAttribute('aria-selected', b === btn ? 'true' : 'false'));
      Object.entries(panels).forEach(([k,p]) => p.hidden = (k !== next));
      // 포커스 이동: 현재 탭의 첫 입력란
      const first = panels[next].querySelector('input,select,textarea');
      first && first.focus();
    }));

    // ===== 글자수 카운터 =====
    const counters = [
      { input: document.getElementById('req-content'), hint: document.getElementById('req-counter') },
      { input: document.getElementById('vol-intro'),   hint: document.getElementById('vol-counter') }
    ];
    counters.forEach(({input, hint}) => {
      const update = () => { const n = input.value.length; hint.textContent = `${n} / 1000`; };
      input.addEventListener('input', update); update();
    });

    // ===== 이미지 미리보기(최대 3장) =====
    function bindPreview(fileInputId, previewId){
      const input = document.getElementById(fileInputId);
      const box = document.getElementById(previewId);
      input.addEventListener('change', () => {
        const files = Array.from(input.files || []).slice(0,3);
        box.innerHTML = '';
        files.forEach(f => {
          const url = URL.createObjectURL(f);
          const img = new Image();
          img.onload = () => URL.revokeObjectURL(url);
          img.src = url; box.appendChild(img);
        });
      });
    }
    bindPreview('req-files','req-preview');
    bindPreview('vol-files','vol-preview');

    // ===== 제출(데모) =====
    function wireSubmit(formId){
      document.getElementById(formId).addEventListener('submit', e => {
        e.preventDefault();
        alert('등록되었습니다. (데모)');
        // 실제 서비스에서는 fetch('/write/addNewArticle.do', { method:'POST', body:FormData }) 등으로 전송
        location.href = 'board.html';
      });
    }
    wireSubmit('panel-req');
    wireSubmit('panel-vol');
  </script>