<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/notice.css'/>" />

<div class="page-notice" role="region" aria-labelledby="noticeTitle">
  <div class="container">
    <!-- Hero -->
    <section class="hero" aria-labelledby="noticeTitle">
      <h1 id="noticeTitle">📢 공지사항</h1>
      <p class="subtitle">서비스 점검, 업데이트, 정책 안내 등 중요한 소식을 전해드립니다.</p>
    </section>

    <!-- 툴바 -->
    <section class="toolbar" aria-label="검색 및 정렬">
      <form id="noticeSearchForm" class="filters" method="get" action="${ctx}/eum/notice.do" role="search">
        <label class="field" aria-label="검색어">
          🔎 <input id="q" name="q" type="search" placeholder="제목, 내용 검색" value="${param.q}" autocomplete="off" />
        </label>
        <label class="field" aria-label="정렬">
          ↕️
          <select id="sort" name="sort">
            <option value="recent" <c:if test="${param.sort == 'recent' || empty param.sort}">selected</c:if>>최신순</option>
            <option value="views"  <c:if test="${param.sort == 'views'}">selected</c:if>>조회순</option>
            <option value="title"  <c:if test="${param.sort == 'title'}">selected</c:if>>제목순</option>
          </select>
        </label>
        <button class="btn" id="searchBtn" type="submit">검색</button>
        <button class="btn ghost" id="resetBtn" type="button" onclick="location.href='${ctx}/eum/notice.do'">초기화</button>
      </form>
    </section>

    <!-- 목록 -->
    <section class="list-wrap">
      <div class="table-wrap">
        <table aria-describedby="desc-notice">
          <caption id="desc-notice" class="sr-only">공지사항 목록</caption>

          <colgroup>
            <col style="width:88px" />
            <col style="width:auto" />
            <col style="width:140px" />
            <col style="width:90px" />
          </colgroup>

          <thead>
            <tr>
              <th scope="col">번호</th>
              <th scope="col">제목</th>
              <th scope="col">작성일</th>
              <th scope="col">조회</th>
            </tr>
          </thead>

          <tbody id="tbody-notice">
            <c:choose>
              <c:when test="${not empty noticeList}">
                <c:forEach items="${noticeList}" var="n" varStatus="i">
                  <tr>
                    <td class="td-no"><c:out value="${n.articleNO != null ? n.articleNO : (n.noticeNo != null ? n.noticeNo : i.index + 1)}"/></td>
                    <td class="td-title">
                      <%-- 퍼블릭 뷰가 없다면 아래 링크를 /admin/board/viewArticle.do?articleNO= 로 바꿔도 됩니다. --%>
                      <a href="<c:url value='/notice/view.do'/>?articleNO=${n.articleNO != null ? n.articleNO : n.noticeNo}">
                        <c:out value="${n.title}"/>
                        <c:if test="${n.isNotice == 1 or n.is_notice == 1 or n.isPinned == true}">
                          <span class="badge">고정</span>
                        </c:if>
                      </a>
                    </td>
                    <td class="td-date">
                      <fmt:formatDate value="${n.writeDate != null ? n.writeDate : n.createDate}" pattern="yyyy.MM.dd" />
                    </td>
                    <td class="td-views"><c:out value="${n.viewCnt != null ? n.viewCnt : (n.views != null ? n.views : '-')}"/></td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr class="empty">
                  <td colspan="4">등록된 공지사항이 없습니다.</td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>

      <!-- 페이지네이션 (pageMaker가 있을 때만 표시) -->
      <nav class="pagination" id="pagination-notice" aria-label="공지사항 페이지">
        <c:if test="${not empty pageMaker}">
          <c:if test="${pageMaker.prev}">
            <a class="page" href="${ctx}/eum/notice.do?page=${pageMaker.startPage - 1}&q=${param.q}&sort=${param.sort}">이전</a>
          </c:if>
          <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="p">
            <a class="page <c:out value='${p == pageMaker.cri.page ? "is-active" : ""}'/>"
               href="${ctx}/eum/notice.do?page=${p}&q=${param.q}&sort=${param.sort}">${p}</a>
          </c:forEach>
          <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
            <a class="page" href="${ctx}/eum/notice.do?page=${pageMaker.endPage + 1}&q=${param.q}&sort=${param.sort}">다음</a>
          </c:if>
        </c:if>
      </nav>
    </section>
  </div>
</div>

<script defer src="<c:url value='/resources/js/notice.js'/>"></script>
