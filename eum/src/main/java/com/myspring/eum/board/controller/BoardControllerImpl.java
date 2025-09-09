package com.myspring.eum.board.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
// @GetMapping 미사용
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.eum.board.service.BoardService;
import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController {
    private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

    @Autowired private BoardService boardService;
    @Autowired private ArticleVO articleVO;

    /** 목록 */
    @Override
    @RequestMapping(value = "/board/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ArticleVO> articlesList = boardService.listArticles();
        System.out.println("[Board] articlesList size = " + (articlesList == null ? "null" : articlesList.size()));
        ModelAndView mav = new ModelAndView("forward:/WEB-INF/views/board/listArticles.jsp");
        mav.addObject("articlesList", articlesList);
        return mav;
    }


    /** 이미지 목록 (단일 이미지/Article 기반) */
    @Override
    @RequestMapping(value = "/board/listImages.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView listImages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ArticleVO> imageFileList = boardService.listArticles(); // ArticleVO에 imageFileName 포함
        ModelAndView mav = new ModelAndView("forward:/WEB-INF/views/board/listImages.jsp");
        mav.addObject("imageFileList", imageFileList); // JSP 키 일치
        return mav;
    }

    /** 글쓰기 폼 */
    @Override
    @RequestMapping(value="/board/articleForm.do", method=RequestMethod.GET)
    public String articleForm() throws Exception {
        return "forward:/WEB-INF/views/board/articleForm.jsp";
    }

    /** 답글 폼(추후 parentNO 처리 확장) */
    @Override
    @RequestMapping(value="/board/replyForm.do", method=RequestMethod.GET)
    public String replyForm() throws Exception {
        return "forward:/WEB-INF/views/board/replyForm.jsp";
    }

    /** 단일 이미지 글 등록 */
    @Override
    @RequestMapping(value = "/board/addNewArticle.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addNewArticle(MultipartHttpServletRequest multipartRequest,
                                                HttpServletResponse response) throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");

        Map<String, Object> articleMap = new HashMap<>();
        Enumeration<?> enu = multipartRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            String value = multipartRequest.getParameter(name);
            articleMap.put(name, value);
        }

        String imageFileName = upload(multipartRequest);

        HttpSession session = multipartRequest.getSession(false);
        MemberVO memberVO = (session != null) ? (MemberVO) session.getAttribute("member") : null;
        String id = (memberVO != null) ? memberVO.getId() : "guest";

        articleMap.put("parentNO", 0);
        articleMap.put("id", id);
        articleMap.put("imageFileName", imageFileName);

        String message;
        ResponseEntity<String> resEnt;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        try {
            int articleNO = boardService.addNewArticle(articleMap);
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
                File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
                FileUtils.moveFileToDirectory(srcFile, destDir, true);
            }

            message = "<script>";
            message += " alert('새글을 추가했습니다.');";
            message += " location.href='" + multipartRequest.getContextPath() + "/board/listArticles.do'; ";
            message += " </script>";
            resEnt = new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
                if (srcFile.exists()) srcFile.delete();
            }

            message = "<script>";
            message += " alert('오류가 발생했습니다. 다시 시도해 주세요.');";
            message += " location.href='" + multipartRequest.getContextPath() + "/board/articleForm.do'; ";
            message += " </script>";
            resEnt = new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
            e.printStackTrace();
        }
        return resEnt;
    }

    /** 단일 이미지 상세 */
    @Override
    @RequestMapping(value = "/board/viewArticle.do", method = RequestMethod.GET)
    public ModelAndView viewArticle(@RequestParam("articleNO") long articleNO,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        articleVO = boardService.viewArticle(articleNO);
        ModelAndView mav = new ModelAndView("forward:/WEB-INF/views/board/viewArticle.jsp");
        mav.addObject("article", articleVO);
        return mav;
    }

    /** 단일 이미지 수정 */
    @Override
    @RequestMapping(value = "/board/modArticle.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> modArticle(MultipartHttpServletRequest multipartRequest,
                                             HttpServletResponse response) throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");
        Map<String, Object> articleMap = new HashMap<>();
        Enumeration<?> enu = multipartRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            String value = multipartRequest.getParameter(name);
            articleMap.put(name, value);
        }

        String imageFileName = upload(multipartRequest);
        articleMap.put("imageFileName", imageFileName);

        String articleNO = (String) articleMap.get("articleNO");

        String message;
        ResponseEntity<String> resEnt;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        try {
            boardService.modArticle(articleMap);
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
                File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
                FileUtils.moveFileToDirectory(srcFile, destDir, true);

                String originalFileName = (String) articleMap.get("originalFileName");
                if (originalFileName != null && !originalFileName.isEmpty()) {
                    File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
                    if (oldFile.exists()) oldFile.delete();
                }
            }
            message = "<script>";
            message += " alert('글을 수정했습니다.');";
            message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO=" + articleNO + "';";
            message += " </script>";
            resEnt = new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
                if (srcFile.exists()) srcFile.delete();
            }
            message = "<script>";
            message += " alert('오류가 발생했습니다. 다시 수정해 주세요.');";
            message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO=" + articleNO + "';";
            message += " </script>";
            resEnt = new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
        }
        return resEnt;
    }

    /** 글 삭제 */
    @Override
    @RequestMapping(value = "/board/removeArticle.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> removeArticle(@RequestParam("articleNO") long articleNO,
                                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        String message;
        ResponseEntity<String> resEnt;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        try {
            boardService.removeArticle(articleNO);
            File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
            if (destDir.exists()) FileUtils.deleteDirectory(destDir);

            message = "<script>";
            message += " alert('글을 삭제했습니다.');";
            message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
            message += " </script>";
            resEnt = new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);

        } catch (Exception e) {
            message = "<script>";
            message += " alert('작업 중 오류가 발생했습니다. 다시 시도해 주세요.');";
            message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
            message += " </script>";
            resEnt = new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
            e.printStackTrace();
        }
        return resEnt;
    }

    /** 단일 파일 업로드 (temp 저장) */
    private String upload(MultipartHttpServletRequest multipartRequest) throws Exception {
        String imageFileName = null;
        Iterator<String> fileNames = multipartRequest.getFileNames();

        // temp 디렉터리 보장
        File tempDir = new File(ARTICLE_IMAGE_REPO + "\\temp");
        if (!tempDir.exists()) tempDir.mkdirs();

        while (fileNames.hasNext()) {
            String partName = fileNames.next();
            MultipartFile mFile = multipartRequest.getFile(partName);
            if (mFile == null || mFile.isEmpty()) continue;

            imageFileName = mFile.getOriginalFilename();
            if (imageFileName == null || imageFileName.isEmpty()) continue;

            File dest = new File(tempDir, imageFileName);
            mFile.transferTo(dest); // 임시 저장
        }
        return imageFileName;
    }
}
