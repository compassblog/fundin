package com.fundin.utils.vm;

import org.apache.commons.lang3.StringUtils;

public final class VmStringTool {

	public static boolean isEqual(String str1, String str2) {
		if (str1 == str2) {
			return true;
		}
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}
	
	public static String cutString(String str) {
		if (StringUtils.isBlank(str) || str.length() < 10) {
			return str;
		}
		return str.substring(0, 10) + "...";
	}
	
}
