package com.eum.help.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HelpRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                 // PK
    private Long requesterId;        // 작성자(세션에서 주입)

    private HelpType type;           // 동행/말벗/장보기/정리정돈/산책_보행/기타
    private Region region;           // 서울/경기/...
    private LocalDate day;           // YYYY-MM-DD
    private LocalTime time;          // HH:mm
    private Urgency urgency;         // 일반/긴급

    private String title;            // 화면에서 생성/수정
    private Integer points;          // 권장 포인트(서버에서 재계산 권장)

    private LocalDateTime scheduledAt; // day + time (정렬/검색용)
    private RequestStatus status;      // OPEN/MATCHED/DONE/CANCELED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HelpRequestVO() {}

    public HelpRequestVO(Long id, Long requesterId, HelpType type, Region region,
                         LocalDate day, LocalTime time, Urgency urgency,
                         String title, Integer points, LocalDateTime scheduledAt,
                         RequestStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.type = type;
        this.region = region;
        this.day = day;
        this.time = time;
        this.urgency = urgency;
        this.title = title;
        this.points = points;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 편의 생성자: 화면 필수 필드만
    public HelpRequestVO(HelpType type, Region region, LocalDate day, LocalTime time,
                         Urgency urgency, String title, Integer points) {
        this.type = type;
        this.region = region;
        this.day = day;
        this.time = time;
        this.urgency = urgency;
        this.title = title;
        this.points = points;
        this.status = RequestStatus.OPEN;
        updateScheduledAt();
    }

    /** day/time이 셋팅돼 있을 때 scheduledAt 동기화 */
    public void updateScheduledAt() {
        if (this.day != null && this.time != null) {
            this.scheduledAt = LocalDateTime.of(this.day, this.time);
        }
    }

    // --- getters & setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public HelpType getType() { return type; }
    public void setType(HelpType type) { this.type = type; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; updateScheduledAt(); }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; updateScheduledAt(); }

    public Urgency getUrgency() { return urgency; }
    public void setUrgency(Urgency urgency) { this.urgency = urgency; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "HelpRequestVO{" +
                "id=" + id +
                ", requesterId=" + requesterId +
                ", type=" + type +
                ", region=" + region +
                ", day=" + day +
                ", time=" + time +
                ", urgency=" + urgency +
                ", title='" + title + '\'' +
                ", points=" + points +
                ", scheduledAt=" + scheduledAt +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

