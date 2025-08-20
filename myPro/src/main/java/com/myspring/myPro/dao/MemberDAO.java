package com.myspring.myPro.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.myPro.vo.MemberVO;

public interface MemberDAO {
	public List searchMemberList(String name) throws DataAccessException;
	public List selectAllMembers() throws DataAccessException;
	
	public MemberVO selectLoginMember(MemberVO memberVO) throws DataAccessException ;
	public int insertMember(MemberVO memberVO) throws DataAccessException ;
	public void deleteMember(String id) throws DataAccessException ;
	public void updateMember(MemberVO memberVO) throws DataAccessException ;
}
