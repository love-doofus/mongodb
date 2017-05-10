package com.wang.mongo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wxe
 * @since 1.0.0
 */
@Controller
@RequestMapping("/mongoMng")
public class TestController {
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public String main(){
		System.out.println("main !");
		return "hello world";
	}

}
