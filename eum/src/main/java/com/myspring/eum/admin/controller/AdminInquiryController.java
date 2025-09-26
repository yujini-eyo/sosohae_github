package com.myspring.eum.admin.controller;

import javax.servlet.http.HttpServletRequest;

import com.myspring.eum.admin.inquiry.service.AdminInquiryService;
import com.myspring.eum.common.paging.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/inquiry")
public class AdminInquiryController {

    @Autowired
    private AdminInquiryService adminInquiryService;

    // 목록
    @RequestMapping(value="/list.do", method=RequestMethod.GET)
    public String list(Integer page, Integer size, String field, String keyword,
                       String status, String sort, String order, Model model) throws Exception {
        PageRequest req = new PageRequest();
        if (page != null) req.setPage(page.intValue());
        if (size != null) req.setSize(size.intValue());
        req.setField(field);
        req.setKeyword(keyword);
        req.setStatus(status);
        req.setSort(sort);
        req.setOrder(order);

        model.addAttribute("page", adminInquiryService.list(req));
        model.addAttribute("q", req);
        return "admin/inquiry/list";
    }

    // 상세
    @RequestMapping(value="/view.do", method=RequestMethod.GET)
    public String view(Long id, Model model) throws Exception {
        model.addAttribute("row", adminInquiryService.view(id));
        return "admin/inquiry/view";
    }

    // 답변
    @RequestMapping(value="/reply.do", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
    public String reply(Long id, String reply, HttpServletRequest request) throws Exception {
        adminInquiryService.reply(id, reply);
        return "redirect:/admin/inquiry/view.do?id=" + id;
    }
}
