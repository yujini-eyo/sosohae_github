package com.myspring.eum.auth.service;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.auth.dao.AuthDAO;
import com.myspring.eum.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    private boolean usePlainText = true; // 운영 시 PasswordEncoder로 교체

    public AdminVO authenticate(String id, String rawPassword) throws Exception {
        AdminVO user = authDAO.findAdminById(id);
        if (user == null) return null;

        String stored = user.getPassword();
        boolean ok;
        if (usePlainText) {
            ok = stored != null && stored.equals(rawPassword);
        } else {
            ok = false; // passwordEncoder.matches(rawPassword, stored);
        }
        if (!ok) return null;

        // 관리자/상태 체크
        if (user.getStatus() != null && !"ACTIVE".equalsIgnoreCase(String.valueOf(user.getStatus()))) return null;
        if (user.getRole() != null   && !"ADMIN".equalsIgnoreCase(String.valueOf(user.getRole())))     return null;

        return user;
    }

    public MemberVO loadMember(String id) throws Exception {
        return authDAO.findMemberById(id);
    }

    public void setUsePlainText(boolean usePlainText) { this.usePlainText = usePlainText; }
}
