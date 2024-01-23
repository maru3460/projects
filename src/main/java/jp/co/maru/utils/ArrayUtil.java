package jp.co.maru.utils;

import java.util.ArrayList;

public class ArrayUtil {
	public static ArrayList<Integer> deepCopyAryToAry(ArrayList<Integer> ary){
        ArrayList<Integer> rtn = new ArrayList<>();
        for(int i = 0; i < ary.size(); i++){
            Integer aryInt = ary.get(i);
            rtn.add(aryInt.intValue());
        }
        return rtn;
    }
	
	public static ArrayList<ArrayList<Integer>> deepCopy2AryTo2Ary(ArrayList<ArrayList<Integer>> ary){
    	ArrayList<ArrayList<Integer>> rtn = new ArrayList<>();
    	ArrayList<Integer> tmp;
    	for(int i = 0; i < ary.size(); i++) {
    		tmp = new ArrayList<>();
    		for(int j = 0; j < ary.get(i).size(); j++) {
    			tmp.add(ary.get(i).get(j).intValue());
    		}
    		rtn.add(tmp);
    	}
    	return rtn;
    }
	
	public static ArrayList<ArrayList<ArrayList<Integer>>> deepCopy3AryTo3Ary(ArrayList<ArrayList<ArrayList<Integer>>> ary){
    	ArrayList<ArrayList<ArrayList<Integer>>> rtn = new ArrayList<>();
    	ArrayList<ArrayList<Integer>> tmp1;
    	ArrayList<Integer> tmp2;
    	for(int i = 0; i < ary.size(); i++) {
    		tmp1 = new ArrayList<>();
    		for(int j = 0; j < ary.get(i).size(); j++) {
    			tmp2 = new ArrayList<>();
    			for(int k = 0; k < ary.get(i).get(j).size(); k++) {
    				tmp2.add(ary.get(i).get(j).get(k).intValue());
    			}
    			tmp1.add(tmp2);
    		}
    		rtn.add(tmp1);
    	}
    	return rtn;
    }
	
	public static ArrayList<Integer> numToAry(int i){
    	ArrayList<Integer> tmp = new ArrayList<>();
    	tmp.add(i);
    	return tmp;
    }
	
	public static ArrayList<Integer> num2ToAry(int i, int j){
    	ArrayList<Integer> tmp = new ArrayList<>();
    	tmp.add(i);
    	tmp.add(j);
    	return tmp;
    }
	
	public static ArrayList<Integer> removeNums(ArrayList<Integer> numSet, ArrayList<Integer> removes){
		for(int remove: removes) {
			for(int i = 0; i < numSet.size(); i ++) {
	    		if(numSet.get(i) == remove) {
	    			numSet.remove(i);
	    			break;
	    		}
	    	}
		}
		return numSet;
	}
}
