package com.myspring.eum.board.vo;

import java.util.Date;

	public class ArticleVO {
	    private Integer articleNO;
	    private Integer parentNO;      // 없으면 0
	    private String id;             // 작성자 (세션)
	    private String title;
	    private String content;
	    private String imageFileName;
	    private java.sql.Timestamp writeDate;
	    private Boolean isNotice;      // 공지 여부

	    // 위저드 확장 필드
	    private String svcType;        // ex) 병원동행/장보기 등
	    private String region;
	    private java.sql.Timestamp reqAt;   // 요청일시
	    private String urgency;        // 일반/긴급 등
	    private Integer points;        // 포인트 (없으면 0)
		public Integer getArticleNO() 
		{
			return articleNO;
		}
		public void setArticleNO(Integer articleNO) {
			this.articleNO = articleNO;
		}
		public Integer getParentNO() {
			return parentNO;
		}
		public void setParentNO(Integer parentNO) {
			this.parentNO = parentNO;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getImageFileName() {
			return imageFileName;
		}
		public void setImageFileName(String imageFileName) {
			this.imageFileName = imageFileName;
		}
		public java.sql.Timestamp getWriteDate() {
			return writeDate;
		}
		public void setWriteDate(java.sql.Timestamp writeDate) {
			this.writeDate = writeDate;
		}
		public Boolean getIsNotice() {
			return isNotice;
		}
		public void setIsNotice(Boolean isNotice) {
			this.isNotice = isNotice;
		}
		public String getSvcType() {
			return svcType;
		}
		public void setSvcType(String svcType) {
			this.svcType = svcType;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public java.sql.Timestamp getReqAt() {
			return reqAt;
		}
		public void setReqAt(java.sql.Timestamp reqAt) {
			this.reqAt = reqAt;
		}
		public String getUrgency() {
			return urgency;
		}
		public void setUrgency(String urgency) {
			this.urgency = urgency;
		}
		public Integer getPoints() {
			return points;
		}
		public void setPoints(Integer points) {
			this.points = points;
		}
	}