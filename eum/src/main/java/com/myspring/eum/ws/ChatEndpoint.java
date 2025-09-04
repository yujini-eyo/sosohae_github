package com.myspring.eum.ws;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ServerEndpoint(value = "/ws/chat/{roomId}")
public class ChatEndpoint {

    private static final ConcurrentMap<String, Set<Session>> ROOMS =
            new ConcurrentHashMap<String, Set<Session>>();

    private static Set<Session> getRoom(String roomId) {
        Set<Session> set = ROOMS.get(roomId);
        if (set == null) {
            Set<Session> newSet = Collections.newSetFromMap(
                    new ConcurrentHashMap<Session, Boolean>());
            Set<Session> prev = ROOMS.putIfAbsent(roomId, newSet);
            set = (prev == null) ? newSet : prev;
        }
        return set;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) throws IOException {
        // ★ Tomcat이 HttpSession을 userProperties에 넣어줌
        javax.servlet.http.HttpSession httpSession =
                (javax.servlet.http.HttpSession) session.getUserProperties()
                        .get(javax.servlet.http.HttpSession.class.getName());

        // ★ 로그인 세션 키는 프로젝트에 맞게 바꾸세요 (예: "loginMember", "memberVO", "userId" 등)
        if (httpSession == null || httpSession.getAttribute("loginMember") == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "LOGIN_REQUIRED"));
            return;
        }

        getRoom(roomId).add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") String roomId) {
        Set<Session> peers = ROOMS.get(roomId);
        if (peers == null) return;

        // 자신에게는 재전송하지 않아 중복 버블 방지
        for (Session s : peers) {
            if (s.isOpen() && s != session) {
                s.getAsyncRemote().sendText(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId) {
        Set<Session> peers = ROOMS.get(roomId);
        if (peers != null) peers.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        thr.printStackTrace();
    }
}