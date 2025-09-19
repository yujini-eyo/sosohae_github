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
    setTitles(PNAME);
  } else {
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

  // --- 채팅 폴링 ---
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

  // --- 채팅 전송 ---
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
  
  // --- 모바일 시트 드래그 (생략 없이 유지) ---
  (function(){
    var sheet  = document.getElementById('chatSheet');
    var handle = document.getElementById('sheetHandle');
    if(!sheet || !handle) return;

    var SNAP_VISIBLE = [85, 55, 20];
    function visibleToOffset(vh){ return Math.max(0, 100 - vh); }
    var SNAP_OFFSET  = SNAP_VISIBLE.map(visibleToOffset);
    var currentOffset = visibleToOffset(55);
    setOffset(currentOffset);

    var dragging = false, startY = 0, startOffset = 0;
    function vhFromPx(px){ return (px / window.innerHeight) * 100; }
    function clamp(v, min, max){ return Math.max(min, Math.min(max, v)); }
    function setOffset(vh){
      currentOffset = clamp(vh, 0, 90);
      sheet.style.setProperty('--sheet-offset', currentOffset + 'vh');
    }
    function onStart(y){
      dragging = true; startY = y; startOffset = currentOffset;
      sheet.classList.add('is-dragging');
    }
    function onMove(y){
      if(!dragging) return;
      var dy = y - startY;
      var deltaVh = vhFromPx(dy);
      setOffset(startOffset + deltaVh);
    }
    function onEnd(){
      if(!dragging) return;
      dragging = false; sheet.classList.remove('is-dragging');
      var nearest = SNAP_OFFSET.reduce(function(best, v){
        return (Math.abs(v - currentOffset) < Math.abs(best - currentOffset)) ? v : best;
      }, SNAP_OFFSET[0]);
      setOffset(nearest);
    }
    handle.addEventListener('mousedown', function(e){
      e.preventDefault(); onStart(e.clientY);
      window.addEventListener('mousemove', onMouseMove);
      window.addEventListener('mouseup', onMouseUp, { once:true });
    });
    function onMouseMove(e){ onMove(e.clientY); }
    function onMouseUp(){ window.removeEventListener('mousemove', onMouseMove); onEnd(); }
    handle.addEventListener('touchstart', function(e){
      var t = e.touches[0]; onStart(t.clientY);
    }, {passive:true});
    handle.addEventListener('touchmove', function(e){
      if(dragging) e.preventDefault();
      var t = e.touches[0]; onMove(t.clientY);
    }, {passive:false});
    handle.addEventListener('touchend', function(){ onEnd(); });
    var header = sheet.querySelector('header');
    if(header){
      header.addEventListener('click', function(){
        var big = SNAP_OFFSET[0], mid = SNAP_OFFSET[1];
        setOffset( Math.abs(currentOffset - big) < 1 ? mid : big );
      });
    }
    window.addEventListener('resize', function(){ setOffset(currentOffset); });
  })();

  /* === 위치공유 추가 시작 === */
  var LOC_UPDATE_URL = (CTX || "") + "/loc/update";
  var LOC_LIST_URL   = (CTX || "") + "/loc/list";

  var mapDesk=null, mapMob=null, meMarker=null;
  var locMarkers = {}; // userId -> google.maps.Marker
  var lastLocPollTs = 0;
  var locPollTimer = null;
  var uploadMinGapMs = 1000;
  var pollMs        = 3000;
  var lastSentAt    = 0;
  var DEFAULT_CENTER = { lat:37.5665, lng:126.9780 };

  function waitForMaps(cb, tries){
    tries = tries || 0;
    if (window.google && window.google.maps){ cb(); return; }
    if (tries > 50) return;
    setTimeout(function(){ waitForMaps(cb, tries+1); }, 100);
  }

  function initMaps(){
    var d = document.getElementById("gmapDesk");
    var m = document.getElementById("gmapMob");
    if (d){
      mapDesk = new google.maps.Map(d, { center: DEFAULT_CENTER, zoom: 14 });
      var zin = document.getElementById("zoomInDesk");
      var zout= document.getElementById("zoomOutDesk");
      var rec = document.getElementById("recenterDesk");
      if (zin)  zin.addEventListener("click",  function(){ mapDesk.setZoom(mapDesk.getZoom()+1); });
      if (zout) zout.addEventListener("click",  function(){ mapDesk.setZoom(mapDesk.getZoom()-1); });
      if (rec)  rec.addEventListener("click",   function(){ recenter(mapDesk); });
    }
    if (m){
      mapMob = new google.maps.Map(m, { center: DEFAULT_CENTER, zoom: 14 });
      var zin2 = document.getElementById("zoomInMob");
      var zout2= document.getElementById("zoomOutMob");
      var rec2 = document.getElementById("recenterMob");
      if (zin2)  zin2.addEventListener("click", function(){ mapMob.setZoom(mapMob.getZoom()+1); });
      if (zout2) zout2.addEventListener("click", function(){ mapMob.setZoom(mapMob.getZoom()-1); });
      if (rec2)  rec2.addEventListener("click",  function(){ recenter(mapMob); });
    }
  }

  function upsertMe(lat, lng){
    var pos = new google.maps.LatLng(lat, lng);
    if (!meMarker){
      var map = mapDesk || mapMob;
      if (!map) return;
      meMarker = new google.maps.Marker({ position: pos, map: map, title:"나" });
      map.panTo(pos);
    } else {
      meMarker.setPosition(pos);
    }
  }

  function upsertLocMarker(userId, lat, lng){
    if (userId === SENDER) return;
    var pos = new google.maps.LatLng(lat, lng);
    var m = locMarkers[userId];
    var map = mapDesk || mapMob;
    if (!m){
      if (!map) return;
      m = new google.maps.Marker({ position: pos, map: map, title: userId });
      locMarkers[userId] = m;
    } else {
      m.setPosition(pos);
    }
  }

  function recenter(map){
    if (!map) return;
    if (meMarker) map.panTo(meMarker.getPosition());
    else map.panTo(DEFAULT_CENTER);
  }

  var watchId = null;
  function startLocationShare(){
    if (!navigator.geolocation){ alert("이 브라우저는 위치 공유를 지원하지 않습니다."); return; }
    watchId = navigator.geolocation.watchPosition(function(pos){
      var t = Date.now();
      if (t - lastSentAt < uploadMinGapMs) return;
      lastSentAt = t;
      var lat = pos.coords.latitude, lng = pos.coords.longitude;
      upsertMe(lat, lng);
      fetch(LOC_UPDATE_URL, {
        method: "POST",
        headers: { "Content-Type":"application/json", "Accept":"application/json" },
        credentials:"same-origin",
        body: JSON.stringify({ roomId: ROOM, userId: SENDER, lat: lat, lng: lng, accuracy: pos.coords.accuracy, ts: t })
      }).catch(function(){});
    }, { enableHighAccuracy:true, maximumAge:5000, timeout:10000 });
  }

  function stopLocationShare(){
    if (watchId !== null){ navigator.geolocation.clearWatch(watchId); watchId = null; }
  }

  function pollLocations(){
    var url = LOC_LIST_URL + "?roomId=" + encodeURIComponent(ROOM);
    if (lastLocPollTs) url += "&since=" + lastLocPollTs;
    fetch(url, { headers:{ "Accept":"application/json" }, credentials:"same-origin" })
      .then(function(r){ return r.text(); })
      .then(function(t){
        var list; try { list = JSON.parse(t); } catch(e){ list = []; }
        if (!list || !list.length) return;
        var maxTs = lastLocPollTs;
        for (var i=0;i<list.length;i++){
          var d = list[i];
          if (d && typeof d.ts === "number" && d.ts > maxTs) maxTs = d.ts;
          if (d && typeof d.lat === "number" && typeof d.lng === "number"){
            upsertLocMarker(d.userId, d.lat, d.lng);
          }
        }
        lastLocPollTs = maxTs || Date.now();
      })
      .catch(function(){});
  }

  function ensureLocControls(){
    if (document.getElementById("locControls")) return;
    var bar = document.createElement("div");
    bar.id = "locControls";
    bar.style.cssText = "position:fixed;top:12px;right:12px;z-index:9999;background:rgba(255,255,255,.95);border:1px solid #ddd;border-radius:10px;box-shadow:0 4px 10px rgba(0,0,0,.08);padding:8px 10px;display:flex;gap:8px;align-items:center;font:13px/1.4 SUIT,system-ui,Arial";
    bar.innerHTML =
      '<button id="btnLocShare" type="button" style="padding:6px 10px;border:1px solid #ccc;border-radius:8px;background:#fff;">내 위치 공유 시작</button>'+
      '<label>폴링 <select id="locPollSec" style="padding:4px 6px;border:1px solid #ccc;border-radius:6px;"><option value="2">2초</option><option value="3" selected>3초</option><option value="5">5초</option></select></label>';
    document.body.appendChild(bar);

    document.getElementById("btnLocShare").addEventListener("click", function(){
      if (!watchId){ startLocationShare(); this.textContent = "내 위치 공유 중지"; }
      else         { stopLocationShare();  this.textContent = "내 위치 공유 시작"; }
    });
    document.getElementById("locPollSec").addEventListener("change", function(){
      var v = parseInt(this.value, 10); if (isNaN(v)||v<1) v=3;
      pollMs = v * 1000;
      if (locPollTimer){ clearInterval(locPollTimer); locPollTimer = null; }
      locPollTimer = setInterval(pollLocations, pollMs);
    });
  }

  function bootAll(){
    ensureLocControls();
    (function wait(){ // 구글맵 로딩 대기
      if (window.google && window.google.maps){
        initMaps();
        if (locPollTimer){ clearInterval(locPollTimer); }
        locPollTimer = setInterval(pollLocations, pollMs);
      } else {
        setTimeout(wait, 120);
      }
    })();
    // 채팅 폴링도 함께 시작
    sync();
  }
  /* === 위치공유 추가 끝 === */

  // 시작 지점 변경: sync() -> bootAll()
  bootAll();
})();