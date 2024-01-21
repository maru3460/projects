package jp.co.maru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.maru.component.UserSession;

@Controller
@RequestMapping("/home")
public class IndexController {
	
	private UserSession userSession;
	
	public IndexController(UserSession userSession) {
		this.userSession = userSession;
	}

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Model m) {
		m.addAttribute("userSession", userSession);
		
		if(userSession.isError()) {
			m.addAttribute("errorMessage", userSession.getErrorMessage());
			userSession.reset();
		}
		
		return "index";
	}
	
}
