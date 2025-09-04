<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- 로그인 페이지처럼, 이 화면 전용 CSS만 링크 -->
<link rel="stylesheet" href="<c:url value='/resources/css/chat.css'/>" />

<!-- 로그인처럼 '화면 하나' 단위 섹션 -->
<section id="chatView"
         class="chat-view"
         role="region"
         aria-label="채팅">

  <!-- 이 화면 전용 래퍼 (#chatApp) + 데이터 전달 -->
  <div id="chatApp"
       class="chat-page"
       data-base="${contextPath}"
       data-room="${empty param.room ? 'global' : param.room}">

    <!-- 전역 .main 과 충돌 피하려고 .chat-body 사용 -->
    <div class="chat-body">

      <!-- 데스크톱: 좌(채팅)/우(지도) 분할 -->
      <section class="desktop-split" id="desktopSplit" aria-label="데스크톱 채팅/지도">
        <!-- 채팅 -->
        <article class="panel" id="chatPanelDesk" aria-label="채팅창">
          <header>💬 짝꿍 이음이와 대화</header>
          <div class="body">
            <div id="chatBoxDesk">
              <div class="msg other">안녕하세요! 무엇을 도와드릴까요?</div>
              <div class="msg me">약국 가는 길을 알려주실 수 있나요?</div>
            </div>
            <div class="chat-input">
              <input type="text" id="msgInputDesk" placeholder="메시지를 입력하세요..." />
              <button id="sendBtnDesk" type="button">전송</button>
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
                <button id="zoomInDesk"  type="button" aria-label="확대">+</button>
                <button id="zoomOutDesk" type="button" aria-label="축소">−</button>
                <button id="recenterDesk" type="button" aria-label="재정렬">◎</button>
              </div>
            </div>
          </div>
        </article>
      </section>

      <!-- 모바일: 전면 지도 + 바텀시트(채팅) -->
      <section class="mobile-map" id="mobileMap" aria-label="모바일 지도">
        <div class="map-bg" id="mapBgMobile"></div>
        <div class="zoom-controls">
          <button id="zoomInMob"  type="button" aria-label="확대">+</button>
          <button id="zoomOutMob" type="button" aria-label="축소">−</button>
          <button id="recenterMob" type="button" aria-label="재정렬">◎</button>
        </div>

        <div class="sheet" id="chatSheet" aria-label="채팅 시트" aria-expanded="false">
          <div class="handle" id="sheetHandle" aria-label="드래그하여 시트 높이 조절"></div>
          <header>💬 짝꿍 이음이와 대화</header>

          <!-- 메시지 스크롤 영역 -->
          <div class="content" id="chatContentMob">
            <div class="chat-box" id="chatBoxMob">
              <div class="msg other">안녕하세요! 무엇을 도와드릴까요?</div>
              <div class="msg me">약국 가는 길을 알려주실 수 있나요?</div>
            </div>
          </div>

          <!-- 입력창 -->
          <div class="sheet-footer">
            <input type="text" id="msgInputMob" placeholder="메시지를 입력하세요..." />
            <button id="sendBtnMob" type="button">전송</button>
          </div>
        </div>
      </section>

    </div><!-- /.chat-body -->
  </div><!-- /#chatApp -->
</section>

<!-- 이 화면 전용 JS만 defer 로드 -->
<script defer src="<c:url value='/resources/js/chat.js'/>"></script>