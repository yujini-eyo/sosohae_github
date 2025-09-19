(function () {
  if (window.ChatModule) return;

  function el(id){ return document.getElementById(id); }

  function create(opts){
    var CTX     = opts.base || "";
    var sender  = opts.sender || "guest";
    var box     = opts.box;      // 메시지 컨테이너
    var input   = opts.input;    // 입력창
    var button  = opts.button;   // 전송 버튼

    var roomId  = null;
    var lastId  = 0;
    var timer   = null;
    var running = false;

    function apiBase() { return CTX + "/api/chat/" + roomId; }

    function clearBox(){ if (box) box.innerHTML = ""; }

    function append(who, text){
      if (!box || !text) return;
      var d = document.createElement("div");
      d.className = "msg " + (who === "me" ? "me" : "other");
      d.textContent = text;
      box.appendChild(d);
      box.scrollTop = box.scrollHeight;
    }

    function parseId(m){
      return Number((m && (m.msgId || m.id || m.MSG_ID || m.msg_id)) || 0);
    }
    function parseSender(m){
      return String((m && (m.senderId || m.sender_id || m.sender)) || "");
    }
    function parseBody(m){
      return String((m && (m.body || m.content || m.text)) || "");
    }

    function sync(){
      if (!running || !roomId) return;
      fetch(apiBase() + "/sync?afterId=" + lastId, {
        method: "GET",
        headers: { "Accept":"application/json" },
        credentials: "same-origin"
      })
      .then(function(res){ if(!res.ok) throw 0; return res.json(); })
      .then(function(list){
        (list || []).forEach(function(m){
          var id = parseId(m);
          lastId = Math.max(lastId, id);
          append(parseSender(m) === sender ? "me" : "other", parseBody(m));
        });
      })
      .catch(function(){ /* 네트워크 임시 오류는 무시 */ })
      .finally(function(){ timer = setTimeout(sync, 1500); });
    }

    function send(text){
      if (!roomId || !text) return;
      fetch(apiBase() + "/send", {
        method : "POST",
        headers: { "Content-Type":"application/json" },
        credentials: "same-origin",
        body   : JSON.stringify({ senderId: sender, content: text })
      }).catch(function(){/* 그대로 무시 */});
    }

    function bindInputs(){
      if (input){
        input.addEventListener("keydown", function(e){
          if (e.key === "Enter"){
            var t = (input.value || "").trim();
            input.value = "";
            send(t);
          }
        });
      }
      if (button){
        button.addEventListener("click", function(){
          var t = (input && input.value || "").trim();
          if (input) input.value = "";
          send(t);
        });
      }
    }

    bindInputs();

    return {
      start: function(newRoomId){
        if (String(newRoomId||"") === String(roomId||"")) return; // 같은 방이면 무시
        // 이전 폴링 정지
        if (timer) { clearTimeout(timer); timer = null; }
        running = false;

        // 상태 초기화
        roomId = String(newRoomId);
        lastId = 0;
        clearBox();

        // 첫 동기화
        running = true;
        sync();
      },
      stop: function(){
        running = false;
        if (timer) { clearTimeout(timer); timer = null; }
      },
      setSender: function(s){ sender = s || sender; },
      clear: clearBox
    };
  }

  window.ChatModule = { create: create };
})();