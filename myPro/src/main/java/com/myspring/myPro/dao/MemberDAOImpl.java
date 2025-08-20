package com.myspring.myPro.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.myPro.vo.MemberVO;

@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
//	public void setSqlSession(SqlSession sqlSession) {
//		this.sqlSession = sqlSession;
//	}
	
	@Override
	public List searchMemberList(String name) throws DataAccessException{
	
		List<MemberVO> searchMemberList = null;
		System.out.println("name :: "+ name);
		searchMemberList = sqlSession.selectList("mapper.member.selectSearchMemberList", name);
		return searchMemberList;
	}
	
	@Override
	public List selectAllMembers() throws DataAccessException{
	
		List<MemberVO> membersList = null;
		membersList = sqlSession.selectList("mapper.member.selectAllMemberList");
		return membersList;
	}
	
	
	@Override
	public MemberVO selectLoginMember(MemberVO memberVO) throws DataAccessException {
		MemberVO result = sqlSession.selectOne("mapper.member.selectLoginMember", memberVO);
		return result;
	}
	
	@Override
	public int insertMember(MemberVO memberVO) throws DataAccessException {
		int result = sqlSession.insert("mapper.member.insertMember", memberVO);
		return result;
	}
	
	@Override
	public void updateMember(MemberVO memberVO) throws DataAccessException {
		sqlSession.insert("mapper.member.updateMember", memberVO);
	}
	
	@Override
	public void deleteMember(String id) throws DataAccessException {
		sqlSession.insert("mapper.member.deleteMember", id);
	}

}
