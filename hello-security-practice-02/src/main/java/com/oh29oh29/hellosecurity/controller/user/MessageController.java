package com.oh29oh29.hellosecurity.controller.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {
	
	@GetMapping(value="/messages")
	public String mypage() throws Exception {

		return "user/messages";
	}

	@GetMapping("/api/messages")
	@ResponseBody
	public String apiMessages() {
		return "messages ok";
	}
}
