package com.myspring.eum.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board")
public class BoardPageController {

	/** http://localhost:8090/eum/board/listArticlesAjax.do */
	@RequestMapping(value = "/listArticlesAjax.do", method = RequestMethod.GET)
	public ModelAndView listArticlesAjaxPage() {
		return new ModelAndView("board/listArticlesAjax");
		// Tiles면 tiles.xml에 name="board/listArticlesAjax" 정의 필요
	}
}
