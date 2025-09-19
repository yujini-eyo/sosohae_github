<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="<c:url value='/resources/css/chat_list.css'/>"/>
<link rel="icon" href="<c:url value='/resources/favicon.ico'/>">

<section id="roomListApp"
         class="chat-list-page"
         data-base="${ctx}"
         data-sender="${empty sessionScope.member.id ? (empty param.sender ? 'guest' : param.sender) : sessionScope.member.id}">
  <header class="list-head">채팅</header>
  <ul id="roomList" class="room-list"></ul>
</section>
<script>
  window.CHAT_LIST_BOOT = {
    listUrl: '/api/chat/my-rooms',
    roomUrl: '/chat/room.do',
    pageSize: 20
  };
</script>
<script defer src="<c:url value='/resources/js/chat_list.js'/>"></script>