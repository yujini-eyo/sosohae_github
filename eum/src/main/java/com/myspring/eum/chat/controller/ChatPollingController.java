package com.myspring.eum.chat.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
// RequestMapping, PathVariable, ResponseBody 등
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myspring.eum.chat.poll.ChatPollMapper;
import com.myspring.eum.chat.poll.ChatRow;

@Controller
@RequestMapping("/api/chat")
public class ChatPollingController {

  @Autowired private ChatPollMapper mapper;

  // 전송
  @RequestMapping(
      value="/{roomId}/send",
      method=RequestMethod.POST,
      consumes="application/json",
      produces="application/json;charset=UTF-8")
  @ResponseBody
  public Map<String,Object> send(@PathVariable("roomId") Long roomId,
                                 @RequestBody Map<String,Object> body,
                                 HttpSession session) {

    Object m = session.getAttribute("member"); // 예) MemberVO
    String me = (m != null) ? getMemberId(m) : String.valueOf(body.get("senderId"));

    if (me != null && me.length() > 0) {
      if (mapper.existsRoomMember(roomId, me) == 0) {
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("ok", false);
        res.put("error", "NOT_MEMBER");
        return res;
      }
    }

    ChatRow row = new ChatRow();
    row.setRoomId(roomId);
    row.setSenderId(me);
    row.setBody(String.valueOf(body.get("content")));
    mapper.insertMessage(row);

    Map<String,Object> res = new HashMap<String,Object>();
    res.put("ok", true);
    res.put("id", row.getMsgId());
    return res;
  }

  // 동기화: 배열로 바로 반환 (JS와 맞춤)
  @RequestMapping(
      value="/{roomId}/sync",
      method=RequestMethod.GET,
      produces="application/json;charset=UTF-8")
  @ResponseBody
  public List<ChatRow> sync(@PathVariable("roomId") Long roomId,
                            @RequestParam(value="afterId", required=false) Long afterId) {
    if (afterId == null) afterId = 0L;
    return mapper.listAfterId(roomId, afterId);
  }

  // 내 방 목록
  @RequestMapping(
		    value="/my-rooms",
		    method=RequestMethod.GET,
		    produces="application/json;charset=UTF-8")
		@ResponseBody
		public List<Map<String,Object>> myRooms(HttpSession session) {
		  Object m = session.getAttribute("member");
		  String me = null;
		  try { if (m != null) me = (String)m.getClass().getMethod("getId").invoke(m); } catch (Exception ignore) {}
		  if (me == null) me = "guest";

		  List<Map<String,Object>> rows = mapper.listMyRooms(me);

		  for (Map<String,Object> row : rows) {
		    fixText(row, "partnerName");
		    fixText(row, "partnerId");
		    fixText(row, "type");
		    fixText(row, "lastMsg");
		    // lastAt/roomId는 숫자라 건드리지 않음
		  }
		  return rows;
		}

		private static void fixText(Map<String,Object> row, String key) {
		  Object v = row.get(key);
		  if (v instanceof byte[]) {
		    row.put(key, new String((byte[]) v, StandardCharsets.UTF_8));
		  } else if (v != null) {
		    row.put(key, String.valueOf(v)); // 혹시 모를 Number/Clob 등도 문자열화
		  }
		}
  
  @RequestMapping(
		    value="/probe",
		    method=RequestMethod.GET,
		    produces="application/json;charset=UTF-8")
		@ResponseBody
		public Map<String,Object> probe() {
		  Map<String,Object> m = new java.util.LinkedHashMap<String,Object>();
		  m.put("plain", "한글 ABC");
		  m.put("id", "cong4");
		  m.put("ts", System.currentTimeMillis());
		  return m;
		}

  // 세션 VO에서 id 가져오기
  private String getMemberId(Object memberVO){
    try { return (String) memberVO.getClass().getMethod("getId").invoke(memberVO); }
    catch (Exception e) { return null; }
  }
}