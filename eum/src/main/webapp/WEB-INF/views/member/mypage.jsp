<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />


<!-- 내정보 전용 CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/mypage.css'/>" />

<section id="mypageView"
         class="mypage-view"
         role="region"
         aria-labelledby="mypageTitle"
         data-update-ok="${param.result eq 'profileUpdated'}"
         data-pw-ok="${param.result eq 'pwChanged'}"
         data-pw-fail="${param.result eq 'pwMismatch'}">
  <div class="content-wrap">
    <header class="section-title">
      <h1 id="mypageTitle">내 정보</h1>
      <small class="muted">개인정보 및 보안을 관리하고 업데이트하세요</small>
    </header>

    <!-- 알림영역 (쿼리 파라미터/플래시 메시지용) -->
    <c:if test="${param.result == 'profileUpdated'}">
      <div class="alert success" role="status">내 정보가 저장되었습니다.</div>
    </c:if>
    <c:if test="${param.result == 'pwChanged'}">
      <div class="alert success" role="status">비밀번호가 변경되었습니다.</div>
    </c:if>
    <c:if test="${param.result == 'pwMismatch'}">
      <div class="alert error" role="alert">비밀번호가 일치하지 않습니다. 다시 시도해 주세요.</div>
    </c:if>

    <!-- 개인정보 카드 -->
    <article class="card" aria-labelledby="personalTitle">
      <div class="card-head">
        <h2 class="card-title" id="personalTitle">개인정보</h2>
        <div class="card-actions">
          <button type="button" class="btn" id="editBtn"><i class="fa-regular fa-pen-to-square"></i> 편집</button>
          <button type="button" class="btn gray" id="cancelBtn" style="display:none">취소</button>
          <button type="button" class="btn primary" id="saveBtn" style="display:none"><i class="fa-regular fa-floppy-disk"></i> 저장</button>
        </div>
      </div>

      <!-- 아바타/프로필 사진 -->
      <div class="avatar-row">
        <div class="avatar" id="avatarPreview">
          <c:out value="${empty member.name ? 'U' : fn:substring(member.name,0,1)}" />
        </div>
        <div class="avatar-meta">
          <div class="help">프로필 사진은 JPG/PNG, 최대 2MB</div>
          <button type="button" class="btn ghost" id="avatarBtn"><i class="fa-regular fa-image"></i> 사진 변경</button>
        </div>
      </div>
      <!-- 실제 파일 입력은 폼 내부에 있음 -->

      <!-- 읽기 모드 -->
      <dl class="info-grid" id="readBlock">
        <div>
          <dt>아이디</dt>
          <dd id="v_id"><c:out value="${member.id}" /></dd>
        </div>
        <div>
          <dt>이름</dt>
          <dd id="v_name"><c:out value="${member.name}" /></dd>
        </div>
        <div>
          <dt>이메일</dt>
          <dd id="v_email"><c:out value="${member.email}" /></dd>
        </div>
        <div>
          <dt>전화번호</dt>
          <dd id="v_phone"><c:out value="${member.phone}" /></dd>
        </div>
        <div>
          <dt>생년월일</dt>
          <dd id="v_birth"><fmt:formatDate value="${member.birth}" pattern="yyyy-MM-dd"/></dd>
        </div>
        <div style="grid-column:1/-1">
          <dt>주소</dt>
          <dd id="v_address"><c:out value="${member.address}" /></dd>
        </div>
        <div style="grid-column:1/-1">
          <dt>특이사항</dt>
          <dd id="v_notes"><c:out value="${member.notes}" /></dd>
        </div>
      </dl>

      <!-- 편집 모드 (multipart로 아바타까지 함께 전송) -->
      <form id="profileForm"
            class="edit-grid"
            method="post"
            enctype="multipart/form-data"
            action="${contextPath}/eum/updateProfile.do"
            style="display:none">
        <c:if test="${not empty _csrf}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </c:if>
        <input type="hidden" name="id" value="<c:out value='${member.id}'/>"/>

        <div class="field">
          <label for="name">이름</label>
          <input class="input" id="name" name="name" value="<c:out value='${member.name}'/>" required />
        </div>
        <div class="field">
          <label for="email">이메일</label>
          <input class="input" id="email" name="email" type="email" value="<c:out value='${member.email}'/>" required />
        </div>
        <div class="field">
          <label for="phone">전화번호</label>
          <input class="input" id="phone" name="phone" placeholder="010-0000-0000"
                 value="<c:out value='${member.phone}'/>" pattern="0\\d{1,2}-\\d{3,4}-\\d{4}" />
          <small class="help">예: 010-1234-5678</small>
        </div>
        <div class="field">
          <label for="birth">생년월일</label>
          <input class="input" id="birth" name="birth" type="date"
                 value="<fmt:formatDate value='${member.birth}' pattern='yyyy-MM-dd'/>" />
        </div>
        <div class="field" style="grid-column:1/-1">
          <label for="address">주소</label>
          <input class="input" id="address" name="address" value="<c:out value='${member.address}'/>" />
        </div>
        <div class="field" style="grid-column:1/-1">
          <label for="notes">특이사항</label>
          <textarea id="notes" name="notes"><c:out value="${member.notes}" /></textarea>
        </div>

        <!-- 아바타 업로드 -->
        <input type="file" id="avatarInput" name="avatar" accept="image/png, image/jpeg" style="display:none" />
      </form>
    </article>

    <!-- 보안 설정 -->
    <article class="card sec" aria-labelledby="securityTitle">
      <div class="sec-head">
        <i class="fa-solid fa-shield-halved" aria-hidden="true"></i>
        <div>
          <h2 class="card-title" id="securityTitle">보안 설정</h2>
          <small class="muted">비밀번호 및 로그인 보안을 관리하세요.</small>
        </div>
      </div>

      <div class="sec-body">
        <!-- 비밀번호 변경 -->
        <section class="section" aria-labelledby="pwTitle">
          <h3 id="pwTitle"><i class="fa-regular fa-key" aria-hidden="true"></i> 비밀번호 변경</h3>
          <form id="pwForm" class="grid-2" method="post" action="${contextPath}/member/changePassword.do" novalidate>
            <c:if test="${not empty _csrf}">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </c:if>
            <input type="hidden" name="id" value="<c:out value='${member.id}'/>"/>

            <div class="field eye">
              <label for="curPw">현재 비밀번호</label>
              <input class="input" id="curPw" name="currentPassword" type="password" autocomplete="current-password" required />
              <button type="button" class="toggle-eye" data-target="curPw" aria-label="비밀번호 표시 전환"><i class="fa-regular fa-eye"></i></button>
            </div>

            <div class="field eye">
              <label for="newPw">새 비밀번호</label>
              <input class="input" id="newPw" name="newPassword" type="password" minlength="8" autocomplete="new-password" required />
              <button type="button" class="toggle-eye" data-target="newPw" aria-label="비밀번호 표시 전환"><i class="fa-regular fa-eye"></i></button>
            </div>

            <div class="field eye">
              <label for="newPw2">새 비밀번호 확인</label>
              <input class="input" id="newPw2" name="newPasswordConfirm" type="password" minlength="8" autocomplete="new-password" required />
              <button type="button" class="toggle-eye" data-target="newPw2" aria-label="비밀번호 표시 전환"><i class="fa-regular fa-eye"></i></button>
              <small id="pwErr" class="error" style="display:none">비밀번호가 일치하지 않습니다.</small>
            </div>

            <div class="field full">
              <div class="meter" aria-label="비밀번호 강도">
                <div class="bar" id="pwBar"></div>
              </div>
              <small class="level" id="pwLevel">강도: -</small>
              <ul class="reqs" id="pwReqs">
                <li id="r1">8자 이상(필수)</li>
                <li id="r2">영문 대/소문자 포함</li>
                <li id="r3">숫자 포함</li>
                <li id="r4">특수문자 포함</li>
              </ul>
            </div>

            <div class="form-actions full">
              <button type="submit" class="btn primary" id="pwSave"><i class="fa-regular fa-floppy-disk"></i> 비밀번호 변경</button>
            </div>
          </form>
        </section>

        <!-- 2단계 인증 -->
        <section class="section" aria-labelledby="mfaTitle">
          <h3 id="mfaTitle"><i class="fa-regular fa-mobile" aria-hidden="true"></i> 2단계 인증(OTP)</h3>
          <div class="row">
            <div>
              <strong>로그인 시 일회용 코드를 추가로 입력합니다.</strong><br/>
              <small class="help">권장: Google Authenticator, Authy 등</small>
            </div>
            <label class="switch" aria-label="2단계 인증 사용">
              <input type="checkbox" id="mfaToggle" <c:if test="${member.mfaEnabled}">checked</c:if> />
              <span class="slider"></span>
            </label>
          </div>
          style="<c:out value='${member.mfaEnabled ? "display:none" : ""}'/>"
            아래 QR을 인증앱으로 스캔한 뒤 생성되는 6자리 코드를 입력해 완료하세요.
          </div>
          <div id="qrBox" class="qr" style="display:none">QR 코드 자리</div>
        </section>
      </div>
    </article>

    <!-- 봉사포인트 -->
    <article class="card" aria-labelledby="pointsTitle" id="pointsCard">
      <div class="card-head">
        <h2 class="card-title" id="pointsTitle">봉사포인트</h2>
        <div class="card-actions">
          <button class="btn ghost" type="button" id="sendPoint"><i class="fa-solid fa-paper-plane"></i> 포인트 보내기</button>
          <button class="btn primary" type="button" id="redeemPoint"><i class="fa-regular fa-gift"></i> 보상 교환</button>
        </div>
      </div>

      <!-- 요약 -->
      <div class="kpis" role="group" aria-label="포인트 요약">
        <div class="kpi">
          <strong>보유 포인트</strong>
          <div class="num" id="pt_current"><fmt:formatNumber value='${pointSummary.current}' pattern='#,###'/> P</div>
        </div>
        <div class="kpi">
          <strong>이번 달 적립 / 사용</strong>
          <div class="num">
            <span id="pt_month_gain">+<fmt:formatNumber value='${pointSummary.monthGain}' pattern='#,###'/></span>
            /
            <span id="pt_month_spend">-<fmt:formatNumber value='${pointSummary.monthSpend}' pattern='#,###'/></span> P
          </div>
        </div>
      </div>

      <!-- 등급 -->
      <div class="tier" aria-live="polite">
        <div>
          <div class="name">등급: <span id="pt_tier"><c:out value="${pointSummary.tier}" default="나무"/></span></div>
          <div class="next"><span id="pt_next_text">다음 등급까지 <fmt:formatNumber value='${pointSummary.nextNeeded}' pattern='#,###'/> P</span></div>
        </div>
        <div class="tier-bar">
          <div class="progress" aria-label="등급 진행도">
            <div class="bar" id="pt_progress" style="width:0%"></div>
          </div>
        </div>
      </div>

      <!-- 탭 -->
      <div class="tabs" role="tablist" aria-label="봉사포인트 탭">
        <button class="tab-btn" role="tab" aria-selected="true" data-tab="overview">개요</button>
        <button class="tab-btn" role="tab" aria-selected="false" data-tab="history">적립/사용 내역</button>
        <button class="tab-btn" role="tab" aria-selected="false" data-tab="rules">포인트 정책</button>
      </div>

      <!-- 패널: 개요 -->
      <div class="tab-panel active" id="panel-overview" role="tabpanel">
        <div class="kpis three">
          <div class="kpi"><strong>누적 적립</strong><div class="num" id="pt_total_gain"><fmt:formatNumber value='${pointSummary.totalGain}' pattern='#,###'/> P</div></div>
        <div class="kpi"><strong>누적 사용</strong><div class="num" id="pt_total_spend"><fmt:formatNumber value='${pointSummary.totalSpend}' pattern='#,###'/> P</div></div>
          <div class="kpi"><strong>누적 봉사시간</strong><div class="num" id="pt_total_hours"><fmt:formatNumber value='${pointSummary.totalHours}' pattern='#,##0.##'/> h</div></div>
        </div>

        <div class="goal">
          <div><strong>이달의 목표</strong> — 봉사 <span id="goalTarget"><c:out value='${pointSummary.goalTarget}' default='5'/></span>건 / 현재 <span id="goalNow"><c:out value='${pointSummary.goalNow}' default='0'/></span>건</div>
          <div class="progress" aria-label="이달 목표 진행도">
            <div class="bar" id="pt_goal_bar" style="width:0%"></div>
          </div>
          <small class="help">목표 달성 시 보너스 +500 P</small>
        </div>
      </div>

      <!-- 패널: 내역 -->
      <div class="tab-panel" id="panel-history" role="tabpanel">
        <div class="field" style="max-width:260px">
          <label for="typeSelect">유형 필터</label>
          <select id="typeSelect" class="input">
            <option value="all" selected>전체</option>
            <option value="earn">적립</option>
            <option value="spend">사용</option>
            <option value="donate">기부</option>
            <option value="transfer">송금/수신</option>
          </select>
        </div>

        <table class="table" aria-label="포인트 내역">
          <thead>
          <tr>
            <th>일시</th>
            <th>사유</th>
            <th>변동</th>
            <th>유형</th>
            <th>상태</th>
          </tr>
          </thead>
          <tbody id="pt_rows">
          <c:forEach items="${pointHistory}" var="h">
            <tr data-type="${h.type}">
              <td><fmt:formatDate value="${h.ts}" pattern="yyyy-MM-dd HH:mm"/></td>
              <td><c:out value="${h.reason}"/></td>
              <td>
                <c:choose>
                  <c:when test="${h.type == 'earn'}">+<fmt:formatNumber value='${h.amount}' pattern='#,###'/> P</c:when>
                  <c:otherwise>-<fmt:formatNumber value='${h.amount}' pattern='#,###'/> P</c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${h.type=='earn'}">적립</c:when>
                  <c:when test="${h.type=='spend'}">사용</c:when>
                  <c:when test="${h.type=='donate'}">기부</c:when>
                  <c:otherwise>송금</c:otherwise>
                </c:choose>
              </td>
              <td><span class="badge success">완료</span></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

      <!-- 패널: 정책 -->
      <div class="tab-panel" id="panel-rules" role="tabpanel">
        <ul class="rules">
          <li><b>적립:</b> 봉사 1건 기본 <b>+500 P</b>, 시간/난이도/거리 등에 따라 가중.</li>
          <li><b>보너스:</b> 피드백 “매우 만족” 시 <b>+10%</b>, 이달 목표 달성 시 <b>+500 P</b>.</li>
          <li><b>차감:</b> 사전 통보 없는 취소/노쇼 <b>-300 P</b>.</li>
          <li><b>유효기간:</b> 적립 후 <b>24개월</b> (최근 사용/적립 시 갱신).</li>
          <li><b>교환/기부:</b> 파트너 보상으로 교환, 공용 나눔풀 기부 가능.</li>
          <li><b>현금화 불가:</b> 포인트는 현금이 아니며 환전 불가.</li>
        </ul>
      </div>
    </article>
  </div>
</section>

<!-- 내정보 전용 JS -->
<script defer src="<c:url value='/resources/js/mypage.js'/>"></script>