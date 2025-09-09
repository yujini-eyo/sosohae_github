package com.myspring.eum.auth.service;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.auth.dao.AuthDAO;
import com.myspring.eum.member.vo.MemberVO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    /** 개발용: 평문 비교 (운영 전환 전까지 OK) */
    private boolean usePlainText = true;

    @Override
    public AdminVO authenticate(String id, String rawPassword) throws Exception {
        if (id == null || rawPassword == null) return null;
        id = id.trim();
        rawPassword = rawPassword.trim();
        if (id.isEmpty() || rawPassword.isEmpty()) return null;

        final AdminVO user = authDAO.findById(id);
        if (user == null) return null;

        final String stored = user.getPassword(); // VO의 getter가 정확해야 함
        if (stored == null) return null;

        boolean ok = usePlainText && stored.equals(rawPassword);
        if (!ok) return null;

        // 상태/권한 체크 (관리자만)
        if (user.getStatus() == null || !"ACTIVE".equalsIgnoreCase(user.getStatus())) return null;
        if (user.getRole() == null   || !"ADMIN".equalsIgnoreCase(user.getRole()))   return null;

        return user;
    }

    public void setUsePlainText(boolean usePlainText) { this.usePlainText = usePlainText; }

    /* ===== 여기부터 TODO 구현 ===== */

    @Override
    public MemberVO loadMember(String id) throws Exception {
        if (id == null) return null;
        id = id.trim();
        if (id.length() == 0) return null;
        // 관리자 페이지에서 회원 단건 조회
        return authDAO.findMemberById(id);
    }

    @Override
    public List<MemberVO> listAllMembers() throws Exception {
        // 관리자 회원 목록 (mapper.member.selectAllMemberList 매핑)
        return authDAO.selectAllMemberList();
    }
}
