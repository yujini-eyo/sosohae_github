package com.myspring.eum.member.service;

import java.util.List;
import com.myspring.eum.member.vo.MemberVO;

public interface MemberService {
    List<MemberVO> listMembers() throws Exception;
    int addMember(MemberVO vo) throws Exception;
    int removeMember(String id) throws Exception;
    MemberVO login(MemberVO memberVO) throws Exception;
    MemberVO findById(String id) throws Exception;
}
