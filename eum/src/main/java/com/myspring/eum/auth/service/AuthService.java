package com.myspring.eum.auth.service;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

public interface AuthService {

    /** 관리자 인증 */
    AdminVO authenticate(String id, String rawPassword) throws Exception;

    /** (관리자 전용) 회원 정보 단건 조회 */
    MemberVO loadMember(String id) throws Exception;
}
