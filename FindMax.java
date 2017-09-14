package project1;

public class FindMax {
	
	static int findMax(int[] array) {
		int index = 0, maxValue = 0;

		for (int i = 0; i < array.length; i++){
			if(array[i] > maxValue){
				maxValue = array[i];
				index = i;
			}
		}
		return index;
	}
}