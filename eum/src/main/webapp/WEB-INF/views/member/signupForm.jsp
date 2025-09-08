<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- HTML 버전과 동일한 스코프/레이아웃을 위해 signup.css / signup.js 사용 -->
<link rel="stylesheet" href="<c:url value='/resources/css/signup.css'/>"/>

<section id="signupView" class="login-view" role="region" aria-labelledby="signupTitle" style="--gap:0px;">
  <div class="content-inner" id="fitBox">
    <h1 id="signupTitle">회원가입</h1>

    <!-- HTML 디자인과 동일한 입력 UI, action은 서버연동 -->
    <form class="login-box" method="post" action="${contextPath}/eum/loginForm.do" autocomplete="on">
      <c:if test="${not empty _csrf}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </c:if>

      <label for="sgId">아이디 *</label>
      <input type="text" id="sgId" name="id" placeholder="아이디 입력"
             autocomplete="username" autocapitalize="off" spellcheck="false" maxlength="30" required />

      <label for="sgPw">비밀번호 *</label>
      <input type="password" id="sgPw" name="pwd" placeholder="비밀번호 입력"
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

      <div class="info-text" aria-live="polite">
        ※ 1365 자원봉사 연동이나 가족 계정 연결은<br/>
        가입 후 <strong>‘내정보’</strong> 메뉴에서 설정하실 수 있어요.
      </div>

      <div class="sub-buttons" aria-label="보조 링크" style="text-align:center; margin-top:14px;">
        <a href="${contextPath}/eum/loginForm.do">이미 계정이 있으신가요? 로그인</a>
      </div>
    </form>
  </div>
</section>

<script defer src="<c:url value='/resources/js/signup.js'/>"></script>
