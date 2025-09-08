package com.myspring.eum.auth.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    // ★ 실제 XML 네임스페이스와 일치시켜야 합니다.
    private static final String NS_AUTH   = "mapper.auth.";   // 관리자 관련 쿼리 (adminMapper.xml)
    private static final String NS_MEMBER = "mapper.member."; // 회원 관련 쿼리 (memberMapper.xml)

    @Autowired
    private SqlSession sqlSession;

    @Override
    public AdminVO authenticate(String id, String rawPassword) throws Exception {
        if (id == null || rawPassword == null) return null;

        // adminMapper.xml 내 select id="findAdminById"
        AdminVO admin = (AdminVO) sqlSession.selectOne(NS_AUTH + "findAdminById", id);
        if (admin == null) return null;

        // 운영에서는 BCrypt 등 해시 비교 권장. 현재는 평문 비교.
        String saved = admin.getPassword();
        if (saved == null) return null;
        return saved.equals(rawPassword) ? admin : null;
    }

    @Override
    public MemberVO loadMember(String id) throws Exception {
        if (id == null) return null;
        // memberMapper.xml 의 select id="findById" (VO 파라미터 기준이라 VO로 감싸 전달)
        MemberVO key = new MemberVO();
        key.setId(id);
        return (MemberVO) sqlSession.selectOne(NS_MEMBER + "findById", key);
    }

    // 관리자 페이지 회원 목록 (컨트롤러에서 사용할 경우)
    public List<MemberVO> listAllMembers() throws Exception {
        // memberMapper.xml 의 select id="selectAllMemberList"
        return sqlSession.selectList(NS_MEMBER + "selectAllMemberList");
    }
}
