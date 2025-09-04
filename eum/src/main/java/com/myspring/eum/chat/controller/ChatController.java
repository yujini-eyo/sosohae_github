package com.myspring.eum.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ChatController {

    @RequestMapping(value="/chat", method=RequestMethod.GET)
    public String chat() {
        return "/chat/chat"; // Tiles 이름이면 그대로, JSP 경로면 ViewResolver 설정에 맞춰주세요
    }
}