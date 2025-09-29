<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/applicants.css'/>" />
<script defer src="<c:url value='/resources/js/applicants.js'/>"></script>

<div class="wrap">
  <h2 class="page-header">지원자 목록 (글번호: ${articleNO})</h2>

  <%-- 데이터가 없을 때의 메시지 --%>
  <c:if test="${empty applicants}">
    <p class="alert danger">아직 지원자가 없습니다.</p> <!-- 더미데이터 지울 때 주석 풀어주기 -->
  </c:if>

<div class="card">
  <div class="table-container">
    <table class="applicant-table">
      <thead>
        <tr>
          <th>#</th>
          <th>지원자 ID</th>
          <th>메시지</th>
          <th>상태</th>
          <th>신청시각</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty applicants}">
            <c:forEach var="ap" items="${applicants}" varStatus="st">
              <tr>
                <td>${st.count}</td>
                <td><c:out value="${ap.volunteerId}"/></td>
                <td><c:out value="${ap.message}"/></td>
                <td>
                  <span class="badge">
                    <c:out value="${ap.status}"/>
                  </span>
                </td>
                <td>
                  <fmt:formatDate value="${ap.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="5" style="text-align:center;">아직 지원자가 없습니다.</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>


  <%--
    [더미 데이터]
    원래는 컨트롤러에서 전달받아야 하지만, 화면 디자인 확인을 위해 여기에 더미 데이터를 추가합니다.
    (실제 배포 시에는 아래 코드를 삭제하고 <c:when test="${not empty applicants}"> 사용)
  --%>
	<!-- <div class="card">
    <div class="table-container">
      <table class="applicant-table">
        <thead>
          <tr>
            <th>#</th>
            <th>지원자 ID</th>
            <th>메시지</th>
            <th>상태</th>
            <th>신청시각</th>
            <th>액션</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>user_a</td>
            <td>안녕하세요, 도와드리고 싶습니다!</td>
            <td><span class="badge selected">선정됨</span></td>
            <td>2025-09-15 10:30</td>
            <td>-</td>
          </tr>
          <tr>
            <td>2</td>
            <td>user_b</td>
            <td>시간 괜찮습니다. 연락주세요.</td>
            <td><span class="badge">신청</span></td>
            <td>2025-09-15 11:20</td>
            <td>
              <form method="post" action="#">
                <button type="submit" class="btn primary">선정</button>
              </form>
            </td>
          </tr>
          <tr>
            <td>3</td>
            <td>user_c</td>
            <td>오후에 가능합니다.</td>
            <td><span class="badge rejected">거절됨</span></td>
            <td>2025-09-15 14:00</td>
            <td>-</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div> -->