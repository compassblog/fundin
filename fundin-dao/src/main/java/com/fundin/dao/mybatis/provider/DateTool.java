package com.fundin.dao.mybatis.provider;

import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public final class DateTool {

	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final FastDateFormat fdf = FastDateFormat.getInstance(DATEFORMAT);
	
	public static String dateFormat(final Date date) {
		String str = "";
		try {
			str = fdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
