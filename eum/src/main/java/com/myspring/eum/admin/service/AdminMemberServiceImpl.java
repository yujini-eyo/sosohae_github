package com.myspring.eum.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.eum.admin.dao.AdminMemberDAO;
import com.myspring.eum.common.paging.Page;
import com.myspring.eum.common.paging.PageRequest;
import com.myspring.eum.member.vo.MemberVO;

@Service("adminMemberService")
public class AdminMemberServiceImpl implements AdminMemberService {

    @Autowired
    private AdminMemberDAO adminMemberDAO;

    @Override
    @Transactional(readOnly = true)
    public Page<MemberVO> list(PageRequest req) throws Exception {
        if (req == null) req = new PageRequest();

        int page = req.getPage();
        int size = req.getSize();
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        long total = adminMemberDAO.countMembers(req);
        int lastPage = size > 0 ? (int) ((total + size - 1) / size) : 1;
        if (lastPage > 0 && page > lastPage) page = lastPage;

        // DAO가 offset/limit를 쓰므로 보정값을 반영
        try {
            req.setPage(page);
            req.setSize(size);
        } catch (Throwable ignore) {
            req = new PageRequest(page, size);
        }

        List<MemberVO> rows = adminMemberDAO.findMembers(req);
        return new Page<MemberVO>(rows, page, size, total);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberVO> list(int page, int size) throws Exception {
        PageRequest req = new PageRequest();
        try {
            req.setPage(page);
            req.setSize(size);
        } catch (Throwable ignore) {
            // 무시
        }
        return list(req);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberVO> search(String keyword, int page, int size) throws Exception {
        PageRequest req = new PageRequest();
        try {
            req.setPage(page);
            req.setSize(size);
            req.setKeyword(keyword); // mapper에서 keyword 사용
            // 특정 필드로 제한하려면: req.setField("name"); 등
        } catch (Throwable ignore) {
            // 무시
        }
        return list(req);
    }
}
