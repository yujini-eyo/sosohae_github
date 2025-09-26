package com.myspring.eum.admin.service;

import com.myspring.eum.common.paging.Page;
import com.myspring.eum.common.paging.PageRequest;
import com.myspring.eum.member.vo.MemberVO;

public interface AdminMemberService {
    /** 기존 시그니처 유지 */
    Page<MemberVO> list(PageRequest req) throws Exception;

    /** 편의 오버로드: page/size만으로 호출 */
    Page<MemberVO> list(int page, int size) throws Exception;

    /** 선택: 키워드 검색 + 페이징 (필요 없으면 제거해도 됨) */
    Page<MemberVO> search(String keyword, int page, int size) throws Exception;
}
