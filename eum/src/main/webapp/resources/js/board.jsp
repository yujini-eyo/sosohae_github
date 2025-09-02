<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>소소한 도움 - 게시판</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <!-- (선택) 프로젝트 폰트 -->
  <link href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" rel="stylesheet" />

  <style>
    :root{
      --bg:#FFF8F2; --text:#59463E; --muted:#806A5A;
      --primary:#F6A96D; --primary-600:#e98c45;
      --card:#fff; --card-br:#FFE1CB; --brand-200:#FADFCB;
      --shadow: 0 8px 24px rgba(0,0,0,0.08);
    }
    body{margin:0; font-family:"SUIT",system-ui,-apple-system,Segoe UI,Roboto,"Noto Sans KR",sans-serif; background:var(--bg); color:var(--text);}
    .wrap{max-width:1100px; margin:40px auto; padding:0 16px;}
    .board-header{display:flex; flex-wrap:wrap; gap:10px; align-items:center; justify-content:space-between; margin-bottom:14px;}
    .tabs{display:flex; gap:8px;}
    .tab-btn{padding:10px 14px; border-radius:12px; border:1px solid var(--brand-200); background:#fff; cursor:pointer; font-weight:700;}
    .tab-btn[aria-selected="true"]{background:#ffe7c9;}
    .filters{display:flex; gap:8px; flex-wrap:wrap;}
    .filters input, .filters select{height:36px; padding:0 10px; border:1px solid var(--brand-200); border-radius:10px; background:#fff;}
    .btn{height:36px; padding:0 12px; border-radius:10px; border:1px solid var(--brand-200); background:#fff; cursor:pointer; font-weight:700;}
    .btn.primary{background:var(--primary); color:#fff; border:0;}
    .btn.ghost{background:#fff;}
    .card{background:var(--card); border:1px solid var(--card-br); border-radius:16px; box-shadow:var(--shadow);}
    table{width:100%; border-collapse:collapse;}
    thead th{position:sticky; top:0; background:#fff6ea; font-size:14px; text-align:left; padding:12px; border-bottom:1px solid var(--brand-200);}
    tbody td{padding:12px; border-bottom:1px solid #f1e6d9; font-size:14px; vertical-align:middle;}
    .table-wrap{overflow:auto; border-radius:16px;}
    .row-actions{display:flex; gap:6px; justify-content:flex-end;}
    .badge{display:inline-block; padding:4px 8px; border-radius:999px; font-size:12px; font-weight:800; border:1px solid var(--brand-200); background:#fff;}
    .badge.type{background:#fffaf2;}
    .badge.urgent{background:#ffe3e3; border-color:#ffc2c2; color:#b10000;}
    .badge.normal{background:#eef8ff; border-color:#cfe7ff; color:#0b4c8c;}
    .status.done{color:#0b8c37; font-weight:800;}
    .status.wait{color:#8c6a0b; font-weight:800;}
    .pagination{display:flex; gap:6px; justify-content:center; padding:16px;}
    .page-btn{padding:8px 12px; border:1px solid var(--brand-200); border-radius:10px; background:#fff; cursor:pointer;}
    .page-btn[aria-current="page"]{background:#ffdcae; font-weight:800;}
    .sr-only{position:absolute; width:1px; height:1px; padding:0; margin:-1px; overflow:hidden; clip:rect(0,0,0,0); white-space:nowrap; border:0;}
    a.row-link{color:inherit; text-decoration:none; font-weight:700;}
    a.row-link:hover{text-decoration:underline;}
    @media (max-width:720px){
      thead{display:none;}
      tbody tr{display:grid; grid-template-columns:1fr 1fr; gap:8px; padding:12px; border-bottom:1px solid #f1e6d9;}
      tbody td{border:0; padding:6px 0;}
      tbody td::before{content:attr(data-label) " "; display:block; font-size:12px; color:var(--muted);}
    }
  </style>
</head>
<body>
  <div class="wrap">
    <h1 style="margin:0 0 8px 0;">도움 게시판</h1>

    <div class="board-header">
      <div class="tabs" role="tablist" aria-label="게시판 종류">
        <button type="button" class="tab-btn" data-tab="req" aria-selected="true" role="tab">요청글</button>
        <button type="button" class="tab-btn" data-tab="vol" aria-selected="false" role="tab">봉사글</button>
        <button type="button" class="tab-btn" data-tab="notice" aria-selected="false" role="tab">공지</button>
      </div>

      <div class="filters">
        <input type="search" id="search" placeholder="검색(제목/작성자/유형)" aria-label="검색" />
        <select id="svc" aria-label="서비스 유형">
          <option value="">유형 전체</option>
          <option value="hospital">동행</option>
          <option value="shopping">장보기</option>
          <option value="walk">산책/보행</option>
          <option value="talk">말벗</option>
          <option value="clean">청소/정리</option>
        </select>
        <select id="region" aria-label="지역">
          <option value="">지역 전체</option>
          <option>서울</option><option>부산</option><option>대구</option><option>인천</option>
          <option>광주</option><option>대전</option><option>울산</option><option>세종</option>
          <option>경기</option><option>강원</option><option>충북</option><option>충남</option>
          <option>전북</option><option>전남</option><option>경북</option><option>경남</option><option>제주</option>
        </select>
        <select id="urgency" aria-label="긴급도(요청글)">
          <option value="">긴급 전체</option>
          <option value="urgent">긴급</option>
          <option value="normal">일반</option>
        </select>
        <select id="sort" aria-label="정렬">
          <option value="recent">최신순</option>
          <option value="views">조회순</option>
          <option value="title">제목순</option>
        </select>
        <button type="button" id="resetBtn" class="btn ghost">초기화</button>
        <button type="button" id="writeBtn" class="btn primary">글쓰기</button>
      </div>
    </div>

    <!-- 요청글 패널 -->
    <section id="panel-req" class="card" role="tabpanel" aria-labelledby="tab-req">
      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>번호</th><th>제목</th><th>요청자</th><th>유형</th><th>지역</th>
              <th>희망일시</th><th>긴급</th><th>진행</th><th>조회</th><th class="sr-only">액션</th>
            </tr>
          </thead>
          <tbody id="tbody-req"></tbody>
        </table>
      </div>
      <div id="pagination-req" class="pagination" aria-label="요청글 페이지네이션"></div>
    </section>

    <!-- 봉사글 패널 -->
    <section id="panel-vol" class="card" role="tabpanel" aria-labelledby="tab-vol" hidden>
      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>번호</th><th>제목</th><th>봉사자</th><th>가능지역</th><th>가능시간대</th>
              <th>보유기술</th><th>매칭</th><th>조회</th><th class="sr-only">액션</th>
            </tr>
          </thead>
          <tbody id="tbody-vol"></tbody>
        </table>
      </div>
      <div id="pagination-vol" class="pagination" aria-label="봉사글 페이지네이션"></div>
    </section>

    <!-- 공지 패널 -->
    <section id="panel-notice" class="card" role="tabpanel" aria-labelledby="tab-notice" hidden>
      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>번호</th><th>제목</th><th>작성일</th><th>조회</th>
            </tr>
          </thead>
          <tbody id="tbody-notice"></tbody>
        </table>
      </div>
      <div id="pagination-notice" class="pagination" aria-label="공지 페이지네이션"></div>
    </section>
  </div>

  <!-- ===== 페이지 전용 스크립트 (기존 board.js 로직을 JSP에 내장) ===== -->
  <script>
  document.addEventListener('DOMContentLoaded', function () {
    // ---------- 설정/상수 ----------
    const CTX = '<c:out value="${ctx}"/>'; // .do 링크용 컨텍스트 루트
    const svcTypes = ['hospital', 'shopping', 'walk', 'talk', 'clean'];
    const svcLabel = { hospital: '동행', shopping: '장보기', walk: '산책/보행', talk: '말벗', clean: '청소/정리' };
    const regions = ['서울','부산','대구','인천','광주','대전','울산','세종','경기','강원','충북','충남','전북','전남','경북','경남','제주'];
    const r = (arr)=>arr[Math.floor(Math.random()*arr.length)];
    const pad = (n)=>String(n).padStart(2,'0');
    const randDate = ()=>{
      const now = new Date();
      const d = new Date(now.getTime() + (Math.random()*10 - 3)*24*3600*1000);
      const y=d.getFullYear(), m=pad(d.getMonth()+1), da=pad(d.getDate()), hh=pad(d.getHours()), mm=pad(d.getMinutes());
      return `${y}-${m}-${da} ${hh}:${mm}`;
    };

    // ---------- 데모 데이터 ----------
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

    // ---------- 상태 ----------
    const state = {
      tab:'req', query:'', svc:'', region:'', urgency:'', sort:'recent',
      page:{ req:1, vol:1, notice:1 }, perPage:10
    };

    // ---------- 요소 ----------
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

    // ---------- 탭 전환 ----------
    els.tabs.forEach(btn => btn.addEventListener('click', () => {
      const next = btn.dataset.tab; if (state.tab === next) return;
      state.tab = next;
      els.tabs.forEach(b => b.setAttribute('aria-selected', b === btn ? 'true' : 'false'));
      Object.entries(els.panels).forEach(([k, p]) => p.hidden = (k !== next));
      render(); els.search?.focus();
    }));

    const resetPage = ()=>{ state.page[state.tab] = 1; };

    // ---------- 필터/정렬 ----------
    els.search .addEventListener('input',  e=>{ state.query   = e.target.value.trim(); resetPage(); render(); });
    els.svc    .addEventListener('change', e=>{ state.svc     = e.target.value;       resetPage(); render(); });
    els.region .addEventListener('change', e=>{ state.region  = e.target.value;       resetPage(); render(); });
    els.urgency.addEventListener('change', e=>{ state.urgency = e.target.value;       resetPage(); render(); });
    els.sort   .addEventListener('change', e=>{ state.sort    = e.target.value;                     render(); });

    els.resetBtn.addEventListener('click', ()=>{
      els.search.value=''; els.svc.value=''; els.region.value=''; els.urgency.value=''; els.sort.value='recent';
      state.query=''; state.svc=''; state.region=''; state.urgency=''; state.sort='recent'; resetPage(); render();
    });

    // ---------- 글쓰기 ----------
    els.writeBtn.addEventListener('click', ()=>{
      if (state.tab==='req')   location.href = CTX + '/help/write.do';
      else if (state.tab==='vol') location.href = CTX + '/volunteer/write.do';
      else location.href = CTX + '/notice/write.do';
    });

    // ---------- 데이터 필터링 ----------
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

    // ---------- 렌더 ----------
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
            <td data-label="제목"><a class="row-link" href="${CTX}/help/detail.do?id=${v.id}">${v.title}</a></td>
            <td data-label="요청자">${v.requester}</td>
            <td data-label="유형"><span class="badge type">${svcLabel[v.type]}</span></td>
            <td data-label="지역">${v.region}</td>
            <td data-label="희망일시">${v.wish}</td>
            <td data-label="긴급">${v.urgency==='urgent' ? '<span class="badge urgent">긴급</span>' : '<span class="badge normal">일반</span>'}</td>
            <td data-label="진행" class="status ${v.status==='완료'?'done':(v.status==='대기'?'wait':'')}">${v.status}</td>
            <td data-label="조회">${v.views}</td>
            <td data-label="액션"><div class="row-actions">
              <a class="btn primary" href="${CTX}/volunteer/apply.do?reqId=${v.id}">신청</a>
              <a class="btn ghost"   href="${CTX}/help/detail.do?id=${v.id}">상세</a>
            </div></td>`;
          tbody.appendChild(tr);
        });
      } else if (t==='vol'){
        items.forEach(v=>{
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td data-label="번호">${v.id}</td>
            <td data-label="제목"><a class="row-link" href="${CTX}/volunteer/detail.do?id=${v.id}">${v.title}</a></td>
            <td data-label="봉사자">${v.volunteer}</td>
            <td data-label="가능지역">${v.area}</td>
            <td data-label="가능시간대">${v.time}</td>
            <td data-label="보유기술"><span class="badge type">${v.skills}</span></td>
            <td data-label="매칭" class="status ${v.match==='매칭완료'?'done':(v.match==='가능'?'':'wait')}">${v.match}</td>
            <td data-label="조회">${v.views}</td>
            <td data-label="액션"><div class="row-actions">
              <a class="btn primary" href="${CTX}/volunteer/offer.do?volId=${v.id}">도와주기 제안</a>
              <a class="btn ghost"   href="${CTX}/volunteer/detail.do?id=${v.id}">상세</a>
            </div></td>`;
          tbody.appendChild(tr);
        });
      } else {
        items.forEach(v=>{
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td data-label="번호">${v.id}</td>
            <td data-label="제목"><a class="row-link" href="${CTX}/notice/detail.do?id=${v.id}">${v.title}</a></td>
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
  </script>
</body>
</html>
