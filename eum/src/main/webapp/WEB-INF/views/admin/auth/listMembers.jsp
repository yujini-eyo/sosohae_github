<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!-- 관리자 세션 없으면 로그인으로 -->
<c:if test="${empty sessionScope.adminUser}">
  <c:redirect url="${ctx}/admin/auth/login.do"/>
</c:if>

<section style="max-width:1040px;margin:0 auto;padding:24px 16px;">
  <header style="display:flex;align-items:center;justify-content:space-between;margin-bottom:16px;">
    <h1 style="margin:0;font-size:1.4rem;">회원 목록</h1>
    <a href="<c:url value='/admin/main.do'/>"
       style="padding:8px 12px;border:1px solid #ddd;border-radius:10px;text-decoration:none;color:#59463E;background:#fff;">
      관리자 메인
    </a>
  </header>

  <div style="overflow:auto;border:1px solid #f0e6dc;border-radius:12px;background:#fff;">
    <table style="width:100%; border-collapse:collapse;">
      <thead>
        <tr style="background:#FFF3E6;">
          <th style="padding:10px;border-bottom:1px solid #f0e6dc;text-align:left;">ID</th>
          <th style="padding:10px;border-bottom:1px solid #f0e6dc;text-align:left;">이름</th>
          <th style="padding:10px;border-bottom:1px solid #f0e6dc;text-align:left;">이메일</th>
          <th style="padding:10px;border-bottom:1px solid #f0e6dc;text-align:left;">권한</th>
          <th style="padding:10px;border-bottom:1px solid #f0e6dc;text-align:left;">상태</th>
          <th style="padding:10px;border-bottom:1px solid #f0e6dc;text-align:left;">가입일</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="m" items="${membersList}">
          <tr>
            <td style="padding:10px;border-bottom:1px solid #f5efe8;"><c:out value="${m.id}"/></td>
            <td style="padding:10px;border-bottom:1px solid #f5efe8;"><c:out value="${m.name}"/></td>
            <td style="padding:10px;border-bottom:1px solid #f5efe8;"><c:out value="${m.email}"/></td>
            <td style="padding:10px;border-bottom:1px solid #f5efe8;"><c:out value="${m.role}"/></td>
            <td style="padding:10px;border-bottom:1px solid #f5efe8;"><c:out value="${m.status}"/></td>
            <td style="padding:10px;border-bottom:1px solid #f5efe8;"><c:out value="${m.createdAt}"/></td>
          </tr>
        </c:forEach>
        <c:if test="${empty membersList}">
          <tr>
            <td colspan="6" style="padding:16px; text-align:center; color:#806A5A;">등록된 회원이 없습니다.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</section>
