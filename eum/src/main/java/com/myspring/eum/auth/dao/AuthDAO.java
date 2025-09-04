package com.myspring.eum.auth.dao;

import com.myspring.eum.member.vo.MemberVO;

public interface AuthDAO {
    MemberVO findById(String id) throws Exception;
}
