package com.fundin.utils.common;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	
	public static final String Comma_Seperator = ",";
	public static final String Random_String_Base = "abcdefghijklmnopqrstuvwxyz0123456789";
	public static final String Random_Code_Base = "0123456789";
	
	public static List<String> splitStr2List(String str, String seperator) {
		return Arrays.asList(splitStr2Arr(str, seperator));
	}
	
	public static String[] splitStr2Arr(String str, String seperator) {
		return StringUtils.split(str, seperator);
	}
	
	public static String generateRandomString(int randomLength) {
	    Random random = new Random();
	    StringBuilder sb = new StringBuilder();
	    
	    for (int i = 0; i < randomLength; i++) {
	        int pos = random.nextInt(Random_String_Base.length());
	        sb.append(Random_String_Base.charAt(pos));
	    }
	    return sb.toString();
	}
	
	public static String generateRandomCode(int randomLength) {
	    Random random = new Random();
	    StringBuilder sb = new StringBuilder();
	    
	    for (int i = 0; i < randomLength; i++) {
	        int pos = random.nextInt(Random_Code_Base.length());
	        sb.append(Random_Code_Base.charAt(pos));
	    }
	    return sb.toString();
	}
	
}
