package com.myspring.eum.admin.inquiry.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myspring.eum.admin.inquiry.vo.InquiryVO;
import com.myspring.eum.common.paging.PageRequest;

@Repository
public class AdminInquiryDAOImpl implements AdminInquiryDAO {

    private static final String NS = "mapper.adminInquiry.";

    @Autowired
    private SqlSessionTemplate sqlSession;

    public long count(PageRequest req) throws Exception {
        Long cnt = (Long) sqlSession.selectOne(NS + "count", req);
        return cnt == null ? 0L : cnt.longValue();
    }

    public List<InquiryVO> list(PageRequest req) throws Exception {
        return sqlSession.selectList(NS + "list", req);
    }

    public InquiryVO findById(Long id) throws Exception {
        return (InquiryVO) sqlSession.selectOne(NS + "findById", id);
    }

    public int reply(Long id, String reply) throws Exception {
        Map params = new HashMap(); // Java 7: 다이아몬드 금지
        params.put("id", id);
        params.put("reply", reply);
        return sqlSession.update(NS + "reply", params);
    }
}
