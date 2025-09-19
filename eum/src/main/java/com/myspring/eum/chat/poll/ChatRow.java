package com.myspring.eum.chat.poll;

import java.util.Date;

public class ChatRow {
  private Long msgId;
  private Long roomId;
  private String senderId;
  private String body;
  private Date sentAt;

  public Long getMsgId() { return msgId; }
  public void setMsgId(Long v) { this.msgId = v; }
  public Long getRoomId() { return roomId; }
  public void setRoomId(Long v) { this.roomId = v; }
  public String getSenderId() { return senderId; }
  public void setSenderId(String v) { this.senderId = v; }
  public String getBody() { return body; }
  public void setBody(String v) { this.body = v; }
  public Date getSentAt() { return sentAt; }
  public void setSentAt(Date v) { this.sentAt = v; }
}