<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- (옵션) 레이아웃에서 공통으로 넣지 않는다면 여기서 직접 로드 -->
<link rel="stylesheet" href="<c:url value='/resources/css/login.css'/>" />

<section id="loginView"
         class="login-view"
         role="region"
         aria-labelledby="loginTitle"
         data-login-failed="${result eq 'loginFailed'}">
  <div class="content-inner" id="fitBox">
    <h1 id="loginTitle">로그인</h1>

    <c:if test="${result == 'loginFailed'}">
      <div class="alert error" role="alert" aria-live="assertive">
        아이디 또는 비밀번호가 올바르지 않습니다. 다시 시도해 주세요.
      </div>
    </c:if>

    <form class="login-box" method="post"
          action="${contextPath}/eum/login.do" autocomplete="on">
      <c:if test="${not empty _csrf}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </c:if>

      <label for="loginId">아이디</label>
      <input type="text" id="loginId" name="id" placeholder="아이디"
             value="<c:out value='${param.id}'/>"
             autocomplete="username" autocapitalize="off" spellcheck="false"
             maxlength="30" required />

      <label for="loginPw">비밀번호</label>
      <input type="password" id="loginPw" name="password" placeholder="비밀번호"
             autocomplete="current-password" maxlength="64" required />

      <button type="submit" id="doLogin">로그인</button>

      <div class="sub-buttons" aria-label="보조 링크">
        <a href="${contextPath}/signupForm.do">회원가입</a> |
        <a href="#">아이디 · 비밀번호 찾기</a>
      </div>
    </form>
  </div>
</section>

<script defer src="<c:url value='/resources/js/login.js'/>"></script>
