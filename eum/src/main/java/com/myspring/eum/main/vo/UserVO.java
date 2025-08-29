package com.myspring.eum.main.vo;

import java.io.Serializable;

public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userName;
    private int    userPoints;

    public UserVO() {}

    public UserVO(String userId, String userName, int userPoints) {
        this.userId = userId;
        this.userName = userName;
        this.userPoints = userPoints;
    }

    public String getUserId()   { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getUserPoints()  { return userPoints; }
    public void setUserPoints(int userPoints) { this.userPoints = userPoints; }

    @Override
    public String toString() {
        return "UserVO{userId='" + userId + "', userName='" + userName + "', userPoints=" + userPoints + "}";
    }
}
