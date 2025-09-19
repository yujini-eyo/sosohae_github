package com.myspring.eum.chat.dto;

import java.time.Instant;

public class ChatMessage {
  public enum Type { CHAT, JOIN, LEAVE, TYPING }
  private Type type;
  private String roomId, senderId, senderName, content;
  private Instant timestamp = Instant.now();
  // getters/setters ...
}