// src/main/java/com/eum/main/web/MainController.java
package com.myspring.eum.main.controller;

import com.myspring.eum.main.vo.MainVO;
import com.myspring.eum.main.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @RequestMapping("/main")
    public String main(Model model, HttpServletRequest request, HttpSession session) {

        MainVO main = new MainVO();
        main.setPageTitle("EuM:");
        main.setCurrentPath(request.getRequestURI());

        UserVO user = null;
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            user = new UserVO(loggedInUser, loggedInUser, 0);
        }

        model.addAttribute("main", main);
        model.addAttribute("user", user);

        // Tiles �젙�쓽紐� 諛섑솚(�삁: tiles.xml�뿉 �젙�쓽�맂 "home")
        return "home";
    }
}
