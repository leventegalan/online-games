package hu.lev.onlinegames.service;

import org.springframework.stereotype.Service;

@Service
public class ConverterServiceImpl implements ConverterService {

	// (used for options)
	@Override
	public int[] stringToIntArray(String arrayString) {
		int[] array = null;

		arrayString = arrayString.substring(1, arrayString.length()-1);
		arrayString = arrayString.replaceAll("\\s+","");
		if(arrayString.length() != 0) {
			String[] integerStrings = arrayString.split(","); 
			array = new int[integerStrings.length]; 
			for (int i = 0; i < array.length; i++){
				array[i] = Integer.parseInt(integerStrings[i]); 
			}
		} else {
			array = new int[]{};
		}
		return array;
	}

}
