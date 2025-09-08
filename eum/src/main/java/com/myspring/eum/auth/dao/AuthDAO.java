package com.myspring.eum.auth.dao;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

public interface AuthDAO {

    /** 관리자 전용 조회 */
    AdminVO findAdminById(String id) throws Exception;

    /** 관리자 페이지에서 회원 정보 조회 */
    MemberVO findMemberById(String id) throws Exception;

    /**
     * @deprecated 대신 {@link #findAdminById(String)} 사용
     */
    @Deprecated
    AdminVO findById(String id) throws Exception;
}
