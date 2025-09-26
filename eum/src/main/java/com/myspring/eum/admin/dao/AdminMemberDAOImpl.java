package com.myspring.eum.admin.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myspring.eum.common.paging.PageRequest;
import com.myspring.eum.member.vo.MemberVO;

@Repository("adminMemberDAO")
public class AdminMemberDAOImpl implements AdminMemberDAO {

    private static final String NS = "mapper.adminMember.";

    @Autowired
    private SqlSessionTemplate sqlSession;

    public List<MemberVO> findMembers(PageRequest req) throws Exception {
        return sqlSession.selectList(NS + "findMembers", req);
    }

    public long countMembers(PageRequest req) throws Exception {
        Long cnt = (Long) sqlSession.selectOne(NS + "countMembers", req);
        return cnt == null ? 0L : cnt.longValue();
    }
}
