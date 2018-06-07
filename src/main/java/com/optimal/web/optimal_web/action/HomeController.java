package com.optimal.web.optimal_web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping("welcome")
	@ResponseBody
	public String welcome() {
		log.info("welcome to page");
		return "this is welcomd";
	}
	
	@GetMapping("hello")
	@ResponseBody
	public String hello() {
		return "this is hello";
	}

}
