package com.myspring.eum.auth.service;

import com.myspring.eum.auth.dao.AuthDAO;
import com.myspring.eum.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    /** 개발용: 평문 비교 허용. 운영 전환 시 false + 해시 도입 */
    private boolean usePlainText = true;

    @Override
    public MemberVO authenticate(String id, String rawPassword) throws Exception {
        if (id == null || rawPassword == null) return null;

        final MemberVO user = authDAO.findById(id);
        if (user == null) return null;

        final String stored = user.getPassword();
        boolean ok = false;

        if (usePlainText) {
            ok = (stored != null && stored.equals(rawPassword));
        } else {
            // TODO: 운영 전환 시 BCrypt 등으로 구현
            ok = false;
        }

        if (!ok) return null;

        // 상태/권한 널 안전
        final String status = user.getStatus();
        if (status == null || !"ACTIVE".equalsIgnoreCase(status)) return null;

        return user;
    }

    // XML에서 <property name="usePlainText" value="true"/>로 조정할 수 있게 setter 제공
    public void setUsePlainText(boolean usePlainText) { this.usePlainText = usePlainText; }
}
