package jp.co.maru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.maru.component.UserSession;
import jp.co.maru.component.hitblow.ManageHitBlow;
import jp.co.maru.exception.NotAppropriateInputException;
import jp.co.maru.model.HitBlowForm;

@Controller
@RequestMapping("/home/hitblow_assist")
public class HitBlowController {
	
	private UserSession userSession;
	
	private ManageHitBlow manageHitBlow;
	
	public HitBlowController(UserSession userSession, ManageHitBlow manageHitBlow) {
		this.userSession = userSession;
		this.manageHitBlow = manageHitBlow;
	}
	
	@RequestMapping(value="", method = RequestMethod.GET) 
	public String index(@ModelAttribute HitBlowForm form, Model m) {
		System.out.println(m.asMap().keySet());
		System.out.println(m.asMap().values());
		try {
			//HitBlowFormの入力があったら歴史を進める
			if(form.getInputValue() != 0) {
				userSession.addHitBlowNum(form.getInputValue(), form.getHit(), form.getBlow());
				manageHitBlow.applyHitBlow(form.getHit(), form.getBlow(), form.getInputValue());
			}
			
			//manageHitBlowの状態に応じて場合分け
			if(manageHitBlow.isError()) {
				throw new NotAppropriateInputException();
				
			}else if(manageHitBlow.isClear()) {
				m.addAttribute("userSession", userSession);
				m.addAttribute("clear", userSession.getClearMessage());
				
				return "hitblow_assist";
				
			}else {
				m.addAttribute("userSession", userSession);
				m.addAttribute("inputValue", manageHitBlow.getNextInputNumber());
				
				return "hitblow_assist";
			}
			
		}catch(NotAppropriateInputException e) {
			m.addAttribute("userSession", userSession);
			
			debug();
			return "hitblow_error";
		}
	}
	
	@RequestMapping(value="/back_history", method = RequestMethod.POST)
	public String backHistory(Model m) {		
		if(userSession.canBackHitBlowHistory() && manageHitBlow.canBackHistory()) {
			userSession.backHitBlowHistory();
			manageHitBlow.backHistory();
			
			m.addAttribute("userSession", userSession);
			m.addAttribute("inputValue", manageHitBlow.getNextInputNumber());
			
			return "hitblow_assist";
		}else {
			userSession.resetHitBlow();
			manageHitBlow.reset();
			
			m.addAttribute("userSession", userSession);
			
			return "index";
		}
	}
	
	@RequestMapping(value="/reset", method = RequestMethod.POST)
	public String hitblowReset(Model m) {
		userSession.resetHitBlow();
		manageHitBlow.reset();
		
		m.addAttribute("userSession", userSession);
		m.addAttribute("inputValue", manageHitBlow.getNextInputNumber());
		
		return "hitblow_assist";
	}
	
	private void debug() {
		System.out.println(userSession);
		System.out.println(manageHitBlow);
	}
	
}
