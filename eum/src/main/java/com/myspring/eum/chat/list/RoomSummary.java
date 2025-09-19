package com.myspring.eum.chat.list;

import java.util.Date;

public class RoomSummary {
  private Long roomId;
  private String otherId;     // 상대(1:1 기준)
  private String lastMsg;
  private Date  lastAt;

  // getter/setter
  public Long getRoomId() { return roomId; }
  public void setRoomId(Long roomId) { this.roomId = roomId; }
  public String getOtherId() { return otherId; }
  public void setOtherId(String otherId) { this.otherId = otherId; }
  public String getLastMsg() { return lastMsg; }
  public void setLastMsg(String lastMsg) { this.lastMsg = lastMsg; }
  public Date getLastAt() { return lastAt; }
  public void setLastAt(Date lastAt) { this.lastAt = lastAt; }
}