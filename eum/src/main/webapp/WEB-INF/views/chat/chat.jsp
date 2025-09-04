<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- layout이 공통 head/foot를 넣는 구조라면, 여기선 '본문'만 렌더 -->
<!-- 로그인처럼 resources 경로를 사용 -->
<link rel="stylesheet" href="<c:url value='/resources/css/chat.css'/>"/>

<!-- 최상위 래퍼에 data-base로 contextPath 전달 -->
<div id="chatApp" class="chat-page" data-base="${contextPath}">
  <!-- 메인 -->
  <main class="main">
    <!-- 데스크톱: 좌우 분할 -->
    <section class="desktop-split" id="desktopSplit">
      <!-- 채팅 패널 -->
      <article class="panel" id="chatPanelDesk" aria-label="채팅창">
        <header>💬 짝꿍 이음이와 대화</header>
        <div class="body">
          <div id="chatBoxDesk">
            <div class="msg other">안녕하세요! 무엇을 도와드릴까요?</div>
            <div class="msg me">약국 가는 길을 알려주실 수 있나요?</div>
          </div>
          <div class="chat-input">
            <input type="text" id="msgInputDesk" placeholder="메시지를 입력하세요..." />
            <button id="sendBtnDesk">전송</button>
          </div>
        </div>
      </article>

      <!-- 리사이저 -->
      <div class="resizer" id="resizer" aria-hidden="true" title="폭 조절"></div>

      <!-- 지도 -->
      <article class="panel" id="mapPanelDesk" aria-label="지도">
        <header>🗺️ 실시간 위치 공유</header>
        <div class="body" style="padding:0;">
          <div id="mapCanvasDesktop">
            <div class="map-bg" id="mapBgDesk"></div>
            <div class="zoom-controls">
              <button id="zoomInDesk">+</button>
              <button id="zoomOutDesk">−</button>
              <button id="recenterDesk">◎</button>
            </div>
          </div>
        </div>
      </article>
    </section>

    <!-- 모바일: 지도 + 바텀시트(채팅) -->
    <section class="mobile-map" id="mobileMap">
      <div class="map-bg" id="mapBgMobile"></div>
      <div class="zoom-controls">
        <button id="zoomInMob">+</button>
        <button id="zoomOutMob">−</button>
        <button id="recenterMob">◎</button>
      </div>

      <div class="sheet" id="chatSheet" aria-label="채팅 시트" aria-expanded="false">
        <div class="handle" id="sheetHandle" aria-label="드래그하여 시트 높이 조절"></div>
        <header>💬 짝꿍 이음이와 대화</header>

        <div class="content" id="chatContentMob">
          <div class="chat-box" id="chatBoxMob">
            <div class="msg other">안녕하세요! 무엇을 도와드릴까요?</div>
            <div class="msg me">약국 가는 길을 알려주실 수 있나요?</div>
          </div>
        </div>

        <div class="sheet-footer">
          <input type="text" id="msgInputMob" placeholder="메시지를 입력하세요..." />
          <button id="sendBtnMob">전송</button>
        </div>
      </div>
    </section>
  </main>
</div>

<!-- 페이지 전용 JS만 defer 로드 (header/footer는 레이아웃에서 처리) -->
<script defer src="<c:url value='/resources/js/chat.js'/>"></script>