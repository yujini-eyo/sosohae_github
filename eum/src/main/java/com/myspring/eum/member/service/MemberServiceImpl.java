package com.myspring.eum.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.eum.member.dao.MemberDAO;
import com.myspring.eum.member.vo.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
  @Autowired private MemberDAO memberDAO;

  @Override
  public int signup(MemberVO vo) throws Exception {
    return memberDAO.insertMember(vo);
  }

  @Override
  public MemberVO login(MemberVO vo) throws Exception {
    return memberDAO.login(vo); // mapper.member.loginById 필요
  }

  @Override
  public MemberVO findById(String id) throws Exception {
    return memberDAO.findById(id);
  }

  @Override
  public List<MemberVO> listAllMembers() throws Exception {
    return memberDAO.selectAllMemberList(); // DAO/매퍼에 존재해야 함
  }

  // ✅ 마이페이지: 프로필 수정
  @Override
  public int updateProfile(MemberVO vo) throws Exception {
    return memberDAO.updateProfile(vo); // mapper.member.updateProfile
  }

  // ✅ 현재 비번 검증 (login 재사용)
  @Override
  public boolean verifyPassword(String id, String curPw) throws Exception {
    MemberVO probe = new MemberVO();
    probe.setId(id);
    probe.setPassword(curPw);
    return memberDAO.login(probe) != null;
    // 또는: return memberDAO.verifyPassword(id, curPw) == 1;  (해당 쿼리 추가 시)
  }

  // ✅ 비밀번호 변경
  @Override
  public int changePassword(String id, String newPw) {
    return memberDAO.changePassword(id, newPw); // mapper.member.changePassword
  }
}
