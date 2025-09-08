package com.myspring.eum.member.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.myspring.eum.member.vo.MemberVO;

public interface MemberDAO {
    List<MemberVO> selectAllMemberList() throws DataAccessException;
    int insertMember(MemberVO vo) throws DataAccessException;
    int removeMember(String id) throws DataAccessException;        // delete
    MemberVO login(MemberVO memberVO) throws DataAccessException;  // SELECT * WHERE id & password
    MemberVO findById(String id) throws DataAccessException;       // 단건 조회
}
