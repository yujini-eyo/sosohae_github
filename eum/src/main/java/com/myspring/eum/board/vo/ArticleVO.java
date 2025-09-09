package com.myspring.eum.board.vo;

import java.util.Date;

public class ArticleVO {
	private Long articleNO;
	private Long parentNO;
	private String id;
	private String title;
	private String content;
	private String imageFileName;
	private Date writeDate;
	
	public Long getArticleNO() {
		return articleNO;
	}
	public void setArticleNO(Long articleNO) {
		this.articleNO = articleNO;
	}
	public Long getParentNO() {
		return parentNO;
	}
	public void setParentNO(Long parentNO) {
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
}