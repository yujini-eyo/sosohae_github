/* points.js — 포인트 전용 인터랙션(탭, 진행도, 필터, 송금/교환 모달 & AJAX)
   - 서버 엔드포인트 예시:
     POST  ${ctx}/points/transfer.do   (to, amount, memo)   → {ok:true, current:12345}
     POST  ${ctx}/points/redeem.do     (rewardId, amount)   → {ok:true, current:12345}
     GET   ${ctx}/points/summary.do                         → {current, monthGain, monthSpend, totalGain, totalSpend, totalHours, tier, nextNeeded, goalTarget, goalNow}
     GET   ${ctx}/points/history.do                         → [{ts, reason, amount, type, status}, ...]
   - 엔드포인트가 아직 없으면 모달 입력만 동작하고, toast로 안내만 해요.
*/
(function(){
  'use strict';

  // ==== 유틸 ====
  const $  = (s, r=document)=>r.querySelector(s);
  const $$ = (s, r=document)=>Array.prototype.slice.call(r.querySelectorAll(s));
  const ctx = (document.querySelector('base') && document.querySelector('base').href) || (window.__CTX__ || (document.querySelector('meta[name="ctx"]')?.content) || (document.body.dataset?.ctx) || (window.location.pathname.split('/')[1] ? '/'+window.location.pathname.split('/')[1] : ''));

  const API = {
    transfer: ctx + '/points/transfer.do',
    redeem:   ctx + '/points/redeem.do',
    summary:  ctx + '/points/summary.do',
    history:  ctx + '/points/history.do',
  };

  function onlyNumber(s){ return (s||'').replace(/[^\d.-]/g,''); }
  function toInt(s){ const n = parseInt(onlyNumber(s), 10); return isNaN(n) ? 0 : n; }
  function fmt(n){ return (n||0).toLocaleString('ko-KR'); }

  // CSRF (Spring Security 표준 meta 또는 hidden input을 탐색)
  function getCsrf(){
    const metaToken = $('meta[name="_csrf"]')?.content;
    const metaHeader= $('meta[name="_csrf_header"]')?.content;
    const input     = document.querySelector('input[name="_csrf"]');
    return {
      header: metaHeader || 'X-CSRF-TOKEN',
      token: metaToken  || (input ? input.value : null)
    };
  }

  async function postForm(url, params){
    const headers = {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'};
    const csrf = getCsrf();
    if (csrf.token) headers[csrf.header] = csrf.token;
    const body = new URLSearchParams(params||{}).toString();
    const res  = await fetch(url, {method:'POST', headers, body});
    if (!res.ok) throw new Error('network');
    return await res.json().catch(()=>({ok:false}));
  }

  function toast(msg, ms=2000){
    let el = $('.points-toast');
    if (!el){ el = document.createElement('div'); el.className='points-toast'; document.body.appendChild(el); }
    el.textContent = msg;
    el.classList.add('show');
    setTimeout(()=>el.classList.remove('show'), ms);
  }

  // ==== 진행도 계산 ====
  function updateTierProgress(){
    const current = toInt($('#pt_current')?.textContent);
    const nextNeededText = $('#pt_next_text')?.textContent || '';
    const nextNeeded = toInt(nextNeededText); // "다음 등급까지 1,500 P"에서 수치만 추출
    const bar = $('#pt_progress');
    if (!bar) return;

    // 추정 방식: 현 등급 포인트 = current, 다음 등급까지 필요치 = nextNeeded
    // 전체 = current + nextNeeded → 진행도 = current / (current + nextNeeded)
    let pct = 0;
    if (current > 0 || nextNeeded > 0){
      pct = Math.max(0, Math.min(100, Math.round((current / (current + nextNeeded)) * 100)));
    }
    bar.style.width = pct + '%';
    bar.setAttribute('aria-valuenow', String(pct));
    bar.title = `다음 등급까지 진행도: ${pct}%`;
  }

  function updateGoalBar(){
    const target = toInt($('#goalTarget')?.textContent);
    const now    = toInt($('#goalNow')?.textContent);
    const bar    = $('#pt_goal_bar');
    if (!bar || !target) return;
    const pct = Math.max(0, Math.min(100, Math.round((now / target) * 100)));
    bar.style.width = pct + '%';
    bar.setAttribute('aria-valuenow', String(pct));
    bar.title = `이달 목표 진행도: ${pct}%`;
  }

  // ==== 탭 ====
  function initTabs(){
    const tabs = $$('.tab-btn');
    const panels = $$('.tab-panel');
    if (!tabs.length) return;
    // 저장된 탭 복원
    const KEY = 'points.activeTab';
    const saved = localStorage.getItem(KEY);
    if (saved){
      tabs.forEach(t => t.setAttribute('aria-selected', String(t.dataset.tab === saved)));
      panels.forEach(p => p.classList.toggle('active', p.id === 'panel-'+saved));
    }
    tabs.forEach(btn=>{
      btn.addEventListener('click', ()=>{
        const tab = btn.dataset.tab;
        tabs.forEach(b => b.setAttribute('aria-selected', String(b===btn)));
        panels.forEach(p => p.classList.toggle('active', p.id === 'panel-'+tab));
        localStorage.setItem(KEY, tab);
      });
    });
  }

  // ==== 내역 필터 ====
  function initFilter(){
    const sel = $('#typeSelect');
    const rows = $$('#pt_rows tr');
    if (!sel || !rows.length) return;
    sel.addEventListener('change', ()=>{
      const v = sel.value;
      rows.forEach(tr=>{
        const t = tr.getAttribute('data-type') || 'all';
        tr.style.display = (v==='all' || v===t) ? '' : 'none';
      });
    });
  }

  // ==== 모달 생성 ====
  function openModal({title, content, onOk, okText='확인', cancelText='취소'}){
    const wrap = document.createElement('div');
    wrap.className = 'points-modal';
    wrap.innerHTML = `
      <div class="points-dialog" role="dialog" aria-modal="true" aria-label="${title}">
        <h4>${title}</h4>
        <div class="row">${content}</div>
        <div class="actions">
          <button type="button" class="btn ghost" data-act="cancel">${cancelText}</button>
          <button type="button" class="btn primary" data-act="ok">${okText}</button>
        </div>
      </div>`;
    document.body.appendChild(wrap);
    const destroy = ()=>wrap.remove();
    wrap.addEventListener('click', e=>{ if (e.target === wrap) destroy(); });
    wrap.querySelector('[data-act="cancel"]').addEventListener('click', destroy);
    wrap.querySelector('[data-act="ok"]').addEventListener('click', async ()=>{
      try { await onOk?.(wrap); destroy(); } catch(e){ toast('처리 중 오류가 발생했어요.'); }
    });
  }

  // ==== 액션: 포인트 보내기 / 보상 교환 ====
  function initActions(){
    const sendBtn = $('#sendPoint');
    const redeemBtn = $('#redeemPoint');

    if (sendBtn){
      sendBtn.addEventListener('click', ()=>{
        openModal({
          title:'포인트 보내기',
          content: `
            <label>받는 아이디</label>
            <input class="input" id="modal_to" placeholder="상대 아이디" />
            <label>보낼 포인트</label>
            <input class="input" id="modal_amount" inputmode="numeric" placeholder="예: 500" />
            <label>메모(선택)</label>
            <textarea id="modal_memo" placeholder="전달 메모(선택)"></textarea>
          `,
          okText:'보내기',
          onOk: async (wrap)=>{
            const to = $('#modal_to', wrap).value.trim();
            const amount = parseInt($('#modal_amount', wrap).value.replace(/[^\d]/g,''),10) || 0;
            const memo = $('#modal_memo', wrap).value.trim();
            if (!to || amount<=0){ toast('아이디와 금액을 확인해 주세요.'); return; }

            try{
              const json = await postForm(API.transfer, {to, amount, memo});
              if (json?.ok){
                // 잔액 업데이트
                const curEl = $('#pt_current');
                if (curEl && typeof json.current === 'number') curEl.textContent = fmt(json.current) + ' P';
                toast('포인트를 보냈어요!');
                updateTierProgress();
              }else{
                toast(json?.message || '전송에 실패했어요.');
              }
            }catch(e){
              toast('서버 통신 오류입니다.');
            }
          }
        });
      });
    }

    if (redeemBtn){
      redeemBtn.addEventListener('click', ()=>{
        openModal({
          title:'보상 교환',
          content: `
            <label>보상 선택</label>
            <select id="modal_reward" class="input">
              <option value="giftcard_1000">기프트카드 1,000P</option>
              <option value="giftcard_5000">기프트카드 5,000P</option>
              <option value="coupon_2000">쿠폰 2,000P</option>
            </select>
            <label>사용 포인트</label>
            <input class="input" id="modal_amount2" inputmode="numeric" placeholder="예: 1000" />
          `,
          okText:'교환하기',
          onOk: async (wrap)=>{
            const rewardId = $('#modal_reward', wrap).value;
            const amount   = parseInt($('#modal_amount2', wrap).value.replace(/[^\d]/g,''),10) || 0;
            if (!amount){ toast('사용할 포인트를 입력해 주세요.'); return; }

            try{
              const json = await postForm(API.redeem, {rewardId, amount});
              if (json?.ok){
                const curEl = $('#pt_current');
                if (curEl && typeof json.current === 'number') curEl.textContent = fmt(json.current) + ' P';
                toast('교환이 완료됐어요!');
                updateTierProgress();
              }else{
                toast(json?.message || '교환에 실패했어요.');
              }
            }catch(e){
              toast('서버 통신 오류입니다.');
            }
          }
        });
      });
    }
  }

  // ==== 선택: 최초 진입 시 서버에서 최신 요약/내역 새로고침 ====
  async function optionalRefresh(){
    // 필요 시 외부에서 window.POINTS_FETCH_ON_LOAD = true 로 켜기
    if (!window.POINTS_FETCH_ON_LOAD) return;
    try{
      const s = await fetch(API.summary).then(r=>r.json());
      if (s){
        // 기본 KPI
        const cur = $('#pt_current'); if (cur) cur.textContent = fmt(s.current) + ' P';
        const mg  = $('#pt_month_gain'); if (mg) mg.textContent = '+'+fmt(s.monthGain||0);
        const ms  = $('#pt_month_spend'); if (ms) ms.textContent = '-'+fmt(s.monthSpend||0)+' P';
        const tg  = $('#pt_total_gain');  if (tg) tg.textContent = fmt(s.totalGain||0)+' P';
        const ts  = $('#pt_total_spend'); if (ts) ts.textContent = fmt(s.totalSpend||0)+' P';
        const th  = $('#pt_total_hours'); if (th) th.textContent = fmt(s.totalHours||0)+' h';
        const tier= $('#pt_tier'); if (tier && s.tier) tier.textContent = s.tier;
        const next= $('#pt_next_text'); if (next && typeof s.nextNeeded==='number') next.textContent = `다음 등급까지 ${fmt(s.nextNeeded)} P`;
        const gt  = $('#goalTarget'); if (gt && s.goalTarget!=null) gt.textContent = fmt(s.goalTarget);
        const gn  = $('#goalNow');    if (gn && s.goalNow!=null) gn.textContent = fmt(s.goalNow);

        updateTierProgress();
        updateGoalBar();
      }

      const tableBody = $('#pt_rows');
      if (tableBody){
        const h = await fetch(API.history).then(r=>r.json());
        if (Array.isArray(h)){
          tableBody.innerHTML = h.map(row=>{
            const date = new Date(row.ts);
            const pad = n=>String(n).padStart(2,'0');
            const ymdhm = `${date.getFullYear()}-${pad(date.getMonth()+1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
            const amount = (row.type==='earn' ? '+' : '-') + fmt(row.amount||0) + ' P';
            const typeKo = row.type==='earn'?'적립':(row.type==='spend'?'사용':(row.type==='donate'?'기부':'송금'));
            const badge  = `<span class="badge ${row.status==='done'?'success':(row.status==='pending'?'warn':'error')}">${row.status==='done'?'완료':(row.status==='pending'?'대기':'실패')}</span>`;
            return `<tr data-type="${row.type}">
                      <td>${ymdhm}</td>
                      <td>${escapeHtml(row.reason||'')}</td>
                      <td>${amount}</td>
                      <td>${typeKo}</td>
                      <td>${badge}</td>
                    </tr>`;
          }).join('');
        }
      }

    }catch(e){
      // 조용히 무시 (서버 준비 안 된 경우)
    }
  }

  function escapeHtml(s){
    return String(s).replace(/[&<>"']/g, m=>({ '&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;', "'":'&#39;' }[m]));
  }

  // ==== 부팅 ====
  document.addEventListener('DOMContentLoaded', ()=>{
    initTabs();
    initFilter();
    initActions();
    updateTierProgress();
    updateGoalBar();
    optionalRefresh();
  });
})();
