package com.myspring.eum.admin.inquiry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.eum.admin.inquiry.dao.AdminInquiryDAO;
import com.myspring.eum.admin.inquiry.vo.InquiryVO;
import com.myspring.eum.common.paging.Page;
import com.myspring.eum.common.paging.PageRequest;

@Service
public class AdminInquiryServiceImpl implements AdminInquiryService {

    @Autowired
    private AdminInquiryDAO adminInquiryDAO;

    @Transactional(readOnly = true)
    public Page<InquiryVO> list(PageRequest req) throws Exception {
        if (req == null) req = new PageRequest();

        int page = req.getPage();
        int size = req.getSize();
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        long total = adminInquiryDAO.count(req);
        int lastPage = size > 0 ? (int)((total + size - 1) / size) : 1;
        if (lastPage > 0 && page > lastPage) page = lastPage;

        // setter 없는 경우도 고려
        try {
            req.setPage(page);
            req.setSize(size);
        } catch (Throwable ignore) {
            req = new PageRequest(page, size);
        }

        List<InquiryVO> rows = adminInquiryDAO.list(req);
        return new Page<InquiryVO>(rows, page, size, total);
    }

    @Transactional(readOnly = true)
    public InquiryVO view(Long id) throws Exception {
        return adminInquiryDAO.findById(id);
    }

    @Transactional
    public void reply(Long id, String reply) throws Exception {
        adminInquiryDAO.reply(id, reply);
    }
}
