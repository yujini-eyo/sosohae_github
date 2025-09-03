(() => {
  'use strict';

  const ready = (fn) => (document.readyState !== 'loading') ? fn() : document.addEventListener('DOMContentLoaded', fn);

  function fmtRegion(item){
    // 백엔드 필드명에 맞춰 수정 가능
    return item.region || item.regionName || item.area || '지역 미상';
  }
  function fmtPoint(item){
    const p = Number(item.points ?? item.rewardPoints ?? 0);
    return p > 0 ? `${p}P` : '';
  }
  function pickEmoji(item){
    const cat = (item.category || item.type || '').toLowerCase();
    if (cat.includes('장보기') || cat.includes('shop')) return '🛒';
    if (cat.includes('약') || cat.includes('pharm')) return '💊';
    if (cat.includes('전등') || cat.includes('light')) return '🪜';
    if (cat.includes('동행') || cat.includes('hospital')) return '🧭';
    if (cat.includes('산책') || cat.includes('walk')) return '🚶';
    if (cat.includes('청소') || cat.includes('clean')) return '🧹';
    return '🤝';
  }

  function render(postsBox, list){
    postsBox.innerHTML = '';
    if (!Array.isArray(list) || list.length === 0){
      postsBox.innerHTML = `<div class="post-card"><h4>자료 없음</h4><p>등록된 게시글이 없습니다.</p></div>`;
      return;
    }
    list.slice(0,3).forEach(it => {
      const url = it.url || (it.articleNO ? `${postsBox.dataset.base || ''}/board/viewArticle.do?articleNO=${it.articleNO}` : '#');
      const emoji = pickEmoji(it);
      const title = it.title || '(제목 없음)';
      const region = fmtRegion(it);
      const point  = fmtPoint(it);
      const meta   = [region, point].filter(Boolean).join(' · ');

      const card = document.createElement('a');
      card.className = 'post-card';
      card.href = url;
      card.innerHTML = `<h4>${emoji} ${title}</h4><p>${meta}</p>`;
      postsBox.appendChild(card);
    });
  }

  async function load(postsBox){
    const endpoint = postsBox.dataset.endpoint;
    if (!endpoint) return;

    try{
      const res = await fetch(endpoint, { headers: { 'Accept':'application/json' } , cache:'no-store' });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      // data가 {items:[]}|[] 둘 다 수용
      const list = Array.isArray(data) ? data : (data.items || data.list || []);
      render(postsBox, list);
    }catch(err){
      console.warn('recent fetch failed:', err);
      // 실패 폴백(정적 카드)
      render(postsBox, [
        { title:'🛒 마트 심부름', region:'서울 양천구 목동', rewardPoints:300 },
        { title:'💊 약 타기 요청', region:'서울 성북구 길음동', rewardPoints:200 },
        { title:'🪜 전등 교체',   region:'서울 중랑구 망우동', rewardPoints:150 }
      ]);
    }
  }

  ready(() => {
    const box = document.getElementById('recentPosts');
    if (!box) return;
    // contextPath가 필요하면 여기에 주입 가능
    box.dataset.base = document.querySelector('base')?.href || '';

    load(box);
    // 실시간 갱신(60초): 원치 않으면 지우세요
    setInterval(() => load(box), 60000);
  });
})();
