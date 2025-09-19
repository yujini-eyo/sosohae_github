(function () {
  if (window.__CHAT_BOOTED__) return;
  window.__CHAT_BOOTED__ = true;

  var app = document.getElementById("chatApp");
  if (!app) return;

  // data-* 읽기 (JSP에서 내려줌)
  var CTX    = app.getAttribute("data-base")   || "";     // 예: /eum
  var ROOM   = app.getAttribute("data-room")   || "";
  var SENDER = app.getAttribute("data-sender") || "guest";// 예: cong4
  
  var qs    = new URLSearchParams(location.search);
  var PNAME = qs.get("partner"); // 목록에서 ?partner=... 로 넘어온 값

  function setTitles(name){
	  var desktopH = document.querySelector("#chatPanelDesk > header");
	  var mobileH  = document.querySelector("#chatSheet > header");
	  var base     = name ? name : (ROOM ? ("방 #" + ROOM) : "대화상대");
	  var label    = base + "님과 대화";
	  if (desktopH) desktopH.textContent = "💬 " + label;
	  if (mobileH)  mobileH.textContent  = "💬 " + label;
	}

  if (PNAME && PNAME.length){
    // URLSearchParams는 이미 디코딩된 문자열을 줌
    setTitles(PNAME);
  } else {
    // 직접 진입 대비: 내 방 목록에서 찾아서 채우기
    fetch((app.getAttribute("data-base")||"") + "/api/chat/my-rooms", { credentials:"same-origin" })
      .then(function(r){ return r.json(); })
      .then(function(list){
        var me = (list||[]).find(function(x){ return String(x.roomId) === String(ROOM); });
        var name = me && (me.partnerName || me.partnerId);
        setTitles(name);
      })
      .catch(function(){ setTitles(null); });
  }
  
  if (!ROOM) return;

  function trimSlash(s){ return (s||"").replace(/\/+$/,""); }
  var API_BASE = trimSlash(CTX) + "/api/chat/" + encodeURIComponent(ROOM);

  // --- 화면 유틸 ---
  var boxes = [];
  var b1 = document.getElementById("chatBoxDesk");
  var b2 = document.getElementById("chatBoxMob");
  if (b1) boxes.push(b1);
  if (b2) boxes.push(b2);

  function appendAll(who, text, kind){
    if (!text) return;
    for (var i=0;i<boxes.length;i++){
      var box = boxes[i];
      var el = document.createElement("div");
      el.className = "msg " + (kind==="system" ? "system" : (who==="me" ? "me" : "other"));
      el.appendChild(document.createTextNode(text));
      box.appendChild(el);
      var sc = box.parentNode || box;
      sc.scrollTop = sc.scrollHeight;
    }
  }

  // --- 메시지 키 파싱(보수적으로) ---
  function notNull(v){ return !(v===undefined || v===null); }

  function getIdFrom(m){
    if (!m) return 0;
    if (notNull(m.msgId))  return Number(m.msgId);
    if (notNull(m.id))     return Number(m.id);
    if (notNull(m.MSG_ID)) return Number(m.MSG_ID);
    if (notNull(m.msg_id)) return Number(m.msg_id);
    return 0;
  }
  function getSenderFrom(m){
    if (!m) return "";
    if (notNull(m.senderId))   return String(m.senderId);
    if (notNull(m.sender))     return String(m.sender);
    if (notNull(m.SENDER_ID))  return String(m.SENDER_ID);
    if (notNull(m.sender_id))  return String(m.sender_id);
    return "";
  }
  function getBodyFrom(m){
    if (!m) return "";
    if (notNull(m.body))    return String(m.body);
    if (notNull(m.content)) return String(m.content);
    if (notNull(m.text))    return String(m.text);
    return "";
  }

  // --- 폴링 ---
  var lastId = 0;
  var syncing = false;

  function schedule(){ setTimeout(sync, 1500); }

  function sync(){
    if (syncing) return;
    syncing = true;

    fetch(API_BASE + "/sync?afterId=" + lastId, {
      method: "GET",
      headers: { "Accept": "application/json" },
      credentials: "same-origin"
    })
    .then(function(res){
      if (!res.ok) throw new Error(res.status);
      return res.text();
    })
    .then(function(text){
      var payload;
      try { payload = JSON.parse(text); } catch(e){ payload = []; }
      var list = Object.prototype.toString.call(payload) === "[object Array]"
                  ? payload
                  : (payload && payload.messages) ? payload.messages : [];
      for (var i=0;i<list.length;i++){
        var m = list[i];
        var id  = getIdFrom(m);
        var who = (getSenderFrom(m) === SENDER) ? "me" : "other";
        var msg = getBodyFrom(m);
        if (id > lastId) lastId = id;
        appendAll(who, msg);
      }
    })
    .catch(function(){ /* 무시 */ })
    .then(function(){ syncing = false; schedule(); }, function(){ syncing = false; schedule(); });
  }

  // --- 전송 ---
  function send(text){
    if (!text || !text.trim()) return;
    fetch(API_BASE + "/send", {
      method: "POST",
      headers: { "Content-Type": "application/json", "Accept":"application/json" },
      credentials: "same-origin",
      body: JSON.stringify({ senderId: SENDER, content: text })
    })
    .then(function(res){ return res.text(); })
    .then(function(t){
      var res; try { res = JSON.parse(t); } catch(e){ res = {}; }
      if (res && res.ok === false && res.error) {
        appendAll("system", "전송 실패: " + res.error, "system");
      }
    })
    .catch(function(err){ console.error("send failed", err); });
  }

  // --- 입력/버튼 ---
  var inputs = [];
  var i1 = document.getElementById("msgInputDesk");
  var i2 = document.getElementById("msgInputMob");
  if (i1) inputs.push(i1);
  if (i2) inputs.push(i2);

  var buttons = [];
  var bt1 = document.getElementById("sendBtnDesk");
  var bt2 = document.getElementById("sendBtnMob");
  if (bt1) buttons.push(bt1);
  if (bt2) buttons.push(bt2);

  for (var k=0;k<inputs.length;k++){
    (function(ip){
      ip.addEventListener("keydown", function(e){
        if (e.key === "Enter"){
          var t = (ip.value || "").trim();
          ip.value = "";
          send(t);
        }
      });
    })(inputs[k]);
  }

  for (var j=0;j<buttons.length;j++){
    (function(idx, bt){
      bt.addEventListener("click", function(){
        var ip = inputs[idx] || inputs[0];
        var t  = (ip && ip.value) ? ip.value.trim() : "";
        if (ip) ip.value = "";
        send(t);
      });
    })(j, buttons[j]);
  }
  
  (function(){
  var sheet  = document.getElementById('chatSheet');
  var handle = document.getElementById('sheetHandle');
  if(!sheet || !handle) return;

  // 스냅 포인트(보이는 높이 기준) -> offset(vh)로 환산해서 씀
  // 보이는 높이 85vh(거의 전체), 55vh(중간), 20vh(작게)
  var SNAP_VISIBLE = [85, 55, 20];         // 보이는 높이(vh)
  function visibleToOffset(vh){ return Math.max(0, 100 - vh); }
  var SNAP_OFFSET  = SNAP_VISIBLE.map(visibleToOffset);

  // 초기 위치: 중간(= 55vh 보이기 ⇒ offset 45vh)
  var currentOffset = visibleToOffset(55);
  setOffset(currentOffset);

  var dragging = false, startY = 0, startOffset = 0;

  function vhFromPx(px){ return (px / window.innerHeight) * 100; }
  function clamp(v, min, max){ return Math.max(min, Math.min(max, v)); }

  function setOffset(vh){
    currentOffset = clamp(vh, 0, 90); // 너무 많이/적게 내려가지 않도록
    sheet.style.setProperty('--sheet-offset', currentOffset + 'vh');
  }

  function onStart(y){
    dragging = true;
    startY = y;
    startOffset = currentOffset;
    sheet.classList.add('is-dragging');
  }
  function onMove(y){
    if(!dragging) return;
    var dy = y - startY;                  // +: 아래로 드래그
    var deltaVh = vhFromPx(dy);
    setOffset(startOffset + deltaVh);
  }
  function onEnd(){
    if(!dragging) return;
    dragging = false;
    sheet.classList.remove('is-dragging');
    // 가장 가까운 스냅 포인트로 붙이기
    var nearest = SNAP_OFFSET.reduce(function(best, v){
      return (Math.abs(v - currentOffset) < Math.abs(best - currentOffset)) ? v : best;
    }, SNAP_OFFSET[0]);
    setOffset(nearest);
  }

  // 마우스
  handle.addEventListener('mousedown', function(e){
    e.preventDefault();
    onStart(e.clientY);
    window.addEventListener('mousemove', onMouseMove);
    window.addEventListener('mouseup', onMouseUp, { once:true });
  });
  function onMouseMove(e){ onMove(e.clientY); }
  function onMouseUp(){ 
    window.removeEventListener('mousemove', onMouseMove);
    onEnd();
  }

  // 터치
  handle.addEventListener('touchstart', function(e){
    var t = e.touches[0];
    onStart(t.clientY);
  }, {passive:true});
  handle.addEventListener('touchmove', function(e){
    // 시트 드래그 중에는 페이지 스크롤을 막아준다
    if(dragging) e.preventDefault();
    var t = e.touches[0];
    onMove(t.clientY);
  }, {passive:false});
  handle.addEventListener('touchend', function(){ onEnd(); });

  // 헤더를 탭하면 (— 라인 포함) 토글: 중간 <-> 거의 전체
  var header = sheet.querySelector('header');
  if(header){
    header.addEventListener('click', function(){
      var big = SNAP_OFFSET[0], mid = SNAP_OFFSET[1];
      setOffset( Math.abs(currentOffset - big) < 1 ? mid : big );
    });
  }

  // 화면 회전/리사이즈 시 현재 비율 유지
  window.addEventListener('resize', function(){ setOffset(currentOffset); });
})();

  // 시작
  sync();
})();