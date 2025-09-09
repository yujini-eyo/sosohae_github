package com.myspring.eum.auth.dao;

import java.util.List;

import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

public interface AuthDAO {

    /** 관리자 단건 조회 (mapper.adminAuth.findById) */
    AdminVO findById(String id) throws Exception;

    /** (선택) 별칭/호환용: mapper.adminAuth.findAdminById */
    AdminVO findAdminById(String id) throws Exception;

    /** 회원 단건 조회 (mapper.member.findById) */
    MemberVO findMemberById(String id) throws Exception;

    /** 전체 회원 목록 (mapper.member.selectAllMemberList) */
    List<MemberVO> selectAllMemberList() throws Exception;
}
