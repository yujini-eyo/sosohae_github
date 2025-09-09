package com.myspring.eum.auth.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

@Repository("authDAO")
public class AuthDAOImpl implements AuthDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NS_ADMIN  = "mapper.adminAuth.";
    private static final String NS_MEMBER = "mapper.member.";

    public AdminVO findById(String id) throws Exception {
        return (AdminVO) sqlSession.selectOne(NS_ADMIN + "findById", id);
    }

    public AdminVO findAdminById(String id) throws Exception {
        return (AdminVO) sqlSession.selectOne(NS_ADMIN + "findAdminById", id);
    }

    public MemberVO findMemberById(String id) throws Exception {
        return (MemberVO) sqlSession.selectOne(NS_MEMBER + "findById", id);
    }

    public List<MemberVO> selectAllMemberList() throws Exception {
        return sqlSession.selectList(NS_MEMBER + "selectAllMemberList");
    }
}
