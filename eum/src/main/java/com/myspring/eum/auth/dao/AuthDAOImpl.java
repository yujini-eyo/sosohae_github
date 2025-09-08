package com.myspring.eum.auth.dao;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("authDAO")
public class AuthDAOImpl implements AuthDAO {

    private static final String ADMIN_NS  = "mapper.adminAuth.";
    private static final String MEMBER_NS = "mapper.member.";

    @Autowired
    private SqlSession sqlSession;

    public AdminVO findAdminById(String id) throws Exception {
        return (AdminVO) sqlSession.selectOne(ADMIN_NS + "findById", id);
    }

    public MemberVO findMemberById(String id) throws Exception {
        return (MemberVO) sqlSession.selectOne(MEMBER_NS + "findById", id);
    }

    /** @deprecated */
    public AdminVO findById(String id) throws Exception {
        return findAdminById(id);
    }
}
