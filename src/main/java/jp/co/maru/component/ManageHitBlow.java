package jp.co.maru.component;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import jp.co.maru.exception.NotAppropriateInputException;

/*
 * HitBlowクラスを、Controllerから簡潔に扱えるようにするためのクラス
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ManageHitBlow implements Serializable{
	private HitBlow hitblow;
	private boolean isClear;
	private boolean isError;
	private int nextInputNumber;
	private ArrayList<Integer> nextInputNumberHistory = new ArrayList<>();
	private StringBuilder sb;
	
	public ManageHitBlow(HitBlow hitblow) {
		this.hitblow = hitblow;
		reset();
	}
	
	/*------------------------------ PUBLIC ------------------------------*/
	
	public int getNextInputNumber(){
		return nextInputNumber;
	}
	
	public boolean isClear() {
		return isClear;
	}
	
	public boolean isError() {
		return isError;
	}
	
	public void applyHitBlow(int hit, int blow, int n) throws NotAppropriateInputException{
		if(isClear || isError) {
			throw new RuntimeException("(isClear || isError) but controller use applyHitBlow()");
		}
		
		hitblow.makeHistory();
		nextInputNumberHistory.add(nextInputNumber);
		
		if(hit == 3 && blow == 0) {
			hitblow.clearGame();
			isClear = true;
		}else {
			try {
				hitblow.applyHitBlow(hit, blow, n);
				nextInputNumber = hitblow.getInputNumber();
			}catch(NotAppropriateInputException e) {
				isError = true;
				throw new NotAppropriateInputException();
			}	
		}
	}
	
	public boolean canBackHistory() {
		return hitblow.canBackHistory() && (nextInputNumberHistory.size() > 0);
	}
	
	public void backHistory() {
		isClear = false;
		isError = false;
		hitblow.backHistory();
		nextInputNumber = nextInputNumberHistory.get(nextInputNumberHistory.size() - 1);
		nextInputNumberHistory.remove(nextInputNumberHistory.size() - 1);
	}
	
	public void reset() {
		isClear = false;
		isError = false;
		nextInputNumber = 0;
		nextInputNumberHistory.clear();
		hitblow.reset();
		
		nextInputNumber = hitblow.getInputNumber();
	}
	
	@Override
	public String toString() {
		sb = new StringBuilder();
		sb.append("クラス：ManageHitBlow" + "\r\n");
		sb.append("isClear：" + isClear + "\r\n");
		sb.append("isError：" + isError + "\r\n");
		sb.append("nextInputNumber：" + nextInputNumber + "\r\n");
		sb.append("nextInputNumberHistory：" + "\r\n");
		for(int i: nextInputNumberHistory) {
			sb.append(i + " ");
		}
		sb.append("\r\n\r\n--------------------\r\n");
		sb.append(hitblow.toString());
		return sb.toString();
	}
	
	/*------------------------------ PRIVATE ------------------------------*/

}
