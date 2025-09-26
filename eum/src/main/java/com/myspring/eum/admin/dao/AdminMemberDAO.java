package com.myspring.eum.admin.dao;


import java.util.List;
import java.util.Map;
import com.myspring.eum.common.paging.PageRequest;
import com.myspring.eum.member.vo.MemberVO;


public interface AdminMemberDAO {
List<MemberVO> findMembers(PageRequest req) throws Exception;
long countMembers(PageRequest req) throws Exception;
}