package com.myspring.eum.main.vo;

import java.io.Serializable;

public class MainVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pageTitle;     // JSP <title>�뿉 諛붿씤�뵫
    private String currentPath;   // �쁽�옱 �슂泥� 寃쎈줈(�븘�슂 �떆)
    private String flashMessage;  // �븣由� 硫붿떆吏�(�꽑�깮)

    public MainVO() {}

    public MainVO(String pageTitle, String currentPath, String flashMessage) {
        this.pageTitle = pageTitle;
        this.currentPath = currentPath;
        this.flashMessage = flashMessage;
    }

    public String getPageTitle()     { return pageTitle; }
    public void setPageTitle(String pageTitle) { this.pageTitle = pageTitle; }

    public String getCurrentPath()   { return currentPath; }
    public void setCurrentPath(String currentPath) { this.currentPath = currentPath; }

    public String getFlashMessage()  { return flashMessage; }
    public void setFlashMessage(String flashMessage) { this.flashMessage = flashMessage; }

    @Override
    public String toString() {
        return "MainVO{pageTitle='" + pageTitle + "', currentPath='" + currentPath + "', flashMessage='" + flashMessage + "'}";
    }
}
