package com.myspring.eum.member.vo;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

public class MemberVO {
    private String id;           // 아이디
    private String password;     // 비밀번호
    private String name;         // 이름
    private String email;        // 이메일
    private String address;      // 주소
    private Integer age;         // 나이 (TINYINT UNSIGNED → Integer)
    private Date birth;          // 생년월일
    private String phone;        // 전화번호
    private String code;         // 코드
    private String notes;        // 메모
    private Timestamp createdAt; // 생성일 (CURRENT_TIMESTAMP)
    private String role;     // ADMIN / USER ...
    private String status;   // ACTIVE / INACTIVE ...
    private Timestamp updatedAt;
    
    
    // ===== Getter / Setter =====
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Date getBirth() {
        return birth;
    }
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
