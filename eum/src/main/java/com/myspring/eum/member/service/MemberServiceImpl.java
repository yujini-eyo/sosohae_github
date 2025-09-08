package com.myspring.eum.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.eum.member.dao.MemberDAO;
import com.myspring.eum.member.vo.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
  @Autowired private MemberDAO memberDAO;
  public int signup(MemberVO vo) throws Exception { return memberDAO.insertMember(vo); }
  public MemberVO login(MemberVO vo) throws Exception { return memberDAO.login(vo); }
  public MemberVO findById(String id) throws Exception { return memberDAO.findById(id); }
@Override
public List<MemberVO> listAllMembers() throws Exception {
	// TODO Auto-generated method stub
	return null;
}
}
