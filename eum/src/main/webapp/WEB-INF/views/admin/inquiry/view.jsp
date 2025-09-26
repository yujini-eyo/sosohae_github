<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
  // 기대 모델: row(InquiryVO)
%>
<div class="container">
  <h2>문의 상세</h2>
  <table border="1" cellpadding="6" cellspacing="0" width="100%">
    <tr><th>ID</th><td>${row.inquiryId}</td></tr>
    <tr><th>회원ID</th><td>${row.memberId}</td></tr>
    <tr><th>제목</th><td>${row.title}</td></tr>
    <tr><th>내용</th><td><pre style="white-space:pre-wrap;">${row.content}</pre></td></tr>
    <tr><th>상태</th><td>${row.status}</td></tr>
    <tr><th>등록일</th><td>${row.createdAt}</td></tr>
    <tr><th>답변일</th><td>${row.answeredAt}</td></tr>
  </table>

  <h3 style="margin-top:20px;">관리자 답변</h3>
  <form method="post" action="${pageContext.request.contextPath}/admin/inquiry/reply.do">
    <input type="hidden" name="id" value="${row.inquiryId}"/>
    <textarea name="reply" rows="6" style="width:100%">${row.adminReply}</textarea>
    <div style="margin-top:10px;">
      <button type="submit">답변 저장</button>
      <a href="${pageContext.request.contextPath}/admin/inquiry/list.do">목록</a>
    </div>
  </form>
</div>
