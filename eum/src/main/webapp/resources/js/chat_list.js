(function(){
  var app = document.getElementById("roomListApp");
  if (!app || window.__ROOM_LIST__) return; window.__ROOM_LIST__ = true;

  var CTX = app.dataset.base || "";
  var UL  = document.getElementById("roomList");

  function fmtTime(s){
    if(!s) return "";
    return String(s).replace("T", " ").slice(0,16);
  }

  function render(items){
	  if(!UL) return;
	  UL.innerHTML = "";
	  (items||[]).forEach(function(r){
	    var displayName = r.partnerName || r.partnerId || ('방 #'+r.roomId);

	    var li = document.createElement("li");
	    li.className = "room-item";
	    li.innerHTML =
	      '<a href="'+ CTX +'/chat?room='+ encodeURIComponent(r.roomId) +
	      '&partner=' + encodeURIComponent(displayName) +'">'+
	        '<div class="name">'+ displayName +'</div>'+
	        '<div class="time">'+ fmtTime(r.lastAt) +'</div>'+
	        '<div class="last">'+ (r.lastMsg || '') +'</div>'+
	      '</a>';
	    UL.appendChild(li);
	  });
	  if(!items || items.length===0){
	    UL.innerHTML = '<li class="room-item"><div>대화 목록이 없습니다.</div></li>';
	  }
	}

  fetch(CTX + "/api/chat/my-rooms", { credentials:"same-origin" })
    .then(function(res){ if(!res.ok) throw 0; return res.json(); })
    .then(render)
    .catch(function(){ render([]); });
})();