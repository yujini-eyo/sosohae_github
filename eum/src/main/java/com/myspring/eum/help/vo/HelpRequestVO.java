package com.myspring.eum.help.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.myspring.eum.help.vo.HelpType;
import com.myspring.eum.help.vo.Region;

public class HelpRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                 // PK
    private Long requesterId;        // �옉�꽦�옄(�꽭�뀡�뿉�꽌 二쇱엯)

    private HelpType type;           // �룞�뻾/留먮쿁/�옣蹂닿린/�젙由ъ젙�룉/�궛梨�_蹂댄뻾/湲고�
    private Region region;           // �꽌�슱/寃쎄린/...
    private LocalDate day;           // YYYY-MM-DD
    private LocalTime time;          // HH:mm
    private Urgency urgency;         // �씪諛�/湲닿툒

    private String title;            // �솕硫댁뿉�꽌 �깮�꽦/�닔�젙
    private Integer points;          // 沅뚯옣 �룷�씤�듃(�꽌踰꾩뿉�꽌 �옱怨꾩궛 沅뚯옣)

    private LocalDateTime scheduledAt; // day + time (�젙�젹/寃��깋�슜)
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

    // �렪�쓽 �깮�꽦�옄: �솕硫� �븘�닔 �븘�뱶留�
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

    /** day/time�씠 �뀑�똿�뤌 �엳�쓣 �븣 scheduledAt �룞湲고솕 */
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

