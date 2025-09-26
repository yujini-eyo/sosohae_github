package com.myspring.eum.admin.inquiry.dao;

import java.util.List;

import com.myspring.eum.admin.inquiry.vo.InquiryVO;
import com.myspring.eum.common.paging.PageRequest;

public interface AdminInquiryDAO {
    long count(PageRequest req) throws Exception;
    List<InquiryVO> list(PageRequest req) throws Exception;
    InquiryVO findById(Long id) throws Exception;
    int reply(Long id, String reply) throws Exception; // 상태 ANSWERED, 답변/시간 업데이트
}
