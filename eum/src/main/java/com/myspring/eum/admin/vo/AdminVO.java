package com.myspring.eum.admin.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 관리자 계정 표현 VO
 * - 보통 member 테이블에서 role='ADMIN' 인 레코드를 맵핑해 사용합니다.
 * - 인증 자체는 MemberVO를 써도 되고, 관리자 화면 전용으로 이 VO를 사용할 수 있습니다.
 */
public class AdminVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 로그인 아이디 (member.id) */
    private String userId;

    /** 이름 */
    private String name;

    /** 이메일 */
    private String email;

    /** 역할 ("ADMIN" / "USER") */
    private String role;

    /** 상태 ("ACTIVE" / "BLOCKED" 등) */
    private String status;

    /** 생성/수정 시각 */
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AdminVO() {}

    /* ===== Getter / Setter ===== */

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    /* ===== 편의 메서드 ===== */

    /** 관리자 여부 */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /** 활성 상태 여부 */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminVO)) return false;
        AdminVO that = (AdminVO) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return userId == null ? 0 : userId.hashCode();
    }

    @Override
    public String toString() {
        return "AdminVO{userId='" + userId + "', role='" + role + "', status='" + status + "'}";
    }
}
