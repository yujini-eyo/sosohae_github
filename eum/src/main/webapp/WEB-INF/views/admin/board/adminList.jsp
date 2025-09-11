<!-- /WEB-INF/views/admin/board/adminList.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<style>
  .toolbar{display:flex; justify-content:space-between; align-items:center; margin:18px 0;}
  .btn{display:inline-block; padding:8px 12px; border:1px solid #ddd; border-radius:8px; background:#fff; text-decoration:none; color:#333; cursor:pointer;}
  .btn.primary{background:#f6a96d; color:#fff; border-color:#f6a96d;}
  table.board{width:100%; border-collapse:collapse; background:#fff;}
  table.board th, table.board td{border-bottom:1px solid #eee; padding:10px; text-align:left; font-size:14px;}
  table.board th{background:#fafafa;}
  .right{text-align:right;}
  .muted{color:#777; font-size:12px;}
  .badge{display:inline-block; padding:2px 8px; border-radius:999px; font-size:12px; border:1px solid #f3caa8; background:#fff6ee; color:#9a5a1f; margin-left:4px;}
  .img-flag{color:#888; font-size:12px; margin-left:6px;}
  .actions form{display:inline;}
</style>

<div class="toolbar">
  <h2>관리자 게시글 목록</h2>
  <c:if test="${sessionScope.userRole eq 'ADMIN' || (sessionScope.member ne null && sessionScope.member.role eq 'ADMIN')}">
    <a class="btn primary" href="${ctx}/admin/board/adminWriteFrom.do">글 작성</a>
  </c:if>
</div>

<table class="board">
  <thead>
  <tr>
    <th style="width:80px">번호</th>
    <th>제목</th>
    <th style="width:140px">작성자</th>
    <th style="width:180px">작성일</th>
    <th style="width:160px">공지</th>
    <th style="width:220px" class="right">관리</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="a" items="${articlesList}">
    <tr>
      <td><c:out value="${a.articleNO}"/></td>
      <td>
        <a href="${ctx}/admin/board/adminview.do?articleNO=${a.articleNO}">
          <c:out value="${a.title}"/>
        </a>
        <c:if test="${a.isNotice}">
          <span class="badge">공지</span>
        </c:if>
        <c:if test="${not empty a.imageFileName}">
          <span class="img-flag">[첨부]</span>
        </c:if>
      </td>
      <td><c:out value="${a.id}"/></td>
      <td><span class="muted"><fmt:formatDate value="${a.writeDate}" pattern="yyyy-MM-dd HH:mm"/></span></td>
      <td>
        <c:choose>
          <c:when test="${a.isNotice}">
            <form method="post" action="${ctx}/admin/board/adminnoticeOff.do" onsubmit="return confirm('공지 해제할까요?');">
              <input type="hidden" name="articleNO" value="${a.articleNO}">
              <c:if test="${not empty _csrf}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              </c:if>
              <button type="submit" class="btn">공지 해제</button>
            </form>
          </c:when>
          <c:otherwise>
            <form method="post" action="${ctx}/admin/board/adminnoticeOn.do" onsubmit="return confirm('공지로 설정할까요?');">
              <input type="hidden" name="articleNO" value="${a.articleNO}">
              <c:if test="${not empty _csrf}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              </c:if>
              <button type="submit" class="btn">공지 설정</button>
            </form>
          </c:otherwise>
        </c:choose>
      </td>
      <td class="right">
        <a class="btn" href="${ctx}/admin/board/adminview.do?articleNO=${a.articleNO}">보기</a>
        <form method="post" action="${ctx}/admin/board/adminremoveArticle.do" onsubmit="return confirm('삭제하시겠어요?');">
          <input type="hidden" name="articleNO" value="${a.articleNO}">
          <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          </c:if>
          <button type="submit" class="btn">삭제</button>
        </form>
      </td>
    </tr>
  </c:forEach>

  <c:if test="${empty articlesList}">
    <tr><td colspan="6" class="muted" style="text-align:center; padding:36px 0;">게시글이 없습니다.</td></tr>
  </c:if>
  </tbody>
</table>
