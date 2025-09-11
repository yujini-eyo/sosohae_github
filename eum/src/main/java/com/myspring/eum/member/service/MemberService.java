package com.myspring.eum.member.service;

import java.util.List;

import com.myspring.eum.member.vo.MemberVO;

public interface MemberService {
    /** 회원가입(삽입) */
    int signup(MemberVO vo) throws Exception;

    /** 로그인(id+password로 조회) */
    MemberVO login(MemberVO memberVO) throws Exception;

    /** 단건 조회(id로 조회) */
    MemberVO findById(String id) throws Exception;
    
    List<MemberVO> listAllMembers() throws Exception;

	int updateProfile(MemberVO vo) throws Exception;
	boolean verifyPassword(String id, String curPw) throws Exception;
	int changePassword(String id, String newPw);
	
	
    
    

}
