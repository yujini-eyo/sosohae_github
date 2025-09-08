package com.myspring.eum.admin.service;

import java.io.File;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.eum.admin.dao.AdminBoardDAO;
import com.myspring.eum.admin.vo.AdminArticleVO;

@Service("adminBoardService")
public class AdminBoardServiceImpl implements AdminBoardService {

    @Autowired
    private AdminBoardDAO adminBoardDAO;

    /** 프로젝트 환경에 맞게 경로 조정 */
    private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

    private void assertAdmin(HttpSession session){
        Object role = (session == null) ? null : session.getAttribute("userRole");
        boolean ok = (role instanceof String) && "ADMIN".equalsIgnoreCase((String) role);
        if (!ok) throw new SecurityException("FORBIDDEN: admin only");
    }

    @Override
    public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));

        Map<String, Object> param = new HashMap<String, Object>();
        int size = parseIntOrDefault(request.getParameter("size"), 10);
        int page = Math.max(1, parseIntOrDefault(request.getParameter("page"), 1));
        param.put("limit", size);
        param.put("offset", (page - 1) * size);

        List<AdminArticleVO> list = adminBoardDAO.selectAllArticles(param);
        ModelAndView mav = new ModelAndView("admin/board/listArticles");
        mav.addObject("articlesList", list);
        return mav;
    }

    @Override
    public ResponseEntity<String> addNewArticle(MultipartHttpServletRequest multipartRequest,
                                                HttpServletResponse response) throws Exception {
        assertAdmin(multipartRequest.getSession(false));
        multipartRequest.setCharacterEncoding("UTF-8");

        // 파라미터 수집: 단일값=String, 다중값=List<String>
        Map<String, Object> articleMap = new HashMap<String, Object>();
        java.util.Enumeration<?> names = multipartRequest.getParameterNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            String[] vals = multipartRequest.getParameterValues(key);
            if (vals == null) continue;

            if (vals.length == 1) {
                String v = vals[0];
                articleMap.put(key, v != null ? v.trim() : null);
            } else {
                for (int i = 0; i < vals.length; i++) {
                    if (vals[i] != null) vals[i] = vals[i].trim();
                }
                articleMap.put(key, java.util.Arrays.asList(vals));
            }
        }

        // 작성자
        HttpSession session = multipartRequest.getSession(false);
        Object uid = (session != null) ? session.getAttribute("userId") : null;
        articleMap.put("id", (uid instanceof String) ? ((String) uid) : "admin");
        articleMap.put("parentNO", 0);

        // ✅ isNotice 정규화(1/0) — 체크박스 미체크/부재 시 0
        articleMap.put("isNotice", normalizeIsNotice(articleMap.get("isNotice")));

        // 파일 저장 (단일 이미지: input name="imageFile")
        String imageFileName = handleSingleFileUpload(multipartRequest);
        if (imageFileName != null) {
            articleMap.put("imageFileName", imageFileName);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        try {
            // ⚠️ 매퍼/DAO는 #{isNotice} → INSERT ... is_notice 컬럼으로 매핑되어 있어야 함
            adminBoardDAO.insertNewArticle(articleMap);

            // (선택) articleNO 반환 시 temp → <articleNO> 이동 로직 추가

            String msg =
                "<script>"
              + "alert('새글을 추가했습니다.');"
              + "location.href='" + multipartRequest.getContextPath() + "/admin/board/listArticles.do';"
              + "</script>";
            return new ResponseEntity<String>(msg, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            if (imageFileName != null) {
                File f = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
                if (f.exists()) f.delete();
            }
            String msg =
                "<script>"
              + "alert('오류가 발생했습니다. 다시 시도해 주세요.');"
              + "history.back();"
              + "</script>";
            return new ResponseEntity<String>(msg, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ModelAndView viewArticle(int articleNO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));
        AdminArticleVO article = adminBoardDAO.selectArticle(articleNO);
        ModelAndView mav = new ModelAndView("admin/board/viewArticle");
        mav.addObject("article", article);
        return mav;
    }

    @Override
    public ResponseEntity<String> removeArticle(int articleNO,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        try {
            int cnt = adminBoardDAO.deleteArticle(articleNO);
            // 첨부 디렉터리 제거(선택)
            File dir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
            if (dir.exists()) FileUtils.deleteDirectory(dir);

            if (cnt > 0) {
                String msg =
                    "<script>"
                  + "alert('글을 삭제했습니다.');"
                  + "location.href='" + request.getContextPath() + "/admin/board/listArticles.do';"
                  + "</script>";
                return new ResponseEntity<String>(msg, headers, HttpStatus.OK);
            } else {
                String msg =
                    "<script>"
                  + "alert('대상이 없습니다.');"
                  + "location.href='" + request.getContextPath() + "/admin/board/listArticles.do';"
                  + "</script>";
                return new ResponseEntity<String>(msg, headers, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String msg =
                "<script>"
              + "alert('작업 중 오류가 발생했습니다.');"
              + "location.href='" + request.getContextPath() + "/admin/board/listArticles.do';"
              + "</script>";
            return new ResponseEntity<String>(msg, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* ========== 내부 유틸 ========== */

    private int parseIntOrDefault(String s, int def){
        try { return Integer.parseInt(s); } catch (Exception e){ return def; }
    }

    /** ✅ isNotice 값 정규화: "1"/"on"/"true"/true/숫자!=0 → 1, 그 외 0 */
    @SuppressWarnings("rawtypes")
    private int normalizeIsNotice(Object raw){
        if (raw == null) return 0;
        if (raw instanceof Number)   return (((Number) raw).intValue() != 0) ? 1 : 0;
        if (raw instanceof Boolean)  return ((Boolean) raw) ? 1 : 0;
        if (raw instanceof List) {
            List list = (List) raw;
            return list.isEmpty() ? 0 : normalizeIsNotice(list.get(0));
        }
        String s = String.valueOf(raw).trim();
        if (s.isEmpty()) return 0;
        if ("1".equals(s)) return 1;
        if ("on".equalsIgnoreCase(s)) return 1;
        if ("true".equalsIgnoreCase(s)) return 1;
        try { return (Integer.parseInt(s) != 0) ? 1 : 0; } catch (Exception ignore) {}
        return 0;
    }

    /** 단일 파일 업로드 (임시폴더: C:\board\article_image\temp) */
    private String handleSingleFileUpload(MultipartHttpServletRequest req) throws Exception {
        Iterator<String> fileNames = req.getFileNames();
        if (!fileNames.hasNext()) return null;

        String formName = fileNames.next();
        MultipartFile mFile = req.getFile(formName);
        if (mFile == null || mFile.isEmpty()) return null;

        String original = mFile.getOriginalFilename();
        if (original == null || original.length() == 0) return null;

        // 파일명 안전화
        original = original.replaceAll("[\\\\/:*?\"<>|]", "_");

        File tempDir = new File(ARTICLE_IMAGE_REPO + "\\temp");
        if (!tempDir.exists()) tempDir.mkdirs();

        File dest = new File(tempDir, original);
        mFile.transferTo(dest);
        return original;
    }
}
