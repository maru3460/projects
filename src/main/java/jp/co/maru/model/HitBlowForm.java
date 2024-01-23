package jp.co.maru.model;

import java.io.Serializable;

public class HitBlowForm implements Serializable {
	private int inputValue = 0;
	private int hit = 0;
	private int blow = 0;
	
	public int getInputValue() {
		return inputValue;
	}
	public void setInputValue(int inputValue) {
		this.inputValue = inputValue;
	}
	
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	
	public int getBlow() {
		return blow;
	}
	public void setBlow(int blow) {
		this.blow = blow;
	}
}
