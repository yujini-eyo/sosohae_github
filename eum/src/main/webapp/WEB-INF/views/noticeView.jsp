<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="<c:url value='/resources/css/notice.css'/>">

<section class="page-notice notice-view">
  <div class="notice">
    <header class="notice-header">
      <h1 class="notice-title">공지사항</h1>
    </header>

    <article class="notice-body">
      <h2 class="notice-h2"><c:out value="${article.title}"/></h2>

      <p class="notice-meta">
        <time datetime="<fmt:formatDate value='${article.writeDate}' pattern='yyyy-MM-dd'/>">
          <fmt:formatDate value='${article.writeDate}' pattern='yyyy.MM.dd' />
        </time>
        <span class="dot" aria-hidden="true">·</span>
        <span class="views">조회수 <c:out value="${article.viewCnt}"/></span>
      </p>

      <div class="notice-content rte">
        <c:out value="${article.content}" escapeXml="false"/>
      </div>

      <nav class="notice-actions" aria-label="상세 하단 버튼">
        <a class="btn" href="<c:url value='/notice.do'/>">목록으로</a>
      </nav>
    </article>
  </div>
</section>
