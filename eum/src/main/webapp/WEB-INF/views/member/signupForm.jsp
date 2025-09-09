<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>

<%-- URL들을 먼저 계산해두면 따옴표/닫힘 문제를 원천 차단 --%>
<c:url var="signupCssUrl"   value="/resources/css/signup.css"/>
<c:url var="signupJsUrl"    value="/resources/js/signup.js"/>
<c:url var="signupAction"   value="/member/signup.do"/>
<c:url var="loginFormUrl"   value="/member/loginForm.do"/>

<link rel="stylesheet" href="${signupCssUrl}"/>

<section id="signupView" class="login-view" role="region" aria-labelledby="signupTitle" style="--gap:0px;">
  <div class="content-inner" id="fitBox">
    <h1 id="signupTitle">회원가입</h1>

    <form class="login-box" method="post" action="${signupAction}" autocomplete="on">
      <c:if test="${not empty _csrf}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </c:if>

      <label for="sgId">아이디 *</label>
      <input type="text" id="sgId" name="id" placeholder="아이디 입력"
             autocomplete="username" autocapitalize="off" spellcheck="false" maxlength="30" required />

      <label for="sgPw">비밀번호 *</label>
      <!-- 반드시 name="password" -->
      <input type="password" id="sgPw" name="password" placeholder="비밀번호 입력"
             autocomplete="new-password" maxlength="64" required />

      <label for="sgEmail">이메일 *</label>
      <input type="text" id="sgEmail" name="email" placeholder="이메일 입력" inputmode="email" required />

      <label for="sgName">이름 *</label>
      <input type="text" id="sgName" name="name" placeholder="홍길동" required />

      <label for="sgBirth">생년월일 *</label>
      <input type="date" id="sgBirth" name="birth" required />

      <label for="sgAddress">주소 *</label>
      <input type="text" id="sgAddress" name="address" placeholder="대전광역시 ○○구 ○○동" required />

      <label for="sgPhone">전화번호 *</label>
      <div class="phone-wrap">
        <input type="tel" id="sgPhone" name="phone" placeholder="010-1234-5678" required />
        <button type="button" id="btnSendCode">인증</button>
      </div>

      <label for="sgCode">인증번호 *</label>
      <input type="text" id="sgCode" name="code" placeholder="숫자 6자리" required />

      <label for="sgNotes">
        특이사항 (병명 / 복용약 등) *
        <small style="font-weight: normal; font-size: 14px; color: #7C6557;">
          (없으면 아래에 체크해주세요)
        </small>
      </label>
      <input type="text" id="sgNotes" name="notes" placeholder="예: 고혈압 / 없음" required />

      <div class="checkbox-area">
        <label>
          <input type="checkbox" id="noNotesCheck" style="margin-right:6px;">
          <strong>특이사항 없어요</strong>
        </label>
      </div>

      <button type="submit" id="doSignup">가입하기</button>

      <div class="sub-buttons" aria-label="보조 링크" style="text-align:center; margin-top:14px;">
        <a href="${loginFormUrl}">이미 계정이 있으신가요? 로그인</a>
      </div>
    </form>
  </div>
</section>

<script defer src="${signupJsUrl}"></script>
