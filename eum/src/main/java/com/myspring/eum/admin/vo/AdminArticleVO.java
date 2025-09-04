package com.myspring.eum.admin.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;
import com.myspring.eum.board.vo.ArticleVO;

/**
 * 관리자 전용 게시글 VO
 * - ArticleVO를 확장해서 관리자 화면에서만 필요한 메타정보를 추가.
 * - 이미지 파일명 인/디코딩 로직은 ArticleVO의 구현을 그대로 상속.
 */
@Component("adminArticleVO")
public class AdminArticleVO extends ArticleVO {

    /** 게시글 고정(상단 고정) 여부 */
    private boolean pinned;

    /** 노출 여부 (VISIBLE / HIDDEN) */
    private String visibility;   // 예: "VISIBLE", "HIDDEN"

    /** 게시 상태 (DRAFT / PUBLISHED / ARCHIVED 등) */
    private String status;       // 예: "PUBLISHED"

    /** 조회수 */
    private int views;

    /** 감사(Audit) 필드 */
    private String createdBy;    // 최초 작성 관리자 ID
    private String updatedBy;    // 최근 수정 관리자 ID
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AdminArticleVO() {
        super();
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
