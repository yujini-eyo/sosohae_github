package com.myspring.eum.member.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.eum.member.vo.MemberVO;

@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO {
	private static final String NS = "mapper.member.";
	@Autowired
	private SqlSession sqlSession;

	public int insertMember(MemberVO vo) {
		return sqlSession.insert(NS + "insertMember", vo);
	}

	public MemberVO login(MemberVO vo) {
		return (MemberVO) sqlSession.selectOne(NS + "loginById", vo);
	}

	public MemberVO findById(String id) {
		// (옵션 A) 매퍼가 parameterType="com....MemberVO" 인 경우 → VO로 감싸서 전달
		MemberVO key = new MemberVO();
		key.setId(id);
		return (MemberVO) sqlSession.selectOne(NS + "findById", key);
		
		// (옵션 B) 매퍼가 parameterType="string" 이고 WHERE id = #{_parameter} 라면:
		// return (MemberVO) sqlSession.selectOne(NS+"findById", id);
	}

	public MemberVO loginById(String id){
		// TODO Auto-generated method stub
		return sqlSession.selectOne(NS + "loginById", id);
	}
	
	@Override
	  public int updateProfile(MemberVO vo) throws DataAccessException {
	    return sqlSession.update(NS + "updateProfile", vo);
	  }

	  @Override
	  public int verifyPassword(String id, String password) throws DataAccessException {
	    Map<String,Object> p = new HashMap<String,Object>();
	    p.put("id", id);
	    p.put("password", password); // 운영에선 해시 비교 권장
	    Integer cnt = sqlSession.selectOne(NS + "verifyPassword", p);
	    return cnt == null ? 0 : cnt;
	  }

	  @Override
	  public int changePassword(String id, String newPassword) throws DataAccessException {
	    Map<String,Object> p = new HashMap<String,Object>();
	    p.put("id", id);
	    p.put("password", newPassword); // 운영에선 해시 후 저장
	    return sqlSession.update(NS + "updatePassword", p);
	  }
	  
	  public List<MemberVO> selectAllMemberList() {
		  return sqlSession.selectList(NS + "selectAllMemberList");
	  }
}
