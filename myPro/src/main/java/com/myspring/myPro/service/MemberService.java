package com.myspring.myPro.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.myPro.vo.MemberVO;

public interface MemberService {
	public List searchMemberList(String name) throws DataAccessException;
	public List listMembers() throws DataAccessException;
	
	public MemberVO login(MemberVO memberVO) throws DataAccessException;
	public int addMember(MemberVO memberVO) throws DataAccessException;
	public void delMember(String id) throws DataAccessException;
	public void updateMember(MemberVO memberVO) throws DataAccessException;
}
