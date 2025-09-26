package com.myspring.eum.admin.inquiry.service;

import com.myspring.eum.admin.inquiry.vo.InquiryVO;
import com.myspring.eum.common.paging.Page;
import com.myspring.eum.common.paging.PageRequest;

public interface AdminInquiryService {
    Page<InquiryVO> list(PageRequest req) throws Exception;
    InquiryVO view(Long id) throws Exception;
    void reply(Long id, String reply) throws Exception;
}
