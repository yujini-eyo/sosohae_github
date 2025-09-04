(function(){
  'use strict';

  function $(sel){ return document.querySelector(sel); }

  var root = document.getElementById('chatApp') || document.body;
  var base = (root && root.getAttribute('data-base')) || '';   // 예: "/eum"
  var roomId = (root && root.getAttribute('data-room')) || 'global';

  var wsScheme = (location.protocol === 'https:') ? 'wss' : 'ws';
  var wsUrl = wsScheme + '://' + location.host + base + '/ws/chat/' + roomId;
  var ws = null;

  function connectWS(){
    try {
      ws = new WebSocket(wsUrl);
      ws.addEventListener('open',  function(){ console.log('[WS] connected:', wsUrl); });
      ws.addEventListener('close', function(){ console.log('[WS] closed'); });
      ws.addEventListener('error', function(e){ console.error('[WS] error', e); });
      ws.addEventListener('message', function(e){
        try {
          var data = JSON.parse(e.data);
          if (data && data.type === 'chat') appendMsg('other', data.text);
          else appendMsg('other', e.data);
        } catch (err) {
          appendMsg('other', e.data);
        }
      });
    } catch (e) { console.error('[WS] init failed', e); }
  }

  function appendMsg(cls, text){
    var sels = ['#chatBoxDesk','#chatBoxMob'];
    for (var i=0; i<sels.length; i++){
      var box = $(sels[i]);
      if(!box) continue;
      var el = document.createElement('div');
      el.className = 'msg ' + cls;
      el.textContent = text;
      box.appendChild(el);
      box.scrollTop = box.scrollHeight;
    }
  }

  function bindChat(sendBtnId, inputId){
    var btn = document.getElementById(sendBtnId);
    var input = document.getElementById(inputId);
    if(!btn || !input) return;

    function send(){
      var txt = (input.value || '').trim();
      if(!txt) return;
      appendMsg('me', txt);
      if (ws && ws.readyState === WebSocket.OPEN) {
        var payload = JSON.stringify({ type:'chat', text: txt, ts: Date.now() });
        ws.send(payload);
      }
      input.value = '';
    }
    btn.addEventListener('click', send);
    input.addEventListener('keydown', function(e){ if(e.key==='Enter') send(); });
  }

  // 데스크톱 리사이저(ES5)
  (function(){
    var split = $('#desktopSplit');
    var rz = $('#resizer');
    if(!split || !rz) return;
    var dragging=false, startX=0, startW=0;

    function px(n){ return n + 'px'; }
    function getLeftWidth(){
      var cols = getComputedStyle(split).gridTemplateColumns.split(' ');
      return parseFloat(cols[0]);
    }
    function setLeftWidth(n){
      var rootEl = document.querySelector('.chat-page') || document.documentElement;
      var cs = getComputedStyle(rootEl);
      var min = parseFloat(cs.getPropertyValue('--desktop-chat-w-min')) || 320;
      var max = parseFloat(cs.getPropertyValue('--desktop-chat-w-max')) || 640;
      var clamped = Math.max(min, Math.min(max, n));
      split.style.gridTemplateColumns = px(clamped) + ' var(--resizer-w) 1fr';
    }

    rz.addEventListener('mousedown', function(e){
      dragging=true; startX=e.clientX; startW=getLeftWidth();
      document.body.style.userSelect='none';
    });
    window.addEventListener('mousemove', function(e){
      if(!dragging) return;
      setLeftWidth(startW + (e.clientX - startX));
    });
    window.addEventListener('mouseup', function(){
      dragging=false; document.body.style.userSelect='';
    });
  })();

  // 모바일 바텀시트(ES5)
  (function(){
    var sheet = $('#chatSheet');
    var handle = $('#sheetHandle');
    if(!sheet || !handle) return;

    function vh(){ return Math.max(window.innerHeight, document.documentElement.clientHeight); }
    var mainEl = $('.main');
    var rootEl = document.querySelector('.chat-page') || document.documentElement;

    function cssVhVar(name, fallbackVh){
      var raw = getComputedStyle(rootEl).getPropertyValue(name).trim();
      var num = parseFloat(raw);
      return isNaN(num) ? fallbackVh * vh() : (raw.slice(-2)==='vh' ? (num/100)*vh() : num);
    }
    function getSnaps(){
      var mainH = mainEl ? mainEl.clientHeight : vh();
      var maxCap = Math.min(mainH - 8, 0.98 * vh());
      var minPx  = Math.max(80, cssVhVar('--sheet-snap-min', 0.20));
      var midPx  = Math.min(cssVhVar('--sheet-snap-mid', 0.70), maxCap);
      var maxPx  = Math.min(cssVhVar('--sheet-snap-max', 0.95), maxCap);
      return { minPx:minPx, midPx:midPx, maxPx:maxPx };
    }
    function setH(px){ sheet.style.height = Math.round(px) + 'px'; }

    function snap(to){
      var s = getSnaps();
      var target = (to==='min')? s.minPx : (to==='max')? s.maxPx : s.midPx;
      sheet.style.transition = 'height .18s ease, transform .18s ease';
      setH(target);
      sheet.setAttribute('aria-expanded', to==='max' ? 'true':'false');
    }

    var startY=0, startH=0, dragging=false;
    function onStart(y){ dragging=true; startY=y; startH=sheet.getBoundingClientRect().height; sheet.style.transition='none'; }
    function onMove(y){
      if(!dragging) return;
      var dy = startY - y;
      var s = getSnaps();
      var next = Math.max(s.minPx, Math.min(s.maxPx, startH + dy));
      setH(next);
    }
    function onEnd(){
      if(!dragging) return;
      dragging=false; sheet.style.transition='';
      var cur = sheet.getBoundingClientRect().height;
      var s = getSnaps();
      var upT = (s.midPx + s.maxPx) * 0.55;
      var downT = (s.minPx + s.midPx) * 0.45;
      if(cur >= upT) snap('max'); else if(cur <= downT) snap('min'); else snap('mid');
    }

    handle.addEventListener('touchstart', function(e){ onStart(e.touches[0].clientY); }, {passive:true});
    handle.addEventListener('touchmove',  function(e){ onMove(e.touches[0].clientY);  }, {passive:true});
    handle.addEventListener('touchend', onEnd);
    handle.addEventListener('mousedown', function(e){ onStart(e.clientY); });
    window.addEventListener('mousemove', function(e){ onMove(e.clientY); });
    window.addEventListener('mouseup', onEnd);

    var input = document.getElementById('msgInputMob');
    if(input){
      input.addEventListener('focus', function(){ snap('max'); });
      input.addEventListener('blur',  function(){ snap('mid'); });
    }

    window.addEventListener('resize', function(){
      var cur = sheet.getBoundingClientRect().height;
      var s = getSnaps();
      var d = [
        {k:'min', v:Math.abs(cur - s.minPx)},
        {k:'mid', v:Math.abs(cur - s.midPx)},
        {k:'max', v:Math.abs(cur - s.maxPx)}
      ].sort(function(a,b){ return a.v - b.v; })[0].k;
      snap(d);
    });

    snap('mid');
  })();

  function bindZoom(bgId, inId, outId, reId){
    var bg = document.getElementById(bgId);
    var zin= document.getElementById(inId);
    var zout= document.getElementById(outId);
    var rec= document.getElementById(reId);
    if(!bg || !zin || !zout || !rec) return;
    var scale=1;
    function apply(){ bg.style.transform = 'scale(' + scale + ')'; bg.style.transformOrigin='center'; }
    zin.addEventListener('click', function(){ scale = Math.min(2.0, scale+0.1); apply(); });
    zout.addEventListener('click', function(){ scale = Math.max(1.0, scale-0.1); apply(); });
    rec.addEventListener('click',  function(){ scale = 1.0; apply(); });
  }
  bindZoom('mapBgDesk','zoomInDesk','zoomOutDesk','recenterDesk');
  bindZoom('mapBgMobile','zoomInMob','zoomOutMob','recenterMob');

  bindChat('sendBtnDesk','msgInputDesk');
  bindChat('sendBtnMob','msgInputMob');
  connectWS();
})();