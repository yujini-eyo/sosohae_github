 (function () {
      const $ = (s, p=document)=>p.querySelector(s);
      const rows = $('#rows');
      const status = $('#status');

      // JSON URL: 같은 쿼리스트링을 유지(필터가 있으면 함께 전달)
      const jsonUrl = new URL('<c:url value="/board/listArticlesJson.do"/>', location.origin);
      if (location.search) jsonUrl.search = location.search;

      function fmtDate(v){
        if(!v) return '';
        try{
          const d = isNaN(v) ? new Date(v) : new Date(Number(v));
          if (isNaN(d.getTime())) return v;
          const y=d.getFullYear();
          const m=String(d.getMonth()+1).padStart(2,'0');
          const day=String(d.getDate()).padStart(2,'0');
          const hh=String(d.getHours()).padStart(2,'0');
          const mm=String(d.getMinutes()).padStart(2,'0');
          return `${y}-${m}-${day} ${hh}:${mm}`;
        }catch(e){ return v; }
      }

      function escapeHtml(s){
        return String(s).replace(/[&<>"']/g, m=>({ '&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;' }[m]));
      }

      function toList(json){
        // 1) {data:[…]} 2) {list:[…]} 3) […] 모두 대응
        if (Array.isArray(json)) return json;
        if (json && Array.isArray(json.data)) return json.data;
        if (json && Array.isArray(json.list)) return json.list;
        return [];
      }

      function render(list){
        rows.innerHTML = '';
        if(!list || list.length===0){
          rows.innerHTML = `<tr><td class="empty" colspan="6">표시할 데이터가 없습니다.</td></tr>`;
          return;
        }
        const frag = document.createDocumentFragment();
        list.forEach(a=>{
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${a.articleNO ?? ''}</td>
            <td>
              <a href="<c:url value='/board/viewArticle.do'/>?articleNO=${encodeURIComponent(a.articleNO ?? '')}">
                ${a.title ? escapeHtml(a.title) : '(제목 없음)'}
              </a>
            </td>
            <td>${a.id ? escapeHtml(a.id) : ''}</td>
            <td>${fmtDate(a.writeDate || a.createdAt || a.req_at)}</td>
            <td>${a.region ?? ''}</td>
            <td>${a.urgency ?? ''}</td>
          `;
          frag.appendChild(tr);
        });
        rows.appendChild(frag);
      }

      fetch(jsonUrl.toString(), { headers: { 'Accept':'application/json' }})
        .then(r=>{
          if(!r.ok) throw new Error('HTTP '+r.status);
          status.textContent = '불러옴';
          return r.json();
        })
        .then(json => render(toList(json)))
        .catch(err=>{
          console.error(err);
          status.textContent = '오류';
          rows.innerHTML = `<tr><td class="empty" colspan="6">데이터를 불러오지 못했습니다.</td></tr>`;
        });
    })();