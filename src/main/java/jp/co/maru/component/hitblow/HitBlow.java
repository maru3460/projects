package jp.co.maru.component.hitblow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import jp.co.maru.exception.NotAppropriateInputException;
import jp.co.maru.utils.ArrayUtil;

/*
 * 三桁のHitBlowのゲーム進行用クラス。
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class HitBlow implements Serializable{
	private WebApplicationContext webApplicationContext;
	private ManagePoses managePoses;
	private final int[] NUMS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};	
	
	//まだHitまたはBlowの出ていない桁の数
	private int initNum;
	private ArrayList<Integer> initNumHistory = new ArrayList<>();
	
	//まだ聞いていない数字のリスト
	private ArrayList<Integer> initNums;
	private ArrayList<ArrayList<Integer>> initNumsHistory = new ArrayList<>();
	private StringBuilder sb;
	
	public HitBlow(ManagePoses managePoses, WebApplicationContext webApplicationContext) {
		this.managePoses = managePoses;
		this.webApplicationContext = webApplicationContext;
		
		initNum = 3;
		initNums = new ArrayList<>();
		for(int i: NUMS) {
	       	initNums.add(i);
		}
	}
	
	/*------------------------------ PUBLIC ------------------------------*/
	
	/*
	 * 次に入力する数字を返す関数
	 */
	public int getInputNumber() throws NotAppropriateInputException{
		int rtn = 0;
		try {
			rtn = generateNumber(managePoses);
		}catch(NotAppropriateInputException e) {
			System.out.println("セッションID：" + getSessionId());
        	System.out.println("getInputNumberでエラーが発生しました。");
			throw new NotAppropriateInputException();
		}
		
		return rtn;
	}
	
	/*
	 * 入力した数字とそのHitとBlowをPosesに適用する関数
	 */
	public void applyHitBlow(int hit, int blow, int n) throws NotAppropriateInputException{
		initNum -= hit + blow;
		int x = n / 100;
		int y = (n % 100) / 10;
		int z = n % 10;
		try{
			managePoses.applyNewNums(hit, blow, x, y, z);
		}catch(NotAppropriateInputException e) {
			initNum = 0;
			initNums = new ArrayList<>();
			
			System.out.println("セッションID：" + getSessionId());
        	System.out.println("applyHitBlowでエラーが発生しました。");
        	
			throw new NotAppropriateInputException();
		}
	}
	
	/*
	 * 3Hitの時に呼び出す関数
	 */
	public void clearGame() {
		initNum = 0;
		initNums.clear();
		managePoses.clearGame();
	}
	
	/*
	 * 歴史を記録する
	 */
	public void makeHistory() {
		initNumHistory.add(initNum);
		initNumsHistory.add(ArrayUtil.deepCopyAryToAry(initNums));
		managePoses.makePosesHistory();
	}
	
	/*
	 * 歴史をさかのぼれるかどうか判定する関数
	 */
	public boolean canBackHistory() {
		return managePoses.canBackHistory() && (initNumHistory.size() > 0) && (initNumsHistory.size() > 0);
	}
	
	/*
	 * 歴史を一つ戻す関数
	 * 
	 * .canBackHistoryを呼び出してtrueだったときのみ使用してください
	 */
	public void backHistory() {
		initNum = initNumHistory.get(initNumHistory.size() - 1);
		initNumHistory.remove(initNumHistory.size() - 1);
		initNums = initNumsHistory.get(initNumsHistory.size() - 1);
		initNumsHistory.remove(initNumsHistory.size() -1);
		managePoses.backHistory();
	}
	
	/*
	 * リセット
	 */
	public void reset() {
		initNum = 3;
		initNumHistory.clear();
		initNums.clear();
		for(int i: NUMS) {
			initNums.add(i);
		}
		initNumsHistory.clear();
		managePoses.reset();
	}
	
	/*
	 * デバッグ用
	 */
	@Override
	public String toString() {
		sb = new StringBuilder();
		sb.append("クラス：HitBlow" + "\r\n");
		sb.append("initNum：" + initNum + "\r\n");
		sb.append("initNumHistory：" + "\r\n");
		for(int i: initNumHistory) {
			sb.append(i + " ");
		}
		sb.append("\r\n" + "initNums：" + "\r\n");
		for(int i: initNums) {
			sb.append(i + " ");
		}
		sb.append("\r\n" + "initNumsHistroy：" + "\r\n");
		for(ArrayList<Integer> list: initNumsHistory) {
			for(int i: list) {
				sb.append(i + " ");
			}
			sb.append("\r\n");
		}
		sb.append("\r\n--------------------\r\n");
		sb.append(managePoses.toString());
		return sb.toString();
	}
	
	/*------------------------------ PRIVATE ------------------------------*/
	
	/*
	 * 次に入力する数字を決める関数
	 */
	private int generateNumber(ManagePoses managePoses) throws NotAppropriateInputException{
		int[] rtn = new int[3];
		int x;
		int y;
		int z;
		
		Random rand = new Random();
		ArrayList<Integer> tmpNumSet;
		if(initNum > 0 && initNums.size() > 3) {
			x = rand.nextInt(initNums.size());
			rtn[0] = initNums.get(x);
			initNums.remove(x);
			y = rand.nextInt(initNums.size());
			rtn[1] = initNums.get(y);
			initNums.remove(y);
			z = rand.nextInt(initNums.size());
			rtn[2] = initNums.get(z);
			initNums.remove(z);
			
		}else if(initNum <= 1) {
			if(initNums.size() == 1) {
				if(initNum == 1) {
					managePoses.oneDecided(initNums.get(0));
					initNum -= 1;
				}else if(initNum == 0) {
					managePoses.oneRemove(initNums.get(0));
					initNum -= 1;
				}else {
					System.out.println("セッションID：" + getSessionId());
		        	System.out.println("generateNumberでエラーが発生しました。(initNums.size() == 1 && initNum != 0, 1)");
					throw new NotAppropriateInputException();
				}
			}
			initNums.clear();
			
			tmpNumSet = managePoses.getRandomPos();
			rtn[0] = tmpNumSet.get(0);
			rtn[1] = tmpNumSet.get(1);
			rtn[2] = tmpNumSet.get(2);
			
		}else {
			System.out.println("セッションID：" + getSessionId());
        	System.out.println("generateNumberでエラーが発生しました。");
			throw new NotAppropriateInputException();
		}

		return rtn[0] * 100 + rtn[1] * 10 + rtn[2];
	}
	
	private String getSessionId() {
        return webApplicationContext.getId();
    }
}
