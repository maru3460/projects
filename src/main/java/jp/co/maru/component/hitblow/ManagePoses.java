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
import jp.co.maru.utils.PosUtil;

/*
 * Poses管理用クラス
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ManagePoses implements Serializable{
	private WebApplicationContext webApplicationContext;
	
	//とりうる三桁の数字のリスト
	private ArrayList<ArrayList<ArrayList<Integer>>> poses = new ArrayList<>();
	private ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> posesHistory = new ArrayList<>();
	private final int[] NUMS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private StringBuilder sb;
	
	public ManagePoses(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}
	
	/*------------------------------ PUBLIC ------------------------------*/
	
	public ArrayList<ArrayList<ArrayList<Integer>>> getPoses(){
		return ArrayUtil.deepCopy3AryTo3Ary(poses);
	}
	
	/*
	 * 次に入力する数字を決める際に使う関数
	 * 
	 * Posesの中からランダムに数字を取ってくる
	 */
	public ArrayList<Integer> getRandomPos() throws NotAppropriateInputException{
    	ArrayList<Integer> rtn = new ArrayList<>();
    	int x = 0;
    	int y = 0;
    	int z = 0;
    	if(poses.size() == 0) {
    		System.out.println("セッションID：" + getSessionId());
        	System.out.println("getRandomNumSetでエラーが発生しました。");
    		throw new NotAppropriateInputException();
    	}else {
    		Random rand = new Random();
    		int tmpPosesIndex = rand.nextInt(poses.size());
    		boolean running = true;
    		while(running) {
    			running = false;
    			ArrayList<ArrayList<Integer>> tmpPos = poses.get(tmpPosesIndex);
				ArrayList<Integer> tmpMat1 = tmpPos.get(0);
				ArrayList<Integer> tmpMat2 = tmpPos.get(1);
				ArrayList<Integer> tmpMat3 = tmpPos.get(2);
				x = tmpMat1.get(rand.nextInt(tmpMat1.size()));
				y = tmpMat2.get(rand.nextInt(tmpMat2.size()));
				if(x == y) {
					running = true;
				}
				z = tmpMat3.get(rand.nextInt(tmpMat3.size()));
				if(y == z || z == x) {
					running = true;
				}
    		}
    		rtn.add(x);
    		rtn.add(y);
    		rtn.add(z);
    		return rtn;
    	}
    }
	
	/*
	 * 入力した数字とそのHitとBlowをPosesに適用する関数
	 * 
	 * 歴史をさかのぼれるようにする際は、必ずmakePosesHistory()を呼び出した後に呼び出してください
	 */
	public void applyNewNums(int hit, int blow, int x, int y, int z) throws NotAppropriateInputException{
    	try {
    		ArrayList<ArrayList<ArrayList<Integer>>> tmpPoses = generatePoses(hit, blow, x, y, z);
    		poses = integratePoses(poses, tmpPoses);
    		
    	}catch (NotAppropriateInputException e){
    		System.out.println("セッションID：" + getSessionId());
        	System.out.println("applyNewNumsでエラーが発生しました。");
    		throw new NotAppropriateInputException();
    	}
    }
	
	/*
	 * ある数字が三桁のどこかに入ることが確定したときに使う関数
	 */
	public void oneDecided(int n) throws NotAppropriateInputException{
    	ArrayList<Integer> tmpNums = new ArrayList<>();
        for(int i: NUMS) {
        	tmpNums.add(i);
        }
        tmpNums.remove(n);
        
        ArrayList<ArrayList<ArrayList<Integer>>> tmpPoses = new ArrayList<>();
        tmpPoses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(n), null, null));
        tmpPoses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(n), null));
        tmpPoses.add(PosUtil.generatePosSet3(tmpNums, null, null, ArrayUtil.numToAry(n)));
        try {
        	poses = integratePoses(poses, tmpPoses);
        }catch (NotAppropriateInputException e){
        	System.out.println("セッションID：" + getSessionId());
        	System.out.println("oneDecidedでエラーが発生しました。");
    		throw new NotAppropriateInputException();
    	}
    }
	
	/*
	 * ある数字が三桁のどこにも入らないことがが確定したときに使う関数
	 */
	public void oneRemove(int n) throws NotAppropriateInputException{
    	ArrayList<Integer> tmpNums = new ArrayList<>();
        for(int i: NUMS) {
        	tmpNums.add(i);
        }
        tmpNums.remove(n);
        
        ArrayList<ArrayList<ArrayList<Integer>>> tmpPoses = new ArrayList<>();
        tmpPoses.add(PosUtil.generatePosSet3(tmpNums, null, null, null));
        try {
        	poses = integratePoses(poses, tmpPoses);
        }catch (NotAppropriateInputException e){
        	System.out.println("セッションID：" + getSessionId());
        	System.out.println("oneRemoveでエラーが発生しました。");
    		throw new NotAppropriateInputException();
    	}
    }
	
	/*
	 * 3Hitの時に呼び出す関数
	 * 
	 * .clear()でもそのままでも処理は変わらなかったりする
	 */
	public void clearGame() {
    	poses.clear();
    }
	
	/*
	 * 歴史を記録する
	 */
	public void makePosesHistory() {
    	posesHistory.add(ArrayUtil.deepCopy3AryTo3Ary(poses));
	}
	
	/*
	 * 歴史をさかのぼれるかどうか判定する関数
	 */
	public boolean canBackHistory() {
    	if(posesHistory.size() == 0) {
    		return false;
    	}else {
    		return true;
    	}
    }
	
	/*
	 * 歴史を一つ戻す関数
	 * 
	 * .canBackHistoryを呼び出してtrueだったときのみ使用してください
	 */
	public void backHistory() {
    	poses = posesHistory.get(posesHistory.size() - 1);
    	posesHistory.remove(posesHistory.size() - 1);
    }
	
	/*
	 * リセット
	 */
	public void reset() {
    	poses.clear();
    	posesHistory.clear();
    }
	
	/*
	 * デバッグ用
	 */
	@Override
    public String toString() {
    	sb = new StringBuilder();
    	sb.append("クラス：ManagePoses" + "\r\n");
    	sb.append("number of Pos: " + poses.size() + "\r\n");
    	sb.append("number of conbination: " + getAllPos() + "\r\n");
    	return sb.toString();
    }
	
	/*------------------------------ PRIVATE ------------------------------*/
	
	private int getAllPos() {
    	int posesNum = 0;
    	
    	for(int i = 0; i < poses.size(); i++) {
    		ArrayList<ArrayList<Integer>> tmpPos = poses.get(i);
    		ArrayList<Integer> tmpMat1 = tmpPos.get(0);
    		ArrayList<Integer> tmpMat2 = tmpPos.get(1);
    		ArrayList<Integer> tmpMat3 = tmpPos.get(2);
    		posesNum += tmpMat1.size() * tmpMat2.size() * tmpMat3.size();
    	}
    	
    	return posesNum;
    }
	
	private ArrayList<ArrayList<ArrayList<Integer>>> generatePoses(int hit, int blow, int x, int y, int z) throws NotAppropriateInputException{
    	ArrayList<ArrayList<ArrayList<Integer>>> poses = new ArrayList<>();

        ArrayList<Integer> tmpNums = new ArrayList<>();
        for(int i: NUMS) {
        	tmpNums.add(i);
        }
        ArrayList<Integer> removes = new ArrayList<>();
        removes.add(x);
        removes.add(y);
        removes.add(z);
        tmpNums = ArrayUtil.removeNums(tmpNums, removes);

        if(hit == 0 && blow == 0){
            poses.add(PosUtil.generatePosSet3(tmpNums, null, null, null));
            
        }else if(hit == 1 && blow == 0) {        	
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(x), null, null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(y), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, null, ArrayUtil.numToAry(z)));
        	
        }else if(hit == 0 && blow == 1) {        	
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.num2ToAry(y, z), null, null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.num2ToAry(x, z), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, null, ArrayUtil.num2ToAry(x, y)));
        	
        }else if(hit == 2 && blow == 0) {
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(x), ArrayUtil.numToAry(y), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(x), null, ArrayUtil.numToAry(z)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(y), ArrayUtil.numToAry(z)));
        	
        }else if(hit == 1 && blow == 1) {        	
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(x), ArrayUtil.numToAry(z), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(x), null, ArrayUtil.numToAry(y)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(y), ArrayUtil.numToAry(x)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(z), ArrayUtil.numToAry(y), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(y), null, ArrayUtil.numToAry(z)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(x), ArrayUtil.numToAry(z)));
        	
        }else if(hit == 0 && blow == 2) {
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(y), ArrayUtil.numToAry(x), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(y), null, ArrayUtil.numToAry(x)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(x), ArrayUtil.numToAry(y)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(y), ArrayUtil.numToAry(z), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(z), null, ArrayUtil.numToAry(y)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(z), ArrayUtil.numToAry(y)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(z), ArrayUtil.numToAry(x), null));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(z), null, ArrayUtil.numToAry(x)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, null, ArrayUtil.numToAry(z), ArrayUtil.numToAry(x)));
        	
        }else if(hit == 1 && blow == 2) {
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(x), ArrayUtil.numToAry(z), ArrayUtil.numToAry(y)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(z), ArrayUtil.numToAry(y), ArrayUtil.numToAry(x)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(y), ArrayUtil.numToAry(x), ArrayUtil.numToAry(z)));
        	
        }else if(hit == 0 && blow == 3) {
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(y), ArrayUtil.numToAry(z), ArrayUtil.numToAry(x)));
        	poses.add(PosUtil.generatePosSet3(tmpNums, ArrayUtil.numToAry(z), ArrayUtil.numToAry(x), ArrayUtil.numToAry(y)));
        	
        }else {
        	System.out.println("セッションID：" + getSessionId());
        	System.out.println("generatePosesでエラーが発生しました。");
        	throw new NotAppropriateInputException();
        }
        
        return poses;
    }
	
	private ArrayList<ArrayList<ArrayList<Integer>>> integratePoses(ArrayList<ArrayList<ArrayList<Integer>>> posesDefault, 
    		ArrayList<ArrayList<ArrayList<Integer>>> posesAdd) throws NotAppropriateInputException{
    	
    	ArrayList<ArrayList<ArrayList<Integer>>> rtn = new ArrayList<>();
    	
    	if(posesDefault.size() == 0) {
    		return posesAdd;
    	}else {
    		for(int i = 0; i < posesDefault.size(); i++) {
    			for(int j = 0; j < posesAdd.size(); j++) {
    				ArrayList<ArrayList<Integer>> tmpPos = new ArrayList<>();
    				tmpPos = PosUtil.posPosToPos3(posesDefault.get(i), posesAdd.get(j));
    				if(tmpPos.size() != 0) {
    					rtn.add(tmpPos);
    				}
    			}
    		}
    	}
    	if(rtn.size() == 0) {
    		System.out.println("セッションID：" + getSessionId());
        	System.out.println("generatePosesでエラーが発生しました。");
    		throw new NotAppropriateInputException();
    	}
    	
    	return rtn;
    }
	
	private String getSessionId() {
        return webApplicationContext.getId();
    }
}
