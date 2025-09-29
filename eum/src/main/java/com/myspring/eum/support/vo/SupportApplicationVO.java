package com.myspring.eum.support.vo;

import java.sql.Timestamp;

/** support_application 테이블 1:1 매핑 VO */
public class SupportApplicationVO {
	private Long applicationId; // BIGINT PK (application_id)
	private Integer articleNo; // INT FK (board_article.articleNO)
	private String volunteerId; // VARCHAR(50)
	private String status; // VARCHAR(20): APPLIED/SELECTED/REJECTED/WITHDRAWN
	private String message; // VARCHAR(500)
	private Timestamp createdAt; // TIMESTAMP
	private Timestamp updatedAt; // TIMESTAMP
	private String articleTitle; // ba.title AS title -> resultMap에서 property=articleTitle
	private String ownerId; // ba.id AS owner_id -> resultMap에서 property=ownerId

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getArticleNO() {
		return articleNo;
	}

	public void setArticleNO(Integer articleNO) {
		this.articleNo = articleNO;
	}

	public String getVolunteerId() {
		return volunteerId;
	}

	public void setVolunteerId(String volunteerId) {
		this.volunteerId = volunteerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
