package com.myspring.eum.loc;

public class LocationDTO {
  private String roomId;
  private String userId;
  private double lat;
  private double lng;
  private Double accuracy; // nullable
  private long ts;         // epoch millis

  public String getRoomId() { return roomId; }
  public void setRoomId(String roomId) { this.roomId = roomId; }

  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }

  public double getLat() { return lat; }
  public void setLat(double lat) { this.lat = lat; }

  public double getLng() { return lng; }
  public void setLng(double lng) { this.lng = lng; }

  public Double getAccuracy() { return accuracy; }
  public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }

  public long getTs() { return ts; }
  public void setTs(long ts) { this.ts = ts; }
}