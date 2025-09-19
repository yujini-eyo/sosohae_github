package com.myspring.eum.chat.poll;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ChatPollMapper {
  void insertMessage(ChatRow row);
  List<ChatRow> listAfterId(@Param("roomId") Long roomId,
                            @Param("afterId") Long afterId);

  // (선택) 방 멤버십 검증
  int existsRoomMember(@Param("roomId") Long roomId,
                       @Param("memberId") String memberId);
  
  List<Map<String,Object>> listMyRooms(@Param("memberId") String memberId);
  
  Long findDirectRoomBetween(@Param("a") String a, @Param("b") String b);
  void insertDirectRoom(Map<String,Object> param); // param.put("roomId")로 생성된 id 받음
  void insertRoomMember(@Param("roomId") Long roomId, @Param("memberId") String memberId);
}