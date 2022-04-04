package Utility;

import java.util.List;

import Elements.Result;

public class Convert {
	public static  Result[] convertToArray(List<Result> l) {
		Result[] array=new Result[l.size()];
		for(int i=0;i<l.size();i++) {
			array[i]=l.get(i);
		}
		return array;
	}
}
