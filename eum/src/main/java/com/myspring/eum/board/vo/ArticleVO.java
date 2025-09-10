package com.myspring.eum.board.vo;

import java.util.Date;

public class ArticleVO {
	private int articleNO;
	private int parentNO;
	private String id;
	private String title;
	private String content;
	private String imageFileName;
	private Date writeDate;

	// 위저드 확장
	private String svcType;
	private String region;
	private Date   reqAt;       // DATETIME ←→ java.util.Date
	private String urgency;     // "일반" | "긴급"
	private Integer points;
	private Boolean isNotice;
	
	public int getArticleNO() {
		return articleNO;
	}
	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}
	public int getParentNO() {
		return parentNO;
	}
	public void setParentNO(int parentNO) {
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
	public Date getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
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
	public Date getReqAt() {
		return reqAt;
	}
	public void setReqAt(Date reqAt) {
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
	public Boolean getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(Boolean isNotice) {
		this.isNotice = isNotice;
	}
}