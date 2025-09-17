package com.myspring.eum.support.vo;

import java.sql.Timestamp;

public class SupportApplicationVO {
	 private int applicationId;
	    private int articleNo;
	    private String volunteerId;
	    private String status; // ENUM 타입은 String으로 처리
	    private String message;
	    private Timestamp createdAt;
	    private Timestamp updatedAt;
	    
	    // 조인해서 가져올 추가 정보 (필요 시)
	    private String volunteerNickname;

		public int getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(int applicationId) {
			this.applicationId = applicationId;
		}

		public int getArticleNo() {
			return articleNo;
		}

		public void setArticleNo(int articleNo) {
			this.articleNo = articleNo;
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

		public String getVolunteerNickname() {
			return volunteerNickname;
		}

		public void setVolunteerNickname(String volunteerNickname) {
			this.volunteerNickname = volunteerNickname;
		} 
}