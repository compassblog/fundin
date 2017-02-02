package com.fundin.service.common;

import java.util.Date;

import com.fundin.utils.common.DateUtil;

public class DateConverter {

	public static String convertDate4Past(final Date startDate, final Date nowDate){
		int days = DateUtil.calcDays(nowDate, startDate);
		if (days > 3) {
			return DateUtil.dateFormat(startDate, DateUtil.DATEFORMAT_UI);
		}
		if (days <= 3 && days > 0) {
			return days + "天前";
		}
		
		int	hours = DateUtil.calcHours(nowDate, startDate);
		if (hours > 0) {
			return hours + "小时前";
		}
		
		int minutes = DateUtil.calcMinutes(nowDate, startDate);
		if (minutes > 0) {
			return minutes + "分钟前";
		} else {
			return "刚刚";
		}
	}
	
	public static String convertDate4Future(final Date endDate, final Date nowDate){
		int days = DateUtil.calcDays(endDate, nowDate);
		if (days > 0) {
			return days + "天";
		}
		
		int	hours = DateUtil.calcHours(endDate, nowDate);
		if (hours > 0) {
			return hours + "小时";
		} else {
			return "1小时";
		}
	}
	
}
