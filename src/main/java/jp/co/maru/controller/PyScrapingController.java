package jp.co.maru.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jp.co.maru.component.UserSession;
import jp.co.maru.component.scraping.ManageScraping;

@Controller
@RequestMapping("/home/py_scraping")
public class PyScrapingController {
	
	private UserSession userSession;
	
	private ManageScraping manageScraping;
	
	public PyScrapingController(UserSession userSession, ManageScraping manageScraping){
		this.userSession = userSession;
		this.manageScraping = manageScraping;
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Model m) throws JsonIOException, JsonSyntaxException, IOException {
		m.addAttribute("userSession", userSession);    
		
		if(manageScraping.setData()) {
			m.addAttribute("booksData", manageScraping.getData());
		}		
        
		return "scraping_gihyo";
	}
}
