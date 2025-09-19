package com.myspring.eum.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ChatController {

    // /chat 과 /chat/room 둘 다 같은 JSP로 보냄
    @RequestMapping(value = {"/chat", "/chat/room"}, method = RequestMethod.GET)
    public String chat() {
        return "chat/chat"; // 앞에 슬래시 없이! (JSP 뷰리졸버가 prefix/suffix 붙임)
    }
    @RequestMapping("/chat/list")
    public String chatList() {
        return "chat/list"; // Tiles 정의 이름
    }
}
