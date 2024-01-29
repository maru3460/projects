package jp.co.maru.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {	
	
	/*-------------------- HITBLOW --------------------*/
	
	private List<List<Integer>> hbList = new ArrayList<>();
	private boolean isHitBlowError = false;
	
	public List<List<Integer>> getHitBlowList(){
		return hbList;
	}
	
	public void addHitBlowNum(int inputValue, int hit, int blow) {
		List<Integer> tmp = new ArrayList<>();
		tmp.add(inputValue);
		tmp.add(hit);
		tmp.add(blow);
		hbList.add(tmp);
	}
	
	public boolean canBackHitBlowHistory() {
		return hbList.size() > 0;
	}
	
	public void backHitBlowHistory() {
		hbList.remove(hbList.size() - 1);
	}
	
	public boolean isHitBlowError() {
		return isHitBlowError;
	}
	
	public void setHitBlowError() {
		isHitBlowError = true;
	}
	
	public void resetHitBlow() {
		hbList = new ArrayList<>();
		isHitBlowError = false;
	}
	
	/*-------------------- Gihyo Scraping --------------------*/
	
	private List<List<String>> gihyoBooksData = new ArrayList<>();
	
	public boolean ifExistGihyoBooksData() {
		if(gihyoBooksData.size() > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<List<String>> getGihyoBooksData(){
		return gihyoBooksData;
	}
	
	public void setGihyoBooksData(List<List<String>> gihyoBooksData) {
		this.gihyoBooksData = gihyoBooksData;
	}
	
	public void resetGihyoBooksData() {
		gihyoBooksData.clear();
	}
	
	/*-------------------- COMMON --------------------*/
	private final String clearMessage = "クリアしました！";
	private final String errorMessage = "内部でのエラーが発生しました。";
	private StringBuilder sb;
	
	public String getClearMessage() {
		return clearMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	@Override
	public String toString() {
		sb = new StringBuilder();
		
		sb.append("----------HitBlow----------");
		sb.append("isHitBlowError: " + isHitBlowError + "\r\n");
		sb.append("hbList\r\n");
		sb.append(hbList);
		
		return sb.toString();
	}
}
