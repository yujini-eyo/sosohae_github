package com.myspring.eum.admin.vo;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

/**
 * 관리자 전용 게시글 VO
 * - ArticleVO를 확장해서 관리자 화면에서만 필요한 메타정보를 추가.
 * - 이미지 파일명 인/디코딩 로직은 ArticleVO의 구현을 그대로 상속.
 */
@Component("adminArticleVO")
//com.myspring.eum.admin.vo.AdminArticleVO
public class AdminArticleVO {
 private Integer articleNO;
 private Integer parentNO;
 private String title;
 private String content;
 private String id;
 private String imageFileName;

 // 이름을 그대로 둡니다 (Boolean 또는 boolean 가능)
 private Boolean isNotice;

 private java.sql.Timestamp writeDate;

 // ... (다른 getter/setter)

 // ★ MyBatis가 찾는 정확한 시그니처
 public Boolean getIsNotice() {       // 또는 primitive면: public boolean getIsNotice()
     return isNotice;
 }
 public void setIsNotice(Boolean isNotice) { // 또는 primitive면: public void setIsNotice(boolean isNotice)
     this.isNotice = isNotice;
 }

 public Integer getArticleNO() {
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
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
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
// (선택) 사용성 좋은 보조 메서드도 함께 두면 좋아요
 public boolean isNotice() { // 템플릿/뷰에서 쓰기 편함
     return Boolean.TRUE.equals(isNotice);
 }
 public void setNotice(boolean notice) {
     this.isNotice = notice;
 }
}
