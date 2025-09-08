package com.myspring.eum.member.dao;

import java.util.List;
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

    public List<MemberVO> selectAllMemberList() throws DataAccessException {
        return sqlSession.selectList(NS + "selectAllMemberList");
    }

    public int insertMember(MemberVO vo) throws DataAccessException {
        return sqlSession.insert(NS + "insertMember", vo);
    }

    public int removeMember(String id) throws DataAccessException {
        // XML: <delete id="deleteMember" ...> 를 호출
        return sqlSession.delete(NS + "deleteMember", id);
    }

    public MemberVO login(MemberVO memberVO) throws DataAccessException {
        // XML: <select id="loginById" parameterType="com.myspring.eum.member.vo.MemberVO" ...>
        return (MemberVO) sqlSession.selectOne(NS + "loginById", memberVO);
    }

    public MemberVO findById(String id) throws DataAccessException {
        // XML: <select id="findById" parameterType="string"> ... WHERE id = #{id}
        // ※ Java 6에서는 단일 String 파라미터에 #{id} 바인딩이 깨질 수 있으니 VO로 감싸 안전하게 전달
        MemberVO key = new MemberVO();
        key.setId(id);
        return (MemberVO) sqlSession.selectOne(NS + "findById", key);
        // 만약 XML에서 WHERE id = #{_parameter} 로 되어 있다면 위 2줄 대신:
        // return (MemberVO) sqlSession.selectOne(NS + "findById", id);
    }
}
