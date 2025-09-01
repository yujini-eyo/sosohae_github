// /resources/js/board.js
document.addEventListener('DOMContentLoaded', function () {

  // ----------------------------- 데모 데이터 -----------------------------
  const svcTypes = ['hospital', 'shopping', 'walk', 'talk', 'clean'];
  const svcLabel = { hospital: '동행', shopping: '장보기', walk: '산책/보행', talk: '말벗', clean: '청소/정리' };
  const regions = ['서울','부산','대구','인천','광주','대전','울산','세종','경기','강원','충북','충남','전북','전남','경북','경남','제주'];
  const r = (arr)=>arr[Math.floor(Math.random()*arr.length)];
  const randDate = ()=>{
    const now = new Date();
    const d = new Date(now.getTime() + (Math.random()*10 - 3)*24*3600*1000);
    return d.toISOString().slice(0,16).replace('T',' ');
  };

  const reqData = Array.from({length:42},(_,i)=>({
    id:42-i, title:r(['병원 진료 동행 부탁드립니다','장보기 도와주실 분','근처 공원 함께 산책해요','말벗 및 스마트폰 설정 도움','집 정리정돈 도와주세요']),
    requester:r(['김**','이**','박**','정**','최**']),
    type:r(svcTypes), region:r(regions), wish:randDate(),
    urgency: Math.random()<.25 ? 'urgent':'normal',
    status:r(['대기','진행중','완료']),
    views: Math.floor(Math.random()*800)+20
  }));

  const volData = Array.from({length:28},(_,i)=>({
    id:28-i, title:r(['주 2회 병원 동행 가능합니다','운전 가능, 장보기/병원','평일 오후 산책 도와드려요','말벗 및 스마트폰 사용 도와드립니다','정리수납 경험 있습니다']),
    volunteer:r(['rainbow','er77','warmhands','yoonhelper','brightday']),
    area:r(regions), time:r(['평일 오전','평일 오후','주말 오전','주말 오후']),
    skills:r(['운전','휠체어 보조','정리수납','간단한 영어','혈압측정 보조']),
    match:r(['가능','매칭중','매칭완료']),
    views: Math.floor(Math.random()*600)+10
  }));

  const noticeData = [
    { id:3, title:'여름철 폭염 대비 안내', date:'2025-07-30', views:420 },
    { id:2, title:'개인정보 보호 수칙',   date:'2025-07-05', views:318 },
    { id:1, title:'서비스 오픈 안내',     date:'2025-06-20', views:982 }
  ];

  // ----------------------------- 상태 -----------------------------
  const state = {
    tab:'req', query:'', svc:'', region:'', urgency:'', sort:'recent',
    page:{ req:1, vol:1, notice:1 }, perPage:10
  };

  const els = {
    tabs: document.querySelectorAll('.tab-btn'),
    panels: {
      req: document.getElementById('panel-req'),
      vol: document.getElementById('panel-vol'),
      notice: document.getElementById('panel-notice'),
    },
    tbodies: {
      req: document.getElementById('tbody-req'),
      vol: document.getElementById('tbody-vol'),
      notice: document.getElementById('tbody-notice'),
    },
    pagers: {
      req: document.getElementById('pagination-req'),
      vol: document.getElementById('pagination-vol'),
      notice: document.getElementById('pagination-notice'),
    },
    search:  document.getElementById('search'),
    svc:     document.getElementById('svc'),
    region:  document.getElementById('region'),
    urgency: document.getElementById('urgency'),
    sort:    document.getElementById('sort'),
    resetBtn:document.getElementById('resetBtn'),
    writeBtn:document.getElementById('writeBtn')
  };

  // ----------------------------- 이벤트 -----------------------------
  els.tabs.forEach(btn => btn.addEventListener('click', () => {
    const next = btn.dataset.tab; if (state.tab === next) return;
    state.tab = next;
    els.tabs.forEach(b => b.setAttribute('aria-selected', b === btn ? 'true' : 'false'));
    Object.entries(els.panels).forEach(([k, p]) => p.hidden = (k !== next));
    render(); els.search?.focus();
  }));

  const resetPage = ()=>{ state.page[state.tab] = 1; };

  els.search .addEventListener('input',  e=>{ state.query   = e.target.value.trim(); resetPage(); render(); });
  els.svc    .addEventListener('change', e=>{ state.svc     = e.target.value;       resetPage(); render(); });
  els.region .addEventListener('change', e=>{ state.region  = e.target.value;       resetPage(); render(); });
  els.urgency.addEventListener('change', e=>{ state.urgency = e.target.value;       resetPage(); render(); });
  els.sort   .addEventListener('change', e=>{ state.sort    = e.target.value;                     render(); });

  els.resetBtn.addEventListener('click', ()=>{
    els.search.value=''; els.svc.value=''; els.region.value=''; els.urgency.value=''; els.sort.value='recent';
    state.query=''; state.svc=''; state.region=''; state.urgency=''; state.sort='recent'; resetPage(); render();
  });

  els.writeBtn.addEventListener('click', ()=>{
    alert('글쓰기 폼으로 이동합니다. (실서비스에서는 /help/write 또는 /volunteer/write 라우팅)');
  });

  // ----------------------------- 렌더링 -----------------------------
  function getFiltered(){
    const t = state.tab;
    if (t==='req'){
      let arr = reqData.slice();
      if (state.svc)     arr = arr.filter(v=>v.type   === state.svc);
      if (state.region)  arr = arr.filter(v=>v.region === state.region);
      if (state.urgency) arr = arr.filter(v=>v.urgency=== state.urgency);
      if (state.query){
        const q = state.query.toLowerCase();
        arr = arr.filter(v=>[v.title,v.requester,svcLabel[v.type]].some(x=>(x||'').toLowerCase().includes(q)));
      }
      switch(state.sort){
        case 'views': arr.sort((a,b)=>b.views-a.views); break;
        case 'title': arr.sort((a,b)=>a.title.localeCompare(b.title,'ko')); break;
        default:      arr.sort((a,b)=>b.id-a.id);
      }
      return arr;
    }
    if (t==='vol'){
      let arr = volData.slice();
      if (state.region) arr = arr.filter(v=>v.area===state.region);
      if (state.query){
        const q = state.query.toLowerCase();
        arr = arr.filter(v=>[v.title,v.volunteer,v.skills].some(x=>(x||'').toLowerCase().includes(q)));
      }
      switch(state.sort){
        case 'views': arr.sort((a,b)=>b.views-a.views); break;
        case 'title': arr.sort((a,b)=>a.title.localeCompare(b.title,'ko')); break;
        default:      arr.sort((a,b)=>b.id-a.id);
      }
      return arr;
    }
    // notice
    let arr = noticeData.slice();
    if (state.query){
      const q = state.query.toLowerCase();
      arr = arr.filter(v=>v.title.toLowerCase().includes(q));
    }
    switch(state.sort){
      case 'views': arr.sort((a,b)=>b.views-a.views); break;
      case 'title': arr.sort((a,b)=>a.title.localeCompare(b.title,'ko')); break;
      default:      arr.sort((a,b)=>b.id-a.id);
    }
    return arr;
  }

  function paginate(arr){
    const p = state.page[state.tab];
    const start = (p-1)*state.perPage;
    return [arr.slice(start, start+state.perPage), Math.ceil(arr.length/state.perPage)];
  }

  function render(){
    const data = getFiltered();
    const [items, total] = paginate(data);
    const t = state.tab, tbody = els.tbodies[t];
    tbody.innerHTML = '';

    if (t==='req'){
      items.forEach(v=>{
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td data-label="번호">${v.id}</td>
          <td data-label="제목"><a class="row-link" href="#">${v.title}</a></td>
          <td data-label="요청자">${v.requester}</td>
          <td data-label="유형"><span class="badge type">${svcLabel[v.type]}</span></td>
          <td data-label="지역">${v.region}</td>
          <td data-label="희망일시">${v.wish}</td>
          <td data-label="긴급">${v.urgency==='urgent' ? '<span class="badge urgent">긴급</span>' : '<span class="badge normal">일반</span>'}</td>
          <td data-label="진행" class="status ${v.status==='완료'?'done':(v.status==='대기'?'wait':'')}">${v.status}</td>
          <td data-label="조회">${v.views}</td>
          <td data-label="액션"><div class="row-actions">
            <button class="btn primary" style="padding:8px 12px">신청</button>
            <button class="btn ghost"   style="padding:8px 12px">상세</button></div>
          </td>`;
        tbody.appendChild(tr);
      });
    } else if (t==='vol'){
      items.forEach(v=>{
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td data-label="번호">${v.id}</td>
          <td data-label="제목"><a class="row-link" href="#">${v.title}</a></td>
          <td data-label="봉사자">${v.volunteer}</td>
          <td data-label="가능지역">${v.area}</td>
          <td data-label="가능시간대">${v.time}</td>
          <td data-label="보유기술"><span class="badge type">${v.skills}</span></td>
          <td data-label="매칭" class="status ${v.match==='매칭완료'?'done':(v.match==='가능'?'':'wait')}">${v.match}</td>
          <td data-label="조회">${v.views}</td>
          <td data-label="액션"><div class="row-actions">
            <button class="btn primary" style="padding:8px 12px">도움 제안</button>
            <button class="btn ghost"   style="padding:8px 12px">상세</button></div>
          </td>`;
        tbody.appendChild(tr);
      });
    } else {
      items.forEach(v=>{
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td data-label="번호">${v.id}</td>
          <td data-label="제목"><a class="row-link" href="#">${v.title}</a></td>
          <td data-label="작성일">${v.date}</td>
          <td data-label="조회">${v.views}</td>`;
        tbody.appendChild(tr);
      });
    }

    renderPager(total);
  }

  function renderPager(total){
    const p = state.page[state.tab], pager = els.pagers[state.tab];
    pager.innerHTML = '';
    const makeBtn = (label, page, current=false)=>{
      const b = document.createElement('button');
      b.className = 'page-btn'; b.textContent = label;
      if (current) b.setAttribute('aria-current','page');
      b.addEventListener('click', ()=>{ state.page[state.tab]=page; render(); window.scrollTo({top:0,behavior:'smooth'}) });
      return b;
    };
    if (p>1) pager.appendChild(makeBtn('이전', p-1));
    for (let i=1;i<=total;i++){
      if (i===1 || i===total || Math.abs(i-p)<=1){
        pager.appendChild(makeBtn(String(i), i, i===p));
      } else if (Math.abs(i-p)===2){
        const dot=document.createElement('span'); dot.textContent='…'; dot.style.padding='10px'; pager.appendChild(dot);
      }
    }
    if (p<total) pager.appendChild(makeBtn('다음', p+1));
  }

  // 초기 렌더
  render();
});
