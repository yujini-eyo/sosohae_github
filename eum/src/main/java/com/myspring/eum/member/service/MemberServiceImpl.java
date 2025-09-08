package com.myspring.eum.member.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myspring.eum.member.dao.MemberDAO;
import com.myspring.eum.member.vo.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;

    public List<MemberVO> listMembers() throws Exception {
        return memberDAO.selectAllMemberList();
    }

    public int addMember(MemberVO vo) throws Exception {
        return memberDAO.insertMember(vo);
    }

    public int removeMember(String id) throws Exception {
        return memberDAO.removeMember(id);
    }

    public MemberVO login(MemberVO memberVO) throws Exception {
        return memberDAO.login(memberVO); // mapper: loginById
    }

    public MemberVO findById(String id) throws Exception {
        return memberDAO.findById(id);
    }
}
