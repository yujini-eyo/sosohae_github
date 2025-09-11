package com.myspring.eum.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.eum.member.vo.MemberVO;

public interface MemberDAO {
    int insertMember(MemberVO vo) throws DataAccessException;       // 가입
    MemberVO login(MemberVO memberVO) throws DataAccessException;   // 로그인
    MemberVO findById(String id) throws DataAccessException;        // 단건 조회
    MemberVO loginById(String id) throws DataAccessException;
    
    int updateProfile(MemberVO vo) throws DataAccessException;    // 프로필 수정
    int verifyPassword(String id, String password) throws DataAccessException; // 현재 비번 확인 (0|1)
    int changePassword(String id, String newPassword) throws DataAccessException; // 비번 변경
	List<MemberVO> selectAllMemberList() throws DataAccessException;
}
