package com.myspring.eum.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.member.service.MemberService;
import com.myspring.eum.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping("/member")
public class MemberControllerImpl implements MemberController {

    @Autowired
    private MemberService memberService;

    /** 회원가입 폼 (GET) */
    @RequestMapping(value="/signupForm.do", method=RequestMethod.GET)
    public ModelAndView signupForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("member/signupForm");
    }

    /** 로그인 폼 (GET) */
    @RequestMapping(value="/loginForm.do", method=RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("member/loginForm");
    }

    /** 회원가입 처리 (POST /member/signup.do) */
    @RequestMapping(value="/signup.do", method=RequestMethod.POST)
    public ModelAndView signup(@ModelAttribute("info") MemberVO memberVO,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (memberVO == null || isEmpty(memberVO.getId()) || isEmpty(memberVO.getPassword())) {
            return new ModelAndView("redirect:/member/signupForm.do");
        }

        // 중복 체크
        MemberVO exists = memberService.findById(memberVO.getId().trim());
        if (exists != null) {
            return new ModelAndView("redirect:/member/signupForm.do");
        }

        try {
            // 저장 (운영 시 비밀번호 해시 권장)
            memberVO.setId(memberVO.getId().trim());
            memberService.signup(memberVO);
        } catch (DuplicateKeyException dke) {
            return new ModelAndView("redirect:/member/signupForm.do");
        }

        // 가입 완료 → 로그인 폼으로
        return new ModelAndView("redirect:/member/loginForm.do");
    }

    /** 로그인 처리 (POST /member/login.do) */
    @RequestMapping(value="/login.do", method=RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("member") MemberVO member,
                              RedirectAttributes rAttr,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (member == null || isEmpty(member.getId()) || isEmpty(member.getPassword())) {
            rAttr.addFlashAttribute("loginError", "아이디/비밀번호를 입력하세요.");
            return new ModelAndView("redirect:/member/loginForm.do");
        }

        MemberVO db = memberService.login(member); // mapper: loginById
        if (db == null) {
            rAttr.addFlashAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return new ModelAndView("redirect:/member/loginForm.do");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("member", db);
        session.setMaxInactiveInterval(60 * 60);
        return new ModelAndView("redirect:/main.do");
    }

    /** 로그아웃 (GET) */
    @RequestMapping(value="/logout.do", method=RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return new ModelAndView("redirect:/main.do");
    }
    
 // 마이페이지
    @RequestMapping(value="/mypage.do", method=RequestMethod.GET)
    public String mypage(HttpSession session, Model model) {
        MemberVO login = (MemberVO) session.getAttribute("member");
        if (login == null) {
            session.setAttribute("action", "/member/mypage.do");
            return "redirect:/member/loginForm.do";
        }
        model.addAttribute("member", login);

        Map<String, Object> ps = new HashMap<>();
        ps.put("current", 0); ps.put("monthGain", 0); ps.put("monthSpend", 0);
        ps.put("totalGain", 0); ps.put("totalSpend", 0); ps.put("totalHours", 0);
        ps.put("tier","나무"); ps.put("nextNeeded", 0); ps.put("goalTarget", 5); ps.put("goalNow", 0);
        model.addAttribute("pointSummary", ps);
        model.addAttribute("pointHistory", java.util.Collections.emptyList());

        // ✅ 여기: /WEB-INF/views/mypage/mypage.jsp
        return "member/mypage";
    }
    
 // 포인트 페이지
    @RequestMapping(value="/point.do", method=RequestMethod.GET)
    public String point(HttpSession session, Model model) {
        MemberVO login = (MemberVO) session.getAttribute("member");
        if (login == null) {
            session.setAttribute("action", "/member/point.do");
            return "redirect:/member/loginForm.do";
        }
        model.addAttribute("member", login);

        Map<String, Object> ps = new HashMap<>();
        ps.put("current", 0); ps.put("monthGain", 0); ps.put("monthSpend", 0);
        ps.put("totalGain", 0); ps.put("totalSpend", 0); ps.put("totalHours", 0);
        ps.put("tier","나무"); ps.put("nextNeeded", 1000); ps.put("goalTarget", 5); ps.put("goalNow", 0);
        model.addAttribute("pointSummary", ps);
        model.addAttribute("pointHistory", java.util.Collections.emptyList());

        // ✅ 여기: 슬래시 없이, /WEB-INF/views/mypage/point.jsp 를 가리키게
        return "member/point";
    }
    
    @RequestMapping(value="/updateProfile.do", method=RequestMethod.POST)
    public String updateProfile(
            @ModelAttribute MemberVO form,                     // id, name, email, phone, birth, address, notes …
            @RequestParam(value="avatar", required=false) MultipartFile avatar,
            HttpSession session,
            RedirectAttributes rAttr) throws Exception {

        MemberVO login = (MemberVO) session.getAttribute("member");
        if (login == null) return "redirect:/member/loginForm.do";

        // 본인 보호
        form.setId(login.getId());

        // === 아바타 저장 (선택) ===
        if (avatar != null && !avatar.isEmpty()) {
            String contentType = avatar.getContentType();
            String original    = avatar.getOriginalFilename();
            if (contentType == null) contentType = "";

            // 1) 타입/확장자 검증 (jpg/png)
            boolean okType = "image/jpeg".equalsIgnoreCase(contentType)
                          || "image/jpg".equalsIgnoreCase(contentType)
                          || "image/png".equalsIgnoreCase(contentType);
            String ext = "";
            if (original != null) {
                int dot = original.lastIndexOf('.');
                if (dot > -1 && dot < original.length()-1) {
                    ext = original.substring(dot).toLowerCase();
                }
            }
            if (!okType) {
                if (".jpg".equals(ext) || ".jpeg".equals(ext)) { contentType = "image/jpeg"; okType = true; }
                else if (".png".equals(ext)) { contentType = "image/png"; okType = true; }
            }
            if (!okType) {
                rAttr.addAttribute("result", "avatarBadType"); // JSP에서 안내문구 띄우면 좋음
                return "redirect:/member/mypage.do";
            }

            // 2) 크기 제한 (<= 2MB)
            if (avatar.getSize() > 2L * 1024L * 1024L) {
                rAttr.addAttribute("result", "avatarTooLarge");
                return "redirect:/member/mypage.do";
            }

            // 3) 저장 경로 (/resources/upload/profile/{userId}/)
            String baseDir = session.getServletContext().getRealPath("/resources/upload/profile");
            if (baseDir == null) {
                // 톰캣 realPath를 못 받는 환경 대비 임시/외부 경로 대체
                baseDir = System.getProperty("java.io.tmpdir") + java.io.File.separator + "profile";
            }
            java.io.File userDir = new java.io.File(baseDir, login.getId());
            if (!userDir.exists()) userDir.mkdirs();

            // 4) 파일명 생성
            String fileExt = ext;
            if (fileExt == null || fileExt.length() == 0) {
                fileExt = "image/png".equalsIgnoreCase(contentType) ? ".png" : ".jpg";
            }
            String fileName = java.util.UUID.randomUUID().toString().replace("-", "") + fileExt;

            // 5) 저장
            java.io.File dest = new java.io.File(userDir, fileName);
            try {
                avatar.transferTo(dest);
            } catch (Exception e) {
                rAttr.addAttribute("result", "avatarSaveError");
                return "redirect:/member/mypage.do";
            }

            // 6) DB에 저장할 상대경로 및 메타
            String relativePath = "/resources/upload/profile/" + login.getId() + "/" + fileName;
            form.setProfileImage(relativePath);
            form.setProfileImageOrig(original);
            form.setProfileImageType(contentType);
            form.setProfileImageSize(Long.valueOf(avatar.getSize()));
        }

        // 프로필 정보 저장
        memberService.updateProfile(form);

        // 세션 최신화
        MemberVO refreshed = memberService.findById(form.getId());
        session.setAttribute("member", refreshed != null ? refreshed : form);

        rAttr.addAttribute("result", "profileUpdated");
        return "redirect:/member/mypage.do";
    }

    
    @RequestMapping(value="/changePassword.do", method=RequestMethod.POST)
    public String changePassword(
            @RequestParam("id") String id,
            @RequestParam("currentPassword") String curPw,
            @RequestParam("newPassword") String newPw,
            HttpSession session,
            RedirectAttributes rAttr) throws Exception {

        MemberVO login = (MemberVO) session.getAttribute("member");
        if (login == null) return "redirect:/member/loginForm.do";

        // 본인 계정만 허용
        if (!login.getId().equals(id)) return "redirect:/member/mypage.do";

        // 현재 비번 검증 (네 구현에 맞게)
        boolean ok = memberService.verifyPassword(id, curPw); // 없으면 login(new MemberVO(id,curPw))로 대체 가능
        if (!ok) {
            rAttr.addAttribute("result", "pwMismatch");
            return "redirect:/member/mypage.do";
        }

        memberService.changePassword(id, newPw);
        rAttr.addAttribute("result", "pwChanged");
        return "redirect:/member/mypage.do";
    }

    private boolean isEmpty(String s) { return s == null || s.trim().length() == 0; }
}
