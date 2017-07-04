package ihamfp.IhTech.common;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static List<Integer> intAtoList(int[] input) {
		List<Integer> intList = new ArrayList<Integer>();
		for (int i=0;i<input.length;i++) {
			intList.add(input[i]);
		}
		return intList;
	}
}
