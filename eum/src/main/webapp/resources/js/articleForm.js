// /resources/js/articleForm.js
(() => {
  const $  = (s, p=document) => p.querySelector(s);
  const $$ = (s, p=document) => [...p.querySelectorAll(s)];

  const form   = $('#articleForm');
  const titleI = $('#title');
  const detail = $('#contentDetail');
  const contentHidden = $('#content');

  const svcTypeH = $('#svcType');
  const regionH  = $('#regionHidden');
  const reqAtH   = $('#reqAt');
  const urgH     = $('#urgencyHidden');
  const pointsH  = $('#pointsHidden');

  const prog   = $('#prog');
  const next   = $('#next');
  const prev   = $('#prev');
  const submit = $('#submit');
  const summary= $('#summary');

  // state
  const state = { type:'', region:'', day:'', time:'', urgency:'일반', points:100 };
  let cur = 1, MAX=5;

  // 유틸
  const go = (n) => {
    cur = Math.max(1, Math.min(MAX, n));
    $$('.step').forEach(s => s.classList.remove('active'));
    $(`.step[data-step="${cur}"]`)?.classList.add('active');
    if (prog) prog.textContent = `${cur} / ${MAX}`;
    prev.disabled = (cur === 1);
    next.classList.toggle('hide', cur === MAX);
    submit.classList.toggle('hide', cur !== MAX);
    renderSummary();
  };

  const setActive = (groupSel, val) => {
    $$(groupSel + ' .btn').forEach(b => b.classList.toggle('active', b.dataset.val === val));
  };

  const ampmTo24 = (label) => {
    // "오전 9시", "오후 1시" → "09:00", "13:00"
    const m = label.match(/오(전|후)\s*(\d{1,2})시/);
    if (!m) return "00:00";
    let h = parseInt(m[2], 10);
    if (m[1] === '후' && h < 12) h += 12;
    if (m[1] === '전' && h === 12) h = 0;
    return (h < 10 ? '0'+h : ''+h) + ':00';
  };

  const buildReqAt = () => {
    if (!state.day || !state.time) return '';
    // 서버 @InitBinder가 "yyyy-MM-dd HH:mm" 또는 "yyyy-MM-dd'T'HH:mm" 모두 수용
    return `${state.day} ${ampmTo24(state.time)}`;
  };

  const genTitle = () => {
    const base = [state.type, state.region, state.day && (state.day.slice(5).replace('-', '/')), state.time]
                  .filter(Boolean).join(' · ');
    return base || '도움 요청';
  };

  const renderSummary = () => {
    if (!summary) return;
    const chips = [
      `유형: ${state.type || '-'}`,
      `지역: ${state.region || '-'}`,
      `날짜: ${state.day || '-'}`,
      `시간: ${state.time || '-'}`,
      `긴급도: ${state.urgency}`,
      `권장 포인트: ${state.points} P`,
    ];
    summary.innerHTML = chips.map(c => `<span class="chip">${c}</span>`).join(' ');
    if (!titleI.value) titleI.placeholder = genTitle();
  };

  // 날짜 버튼(7일)
  const dayWrap = $('#dayBtns');
  if (dayWrap) {
    const today = new Date();
    for (let i=0;i<7;i++){
      const d = new Date(today); d.setDate(today.getDate()+i);
      const yyyy = d.getFullYear();
      const mm = String(d.getMonth()+1).padStart(2,'0');
      const dd = String(d.getDate()).padStart(2,'0');
      const iso = `${yyyy}-${mm}-${dd}`;
      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'btn';
      btn.dataset.val = iso;
      btn.textContent = `${mm}/${dd}(${['일','월','화','수','목','금','토'][d.getDay()]})`;
      btn.addEventListener('click', () => {
        state.day = iso;
        setActive('#dayBtns', iso);
        renderSummary();
      });
      dayWrap.appendChild(btn);
    }
  }

  // 1단계: 유형
  $$('#typeBtns .btn').forEach(b => b.addEventListener('click', () => {
    state.type = b.dataset.val; setActive('#typeBtns', state.type); go(2);
  }));

  // 2단계: 지역
  $$('#regionBtns .btn').forEach(b => b.addEventListener('click', () => {
    state.region = b.dataset.val; setActive('#regionBtns', state.region); go(3);
  }));

  // 3단계: 시간
  $$('#timeBtns .btn').forEach(b => b.addEventListener('click', () => {
    state.time = b.dataset.val; setActive('#timeBtns', state.time); go(4);
  }));

  // 4단계: 긴급
  $$('#urgBtns .btn').forEach(b => b.addEventListener('click', () => {
    state.urgency = b.dataset.val;
    state.points = (state.urgency === '긴급') ? 130 : 100;
    $$('#urgBtns .btn').forEach(x => x.classList.toggle('active', x.dataset.val===state.urgency));
    renderSummary(); go(5);
  }));

  // 제목 다시 만들기
  $('#regen')?.addEventListener('click', () => { titleI.value = genTitle(); });

  // 이전/다음
  prev?.addEventListener('click', () => go(cur-1));
  next?.addEventListener('click', () => go(cur+1));

  // 폼 제출: 마지막에만 등록
  form?.addEventListener('submit', (e) => {
    // 유효성 검사
    if (!state.type || !state.region || !state.day || !state.time) {
      e.preventDefault();
      alert('유형, 지역, 날짜, 시간을 모두 선택해 주세요.');
      return;
    }

    // 숨은 필드 채우기
    svcTypeH.value = state.type;
    regionH.value  = state.region;
    reqAtH.value   = buildReqAt();           // 예: "2025-09-12 13:00"
    urgH.value     = state.urgency;
    pointsH.value  = state.points;

    // content 자동 구성 (요약 + 상세)
    const lines = [
      `[요약] ${genTitle()}`,
      `- 유형: ${state.type}`,
      `- 지역: ${state.region}`,
      `- 요청 시각: ${reqAtH.value || '-'}`,
      `- 긴급도/권장포인트: ${state.urgency} / ${state.points}P`,
      '',
      detail.value.trim()
    ];
    contentHidden.value = lines.join('\n');

    // 제목 비어있으면 자동 생성
    if (!titleI.value.trim()) titleI.value = genTitle();
  });

  // "등록하기"는 submit 트리거만
  submit?.addEventListener('click', () => form.requestSubmit());

  // 초기 진입
  go(1);
})();
