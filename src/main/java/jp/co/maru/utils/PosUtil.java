package jp.co.maru.utils;

import java.util.ArrayList;

public class PosUtil {
	
	/*-------------------- 3 num only --------------------*/
	
	public static ArrayList<ArrayList<Integer>> posPosToPos3(ArrayList<ArrayList<Integer>> pos1, ArrayList<ArrayList<Integer>> pos2){
    	ArrayList<ArrayList<Integer>> rtn = new ArrayList<>();
    	
    	ArrayList<Integer> mat1 = matrixMultiplication(pos1.get(0), pos2.get(0));
		ArrayList<Integer> mat2 = matrixMultiplication(pos1.get(1), pos2.get(1));
		ArrayList<Integer> mat3 = matrixMultiplication(pos1.get(2), pos2.get(2));
		
		if(mat1.size() == 0 || mat2.size() == 0 || mat3.size() == 0) {
			return rtn;
		}
		rtn.add(mat1);
		rtn.add(mat2);
		rtn.add(mat3);
    	
    	return rtn;
    }
	
	public static ArrayList<ArrayList<Integer>> generatePosSet3(ArrayList<Integer> numSet, ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Integer> z){
    	ArrayList<ArrayList<Integer>> pos = new ArrayList<>();
    	if(x == null) {
    		pos.add(ArrayUtil.deepCopyAryToAry(numSet));
    	}else {
    		pos.add(x);
    	}
    	if(y == null) {
    		pos.add(ArrayUtil.deepCopyAryToAry(numSet));
    	}else {
    		pos.add(y);
    	}
    	if(z == null) {
    		pos.add(ArrayUtil.deepCopyAryToAry(numSet));
    	}else {
    		pos.add(z);
    	}
    	return pos;
    }
	
	/*-------------------- common --------------------*/
	
	public static ArrayList<Integer> matrixMultiplication(ArrayList<Integer> mat1, ArrayList<Integer> mat2){
    	ArrayList<Integer> rtnMat = new ArrayList<>();
    	
    	for(int i = 0; i < mat1.size(); i++) {
    		int tmpElement = mat1.get(i);
    		if(mat2.contains(tmpElement)) {
    			rtnMat.add(tmpElement);
    		}
    	}
    	
    	return rtnMat;
    }
}
