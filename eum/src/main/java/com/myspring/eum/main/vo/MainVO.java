package com.myspring.eum.main.vo;

import java.io.Serializable;
import org.apache.ibatis.type.Alias;

@Alias("MainVO") // optional: mybatis type alias
public class MainVO implements Serializable {
    private static final long serialVersionUID = 1L;

    // Primary Key (matches DAO: deleteMain(String id))
    private String id;

    // Page meta (used for view rendering)
    private String pageTitle;    // HTML <title> text
    private String currentPath;  // current request path
    private String flashMessage; // transient flash message

    public MainVO() {}

    public MainVO(String id, String pageTitle, String currentPath, String flashMessage) {
        this.id = id;
        this.pageTitle = pageTitle;
        this.currentPath = currentPath;
        this.flashMessage = flashMessage;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPageTitle() { return pageTitle; }
    public void setPageTitle(String pageTitle) { this.pageTitle = pageTitle; }

    public String getCurrentPath() { return currentPath; }
    public void setCurrentPath(String currentPath) { this.currentPath = currentPath; }

    public String getFlashMessage() { return flashMessage; }
    public void setFlashMessage(String flashMessage) { this.flashMessage = flashMessage; }

    @Override
    public String toString() {
        return "MainVO{id='" + id + "', pageTitle='" + pageTitle + "', currentPath='" + currentPath
                + "', flashMessage='" + flashMessage + "'}";
    }
}
