<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- 컨텍스트/CSRF 메타 (points.js가 자동 인식) -->
<meta name="ctx" content="${contextPath}" />
<c:if test="${not empty _csrf}">
  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
</c:if>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />

<!-- 기본 스타일(재사용) 후, 포인트 전용 CSS로 오버라이드 -->
<link rel="stylesheet" href="<c:url value='/resources/css/mypage.css'/>?v=20250912a" />
<link rel="stylesheet" href="<c:url value='/resources/css/points.css'/>?v=20250912a" />

<section id="pointsView" class="mypage-view" role="region" aria-labelledby="pointsTitle">
  <div class="content-wrap">
    <article class="card" aria-labelledby="pointsTitle" id="pointsCard">
      <div class="card-head">
        <h2 class="card-title" id="pointsTitle">봉사포인트</h2>
        <div class="card-actions">
          <button class="btn ghost" type="button" id="sendPoint">
            <i class="fa-solid fa-paper-plane" aria-hidden="true"></i> 포인트 보내기
          </button>
          <button class="btn primary" type="button" id="redeemPoint">
            <i class="fa-solid fa-gift" aria-hidden="true"></i> 보상 교환
          </button>
        </div>
      </div>

      <!-- 요약 -->
      <div class="kpis" role="group" aria-label="포인트 요약">
        <div class="kpi">
          <strong>보유 포인트</strong>
          <div class="num" id="pt_current">
            <fmt:formatNumber value='${pointSummary.current}' pattern='#,###'/> P
          </div>
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
          <div class="name">등급:
            <span id="pt_tier"><c:out value="${pointSummary.tier}" default="나무"/></span>
          </div>
          <div class="next">
            <span id="pt_next_text">다음 등급까지
              <fmt:formatNumber value='${pointSummary.nextNeeded}' pattern='#,###'/> P
            </span>
          </div>
        </div>
        <div class="tier-bar">
          <div class="progress" aria-label="등급 진행도">
            <div class="bar" id="pt_progress" style="width:0%"></div>
          </div>
        </div>
      </div>

      <!-- 탭 -->
      <div class="tabs" role="tablist" aria-label="봉사포인트 탭">
        <button class="tab-btn" role="tab" aria-selected="true"  data-tab="overview">개요</button>
        <button class="tab-btn" role="tab" aria-selected="false" data-tab="history">적립/사용 내역</button>
        <button class="tab-btn" role="tab" aria-selected="false" data-tab="rules">포인트 정책</button>
      </div>

      <!-- 패널: 개요 -->
      <div class="tab-panel active" id="panel-overview" role="tabpanel">
        <div class="kpis three">
          <div class="kpi">
            <strong>누적 적립</strong>
            <div class="num" id="pt_total_gain">
              <fmt:formatNumber value='${pointSummary.totalGain}' pattern='#,###'/> P
            </div>
          </div>
          <div class="kpi">
            <strong>누적 사용</strong>
            <div class="num" id="pt_total_spend">
              <fmt:formatNumber value='${pointSummary.totalSpend}' pattern='#,###'/> P
            </div>
          </div>
          <div class="kpi">
            <strong>누적 봉사시간</strong>
            <div class="num" id="pt_total_hours">
              <fmt:formatNumber value='${pointSummary.totalHours}' pattern='#,##0.##'/> h
            </div>
          </div>
        </div>

        <div class="goal">
          <div>
            <strong>이달의 목표</strong> — 봉사
            <span id="goalTarget"><c:out value='${pointSummary.goalTarget}' default='5'/></span>건 /
            현재 <span id="goalNow"><c:out value='${pointSummary.goalNow}' default='0'/></span>건
          </div>
          <div class="progress" aria-label="이달 목표 진행도">
            <div class="bar" id="pt_goal_bar" style="width:0%"></div>
          </div>
          <small class="help">목표 달성 시 보너스 +500 P</small>
        </div>
      </div>

      <!-- 패널: 내역 -->
      <div class="tab-panel" id="panel-history" role="tabpanel" aria-busy="false">
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
            <c:choose>
              <c:when test="${empty pointHistory}">
                <tr data-type="all">
                  <td colspan="5" style="color:#9a8779">내역이 아직 없어요.</td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach items="${pointHistory}" var="h">
                  <tr data-type="${h.type}">
                    <td><fmt:formatDate value="${h.ts}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td><c:out value="${h.reason}"/></td>
                    <td>
                      <c:choose>
                        <c:when test="${h.type == 'earn'}">
                          +<fmt:formatNumber value='${h.amount}' pattern='#,###'/> P
                        </c:when>
                        <c:otherwise>
                          -<fmt:formatNumber value='${h.amount}' pattern='#,###'/> P
                        </c:otherwise>
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
              </c:otherwise>
            </c:choose>
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

<!-- (선택) 최초 진입 시 서버에서 최신 요약/내역 자동 새로고침 -->
<script>window.POINTS_FETCH_ON_LOAD = true;</script>

<!-- 포인트 전용 JS -->
<script defer src="<c:url value='/resources/js/points.js'/>?v=20250912a"></script>
