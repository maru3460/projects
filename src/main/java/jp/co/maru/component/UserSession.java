package jp.co.maru.component;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {
	/*-------------------- COMMON --------------------*/
	private boolean isError = false;
	private final String errorMessage = "内部でのエラーが発生しました。";
	private StringBuilder sb;
	
	public boolean isError() {
		return isError;
	}
	
	public void setError() {
		isError = true;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void reset() {
		isError = false;
	}
	
	@Override
	public String toString() {
		sb = new StringBuilder();
		
		sb.append("isError: " + isError + "\r\n");
		
		return sb.toString();
	}
}
