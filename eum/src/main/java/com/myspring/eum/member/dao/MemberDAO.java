package com.myspring.eum.member.dao;

import org.springframework.dao.DataAccessException;
import com.myspring.eum.member.vo.MemberVO;

public interface MemberDAO {
    int insertMember(MemberVO vo) throws DataAccessException;       // 가입
    MemberVO login(MemberVO memberVO) throws DataAccessException;   // 로그인
    MemberVO findById(String id) throws DataAccessException;        // 단건 조회
}
