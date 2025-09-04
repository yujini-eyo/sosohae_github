package com.myspring.eum.auth.service;

import com.myspring.eum.member.vo.MemberVO;

public interface AuthService {
    MemberVO authenticate(String id, String rawPassword) throws Exception;
}
