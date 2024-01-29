package jp.co.maru.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jp.co.maru.component.ScrapingGihyo;
import jp.co.maru.component.UserSession;

@Controller
@RequestMapping("/home/py_scraping")
public class PyScrapingController {
	
	private UserSession userSession;
	
	private ScrapingGihyo scrapingGihyo;
	
	public PyScrapingController(UserSession userSession, ScrapingGihyo scrapingGihyo){
		this.userSession = userSession;
		this.scrapingGihyo = scrapingGihyo;
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Model m) throws JsonIOException, JsonSyntaxException, IOException {
		m.addAttribute("userSession", userSession);        
        
		return "scraping_gihyo";
	}	
	
	@RequestMapping(value="/fetch", method = RequestMethod.POST)
	public String fetch(Model m) {
		scrapingGihyo.delete();
		userSession.resetGihyoBooksData();
		if(!scrapingGihyo.fetchData()) {
			return "scraping_gihyo";
		};
		userSession.setGihyoBooksData(scrapingGihyo.getBooksData());
		
		m.addAttribute("userSession", userSession);  
		
		return "scraping_gihyo";
	}
}
