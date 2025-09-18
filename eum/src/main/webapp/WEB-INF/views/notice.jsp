<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/notice.css'/>">
<script defer src="<c:url value='/resources/js/notice.js'/>"></script>

<section class="page-notice">
  <div class="notice">

    <!-- 상단 헤더 -->
    <header class="notice-header">
      <h1 class="notice-title">공지사항</h1>
      <p class="desc">중요 안내와 소식을 확인하세요.</p>

      <!-- 툴바 -->
      <div class="toolbar">
        <c:if test="${not empty login && login.role == 'ADMIN'}">
          <!-- ▼ 여기만 수정 -->
          <a class="btn primary" href="<c:url value='/admin/board/adminWriteForm.do'/>">
            <i class="fa-regular fa-pen-to-square" aria-hidden="true"></i> 글쓰기
          </a>
        </c:if>
      </div>
    </header>

    <!-- 목록 테이블 -->
    <div class="table-wrap">
      <table aria-label="공지사항 목록">
        <thead>
          <tr>
            <th>제목</th>
            <th>작성일</th>
            <th>조회수</th>
          </tr>
        </thead>
        <tbody id="tbody-notice">
          <c:choose>
            <c:when test="${empty noticeList}">
              <tr class="empty"><td colspan="3">등록된 공지사항이 없습니다.</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach items="${noticeList}" var="n">
                <tr>
                  <td class="td-title" data-label="제목">
                    <a href="<c:url value='/notice/view.do'/>?articleNO=${n.articleNO}">
                      <c:out value="${n.title}" />
                    </a>
                  </td>

                  <td class="td-date" data-label="작성일">
                    <fmt:formatDate value="${n.writeDate}" pattern="yyyy.MM.dd"/>
                  </td>

                  <td class="td-views" data-label="조회수">
                    <c:choose>
                      <c:when test="${not empty n.viewCnt}">
                        <fmt:formatNumber value="${n.viewCnt}" pattern="#,###"/>
                      </c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>

    <!-- 페이지 네비게이션 -->
    <nav id="pagination-notice" class="pagination" aria-label="페이지 이동"></nav>

  </div>
</section>
