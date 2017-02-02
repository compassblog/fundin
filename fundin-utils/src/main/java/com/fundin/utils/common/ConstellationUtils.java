package com.fundin.utils.common;

public class ConstellationUtils {

	private static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", 
		"狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
	private static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
	
	private static final int[] constellationId = { 10, 11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	/**
	 * 根据日期获取星座
	 * @param time
	 * @return
	 */
	public static String date2Constellation(int month, int day) {
		month--;
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
			if (month == -1) {
				month = 11;
			}
		}
		return constellationArr[month];
	}
	
	/**
	 * 根据日期获取星座Id
	 * @param time
	 * @return
	 */
	public static int date2ConstellationId(int month, int day) {
		month--;
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
			if (month == -1) {
				month = 11;
			}
		}
		return constellationId[month];
	}
	

}
