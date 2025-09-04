package com.myspring.eum.auth.dao;

import com.myspring.eum.member.vo.MemberVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("authDAO")
public class AuthDAOImpl implements AuthDAO {
    private static final String NS = "mapper.auth.";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public MemberVO findById(String id) throws Exception {
        return sqlSession.selectOne(NS + "findById", id);
    }
}
