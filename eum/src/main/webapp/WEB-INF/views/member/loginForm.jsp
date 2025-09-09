<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%-- ✔ URL들을 먼저 계산해두면 '<c:url' 미종료 오류를 원천 차단 --%>
<c:url var="loginCssUrl"    value="/resources/css/login.css"/>
<c:url var="loginActionUrl" value="/member/login.do"/>
<c:url var="signupUrl"      value="/member/signupForm.do"/>

<link rel="stylesheet" href="${loginCssUrl}" />

<section id="loginView" class="login-view" role="region" aria-labelledby="loginTitle">
  <div class="content-inner" id="fitBox">
    <h1 id="loginTitle">로그인</h1>

    <c:if test="${not empty loginError}">
      <div class="alert error" role="alert" aria-live="assertive" id="loginErrorBox">
        <c:out value="${loginError}"/>
      </div>
    </c:if>

    <form class="login-box" method="post" action="${loginActionUrl}" autocomplete="on">
      <c:if test="${not empty _csrf}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </c:if>

      <label for="loginId">아이디</label>
      <input type="text" id="loginId" name="id" placeholder="아이디"
             value="${fn:escapeXml(param.id)}"
             autocomplete="username" autocapitalize="off" spellcheck="false"
             maxlength="30" required />

      <label for="loginPw">비밀번호</label>
      <input type="password" id="loginPw" name="password" placeholder="비밀번호"
             autocomplete="current-password" maxlength="64" required />

      <button type="submit" id="doLogin">로그인</button>

      <div class="sub-buttons" aria-label="보조 링크">
        <a href="${signupUrl}">회원가입</a> |
        <a href="#">아이디 · 비밀번호 찾기</a>
      </div>
    </form>
  </div>
</section>

<script>
  (function(){
    var err = document.getElementById('loginErrorBox');
    if(err && err.focus){ err.tabIndex = -1; err.focus(); }
  })();
</script>
