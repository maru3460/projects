package jp.co.maru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.maru.component.UserSession;

@Controller
@RequestMapping("/home/tic-tac-toe")
public class OXController {
	
	private UserSession userSession;
	
	public OXController(UserSession userSession) {
		this.userSession = userSession;
	}
	
	@RequestMapping(value="", method = RequestMethod.GET) 
	public String index(Model m) {
		m.addAttribute("userSession", userSession);
		
		return "tic-tac-toe";
	}
	
}
