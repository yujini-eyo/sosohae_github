<!-- /WEB-INF/views/admin/board/adminview.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="a"   value="${article}" />

<style>
  .meta{color:#666; font-size:13px; margin-bottom:10px;}
  .actions{margin-top:20px; display:flex; gap:8px; flex-wrap:wrap;}
  .btn{display:inline-block; padding:8px 14px; border:1px solid #ddd; border-radius:8px; text-decoration:none; color:#333; background:#fff;}
  .btn.primary{background:#f6a96d; color:#fff; border-color:#f6a96d;}
  .btn.danger{border-color:#f1c3c3;}
  .content{white-space:pre-wrap; background:#fff; padding:16px; border:1px solid #eee; border-radius:8px;}
  .img-wrap{margin:16px 0;}
  img{max-width:100%; height:auto; border:1px solid #eee; border-radius:8px;}
  .badge{display:inline-block; padding:2px 8px; border-radius:999px; font-size:12px; border:1px solid #f3caa8; background:#fff6ee; color:#9a5a1f; margin-left:6px;}
</style>

<c:choose>
  <c:when test="${not empty a}">
    <h2>
      <c:out value="${a.title}"/>
      <c:if test="${a.isNotice}"><span class="badge">공지</span></c:if>
    </h2>

    <div class="meta">
      글번호 <c:out value="${a.articleNO}"/> ·
      작성자 <c:out value="${a.id}"/>
      <c:if test="${not empty a.writeDate}">
        · 작성일 <fmt:formatDate value="${a.writeDate}" pattern="yyyy-MM-dd HH:mm"/>
      </c:if>
    </div>

    <c:if test="${not empty a.imageFileName}">
      <div class="img-wrap">
        <img alt="첨부 이미지"
             src="${ctx}/resources/adminboardImages/${a.imageFileName}"
             onerror="this.style.display='none'">
      </div>
    </c:if>

    <div class="content"><c:out value="${a.content}"/></div>

    <div class="actions">
      <a class="btn" href="${ctx}/admin/board/adminList.do">목록</a>

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

      <form method="post" action="${ctx}/admin/board/adminremoveArticle.do" onsubmit="return confirm('정말 삭제할까요?');">
        <input type="hidden" name="articleNO" value="${a.articleNO}">
        <c:if test="${not empty _csrf}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </c:if>
        <button type="submit" class="btn danger">삭제</button>
      </form>
    </div>
  </c:when>
  <c:otherwise>
    <p>게시글을 찾을 수 없습니다.</p>
    <a class="btn" href="${ctx}/admin/board/adminList.do">목록</a>
  </c:otherwise>
</c:choose>
