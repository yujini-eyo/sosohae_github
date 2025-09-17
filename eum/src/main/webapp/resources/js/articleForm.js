// articleForm.js
// 폼 단계 이동 + 숨김필드 반영 + 제출 시 content는 상세만 전송합니다.
(() => {
  // ===== 유틸 셀렉터 =====
  const $  = (s, p = document) => p.querySelector(s);
  const $$ = (s, p = document) => [...p.querySelectorAll(s)];

  // ===== DOM 참조 =====
  const form          = $('#articleForm');
  if (!form) return; // 폼이 없으면 조용히 종료

  const stepsAll      = $$('.step');
  const titleInput    = $('#title');

  // 보이는 본문(사용자 입력), 숨김 본문(서버 전송용)
  const contentDetail = $('#contentDetail') || $('#content');
  const contentHidden = $('#contentHidden')
    || $('textarea[name="content"][hidden]')
    || $('input[name="content"][type="hidden"]');

  // 숨은 필드(서버 전송용)
  const svcTypeHidden = $('#svcType');        // name="svcType"
  const regionHidden  = $('#regionHidden');   // name="region"
  const reqAtHidden   = $('#reqAt');          // name="reqAt"
  const urgencyHidden = $('#urgencyHidden');  // name="urgency"
  const pointsHidden  = $('#pointsHidden');   // name="points"

  // 진행/네비
  const prog        = $('#prog');
  const nextBtn     = $('#next');
  const prevBtn     = $('#prev');
  const submitBtn   = $('#submit');
  const summaryChips= $('#summary');

  // 선택 버튼 그룹(유형/지역/시간/긴급도) + 날짜 영역
  const typeBtns    = $('#typeBtns');
  const regionBtns  = $('#regionBtns');
  const timeBtns    = $('#timeBtns');
  const urgBtns     = $('#urgBtns');
  const dayWrap     = $('#dayBtns');

  // 제목 자동 생성 트리거
  const regenBtn    = $('#regen');

  // ===== 상태 =====
  const state = { type:'', region:'', day:'', time:'', urgency:'일반', points:100 };
  let currentStep = 1;
  const MAX_STEP  = 5;

  // ===== 헬퍼 =====
  const clamp      = (n, min, max) => Math.max(min, Math.min(max, n));
  const scrollInto = (el) => { try { el?.scrollIntoView({ behavior:'smooth', block:'start' }); } catch {} };

  // 버튼 그룹 내 .active 토글
  const setActiveButton = (groupEl, value) => {
    if (!groupEl) return;
    $$('.btn', groupEl).forEach(btn => {
      btn.classList.toggle('active', btn.dataset.val === value);
    });
  };

  // "오전/오후 X시" → "HH:mm"
  const ampmTo24 = (label) => {
    const m = String(label || '').match(/오(전|후)\s*(\d{1,2})\s*시/);
    if (!m) return '00:00';
    let h = parseInt(m[2], 10);
    if (m[1] === '후' && h < 12) h += 12;
    if (m[1] === '전' && h === 12) h = 0;
    return `${h.toString().padStart(2,'0')}:00`;
  };

  // yyyy-MM-dd HH:mm (reqAt)
  const buildRequestAt = () => (!state.day || !state.time) ? '' : `${state.day} ${ampmTo24(state.time)}`;

  // 제목 자동 생성
  const generateTitle = () => {
    const parts = [
      state.type,
      state.region,
      state.day && state.day.slice(5).replace('-', '/'),
      state.time
    ].filter(Boolean);
    return parts.join(' · ') || '도움 요청';
  };

  // (남겨두기) 요약 문자열 — 화면 칩/숨은필드 반영에만 사용, 본문 전송엔 사용하지 않음
  const buildSummary = () => {
    const svc = svcTypeHidden?.value || '';
    const reg = regionHidden?.value   || '';
    const req = reqAtHidden?.value    || '';
    const urg = urgencyHidden?.value  || '';
    const pts = pointsHidden?.value   || '';
    const lines = [];
    if (svc) lines.push(`유형: ${svc}`);
    if (reg) lines.push(`지역: ${reg}`);
    if (req) lines.push(`요청시각: ${req}`);
    if (urg) lines.push(`긴급도: ${urg}`);
    if (pts) lines.push(`포인트: ${pts}`);
    return lines.length ? lines.join(' | ') + '\n\n' : '';
  };

  // 요약칩 렌더 + 숨은필드 반영
  const renderSummary = () => {
    if (summaryChips) {
      const chips = [
        `유형: ${state.type || '-'}`,
        `지역: ${state.region || '-'}`,
        `날짜: ${state.day || '-'}`,
        `시간: ${state.time || '-'}`,
        `긴급도: ${state.urgency}`,
        `권장 포인트: ${state.points} P`
      ];
      summaryChips.innerHTML = chips.map(t => `<span class="chip">${t}</span>`).join(' ');
    }
    if (titleInput && !titleInput.value) titleInput.placeholder = generateTitle();

    // 숨김값 반영(서버 전송용)
    if (svcTypeHidden) svcTypeHidden.value = state.type || '';
    if (regionHidden)  regionHidden.value  = state.region || '';
    if (reqAtHidden)   reqAtHidden.value   = buildRequestAt();
    if (urgencyHidden) urgencyHidden.value = state.urgency;
    if (pointsHidden)  pointsHidden.value  = state.points;
  };

  // ===== 상세 → 숨김 본문 동기화 (상세만 전송) =====
  const normalize = (s) => (s || '')
    .replace(/\r\n/g, '\n')       // CRLF → LF
    .replace(/\u00A0/g, ' ')      // NBSP → space
    .replace(/\n{3,}/g, '\n\n')   // 3줄 이상 빈줄 → 2줄
    .trim();

  const syncContent = () => {
    if (contentHidden) contentHidden.value = normalize(contentDetail?.value || '');
  };

  contentDetail?.addEventListener('input', syncContent);
  syncContent(); // 초기 1회

  // 단계 이동
  const goToStep = (n) => {
    currentStep = clamp(n, 1, MAX_STEP);
    stepsAll.forEach(s => s.classList.remove('active'));
    const curEl = $(`.step[data-step="${currentStep}"]`);
    curEl?.classList.add('active');

    if (prog) prog.textContent = `${currentStep} / ${MAX_STEP}`;
    if (prevBtn) prevBtn.disabled = (currentStep === 1);
    nextBtn?.classList.toggle('hide', currentStep === MAX_STEP);
    submitBtn?.classList.toggle('hide', currentStep !== MAX_STEP);

    renderSummary();
    scrollInto(curEl);
  };

  // ===== 이벤트 바인딩 =====

  // (중요) data-step 요소 클릭 → 해당 단계로 점프 (전역 위임)
  document.addEventListener('click', (e) => {
    const el = e.target.closest('[data-step]');
    if (!el) return;
    const n = parseInt(el.getAttribute('data-step'), 10);
    if (Number.isNaN(n)) return;

    // 네비 버튼/링크 기본동작 방지
    if (el.tagName === 'A' || (el.tagName === 'BUTTON' && el.type !== 'submit')) {
      e.preventDefault();
    }
    goToStep(n);
  });

  // 날짜 버튼(7일치) 동적 생성
  if (dayWrap) {
    const today = new Date();
    for (let i = 0; i < 7; i++) {
      const d = new Date(today);
      d.setDate(today.getDate() + i);
      const iso = d.toISOString().slice(0, 10); // yyyy-MM-dd

      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'btn';
      btn.dataset.val = iso;
      btn.textContent = `${d.getMonth()+1}/${d.getDate()}(${['일','월','화','수','목','금','토'][d.getDay()]})`;

      btn.addEventListener('click', () => {
        state.day = iso;
        setActiveButton(dayWrap, iso);
        renderSummary();
      });

      dayWrap.appendChild(btn);
    }
  }

  // 유형/지역/시간/긴급도 선택
  $$('.btn', typeBtns).forEach(b => b.addEventListener('click', () => {
    state.type = b.dataset.val || '';
    setActiveButton(typeBtns, state.type);
    goToStep(2);
  }));

  $$('.btn', regionBtns).forEach(b => b.addEventListener('click', () => {
    state.region = b.dataset.val || '';
    setActiveButton(regionBtns, state.region);
    goToStep(3);
  }));

  $$('.btn', timeBtns).forEach(b => b.addEventListener('click', () => {
    state.time = b.dataset.val || '';
    setActiveButton(timeBtns, state.time);
    goToStep(4);
  }));

  $$('.btn', urgBtns).forEach(b => b.addEventListener('click', () => {
    state.urgency = b.dataset.val || '일반';
    state.points  = (state.urgency === '긴급') ? 130 : 100;
    setActiveButton(urgBtns, state.urgency);
    goToStep(5);
  }));

  // 제목 자동 생성
  regenBtn?.addEventListener('click', () => {
    if (titleInput) titleInput.value = generateTitle();
  });

  // 이전/다음
  prevBtn?.addEventListener('click', () => goToStep(currentStep - 1));
  nextBtn?.addEventListener('click', () => goToStep(currentStep + 1));

  // 제출 직전: 필수 선택 검증 + 숨김필드 보정 + 상세만 전송
  form.addEventListener('submit', (e) => {
    // 필수 4요소 체크
    if (!state.type || !state.region || !state.day || !state.time) {
      e.preventDefault();
      alert('유형, 지역, 날짜, 시간을 모두 선택해 주세요.');
      goToStep(1);
      return;
    }

    // 서버 전송용 숨김 필드 재확인(마지막 보정)
    renderSummary();

    // (핵심) 본문은 상세만 전송
    syncContent();
    if (contentHidden) {
      console.log('[JS] contentHidden.length =', contentHidden.value.length);
    }

    // 제목이 비면 자동 생성
    if (titleInput && !titleInput.value.trim()) {
      titleInput.value = generateTitle();
    }
  });

  // '등록하기' 버튼 → submit 트리거
  submitBtn?.addEventListener('click', () => form.requestSubmit());

  // 초기 표시
  goToStep(1);

  // (호환) 혹시 JSP 어딘가에 onclick="makeSummary()"가 남아있어도 에러 안 나게
  window.makeSummary = buildSummary;
})();
