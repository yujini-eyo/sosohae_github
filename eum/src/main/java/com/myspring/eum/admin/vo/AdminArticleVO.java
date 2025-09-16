package com.myspring.eum.admin.vo;

import java.util.Date;

public class AdminArticleVO {
    private int articleNO;
    private Integer parentNO;          // null 허용 → INSERT 시 COALESCE
    private String title;
    private String content;
    private String id;                 // 작성자 ID
    private String imageFileName;      // 저장 파일명
    private Boolean isNotice;          // 공지 여부
    private Date writeDate;
    private Integer viewCnt;          // 또는 int
    
    public Integer getViewCnt() { return viewCnt; }
    public void setViewCnt(Integer viewCnt) { this.viewCnt = viewCnt; }

    public int getArticleNO() { return articleNO; }
    public void setArticleNO(int articleNO) { this.articleNO = articleNO; }

    public Integer getParentNO() { return parentNO; }
    public void setParentNO(Integer parentNO) { this.parentNO = parentNO; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getImageFileName() { return imageFileName; }
    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }

    public Boolean getIsNotice() { return isNotice; }
    public void setIsNotice(Boolean isNotice) { this.isNotice = isNotice; }

    public Date getWriteDate() { return writeDate; }
    public void setWriteDate(Date writeDate) { this.writeDate = writeDate; }
}
