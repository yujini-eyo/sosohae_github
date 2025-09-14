<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 분리된 CSS/JS -->
<link rel="stylesheet" href="<c:url value='/resources/css/board.css'/>" />
<script defer src="<c:url value='/resources/js/board.js'/>"></script>

<!-- Tiles 레이아웃이 <html>/<head>/<body>를 감쌉니다. 본문만 작성 -->
<section id="main" class="board" aria-labelledby="boardTitle">
  <header class="board-header">
    <div>
      <h1 id="boardTitle" class="board-title">도움 매칭 게시판</h1>
      <p class="desc">요청 글 목록입니다. 제목을 클릭하면 상세 보기로 이동합니다.</p>
    </div>

    <!-- 간단 필터/액션 (서버연동 전, 기본 버튼만 동작) -->
    <div class="toolbar" aria-label="검색 및 액션">
      <div class="filters">
        <label class="field" aria-label="검색어">
          🔎
          <input id="search" type="search" placeholder="제목, 작성자 검색" autocomplete="off" />
        </label>
      </div>
      <div class="cta">
        <button class="btn ghost" id="resetBtn" type="button">초기화</button>
        <a class="btn primary" href="<c:url value='/board/articleForm.do'/>">글쓰기</a>
      </div>
    </div>
  </header>

  <div class="table-wrap">
    <table aria-describedby="desc-req">
      <caption id="desc-req" class="sr-only" style="position:absolute;left:-9999px;">도움 요청 목록</caption>
      <thead>
        <tr>
          <th scope="col">번호</th>
          <th scope="col">제목</th>
          <th scope="col">작성자</th>
          <th scope="col">작성일</th>
          <th scope="col">이미지</th>
          <th scope="col">액션</th>
        </tr>
      </thead>
      <tbody id="tbody-req">
        <c:choose>
          <c:when test="${not empty articlesList}">
            <c:forEach var="a" items="${articlesList}">
              <tr>
                <td data-label="번호"><c:out value="${a.articleNO}"/></td>
                <td data-label="제목" style="text-align:left">
                  <a class="row-link"
                     href="<c:url value='/board/viewArticle.do'><c:param name='articleNO' value='${a.articleNO}'/></c:url>">
                    <c:out value="${a.title}"/>
                  </a>
                </td>
                <td data-label="작성자"><c:out value="${a.id}"/></td>
                <td data-label="작성일">
                  <c:choose>
                    <c:when test="${not empty a.writeDate}">
                      <fmt:formatDate value="${a.writeDate}" pattern="yyyy-MM-dd HH:mm"/>
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
                <td data-label="이미지">
                  <c:choose>
                    <c:when test="${not empty a.imageFileName}">첨부</c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
                <td data-label="액션">
                  <div class="row-actions">
                    <a class="btn primary" style="padding:8px 12px"
                       href="<c:url value='/board/viewArticle.do'><c:param name='articleNO' value='${a.articleNO}'/></c:url>">상세</a>
                  </div>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="6" style="padding:22px">등록된 글이 없습니다.</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>

  <!-- (선택) 페이징 자리 -->
  <nav class="pagination" id="pagination-req" aria-label="페이지"></nav>
</section>