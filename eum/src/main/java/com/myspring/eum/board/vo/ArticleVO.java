// com.myspring.eum.board.vo.ArticleVO
package com.myspring.eum.board.vo;

import java.util.Date;

public class ArticleVO {
	private Long articleNO;
	private Long parentNO;
	private String title;
	private String content;
	private Date writeDate;
	private String imageFileName;

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

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
}
