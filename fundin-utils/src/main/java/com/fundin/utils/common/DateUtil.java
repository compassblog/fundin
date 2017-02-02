package com.fundin.utils.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public final class DateUtil {

	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.S";
	public static final String DATEFORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMAT_MINUTE = "yyyy-MM-dd HH:mm";
	public static final String DATEFORMAT_HOUR = "yyyy-MM-dd HH";
	public static final String DATEFORMAT_DAY = "yyyy-MM-dd";
	public static final String DATEFORMAT_MONTH = "yyyy-MM";
	public static final String DATEFORMAT_YEAR = "yyyy";
	public static final String DATEFORMAT_TRIM = "yyyyMMddHHmmss";
	
	public static final String DATEFORMAT_UI = "MM月dd日";
	public static final String DATEFORMAT_FOR_ORDER = "yyyyMMddHHmmssSSS";

	private DateUtil() {
		throw new IllegalArgumentException("不能实例化DateUtil");
	}

	public static Date stringToDate(final String dateString, final String patten) {
		Date date = null;
		try {
			date = DateUtils.parseDate(dateString, new String[] { patten });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateFormat(final Date date, final String patten) {
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		String str = "";
		try {
			str = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String dateStringFormat(final String dateString, final String patten) {
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		DateFormat df = DateFormat.getDateInstance();
		String str = "";
		try {
			Date d = df.parse(dateString);
			str = sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String dateStringFormat(final String dateString, final String source, final String target) {
		SimpleDateFormat sdf = new SimpleDateFormat(source);
		String str = "";
		try {
			Date d = sdf.parse(dateString);
			sdf.applyPattern(target);
			str = sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getNextTime(final String newsday, final int data) {
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			date = DateUtils.parseDate(newsday, new String[] { DATEFORMAT_DAY });
			cal.setTime(date);
			cal.add(Calendar.DATE, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}

	public static String getweek() {
		String[] weekDays = new String[] { "7", "1", "2", "3", "4", "5", "6" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 获取指定时间对应的周一
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间对应的周二
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTuesday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间对应的周三
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWednesday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间对应的周四
	 * 
	 * @param date
	 * @return
	 */
	public static Date getThursday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间对应的周五
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFriday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间对应的周六
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSaturday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间对应的周日
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSunday(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATEFORMAT_DAY);
		return java.sql.Date.valueOf(df.format(c.getTime()));
	}

	/**
	 * 获取指定时间所在周的周一到周日的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getWeekFromMondayToSunday(final Date date) {
		return new Date[] { getMonday(date), getTuesday(date), getWednesday(date), getThursday(date), getFriday(date),
				getSaturday(date), getSunday(date) };
	}

	/**
	 * 获得指定时间当天的开始时间
	 * 
	 * @param date
	 *            指定时间
	 * @return 开始时间字符串
	 */
	public static String getDateBeginTime(final Date date) {
		Calendar calendar = getCalendarBegin(date);
		return DateUtil.dateFormat(calendar.getTime(), DateUtil.DATEFORMAT_SECOND);
	}

	private static Calendar getCalendarBegin(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	/**
	 * 获得指定时间当天的开始时间
	 * 
	 * @param date
	 *            指定时间
	 * @return 开始时间
	 */
	public static Date getDateBegin(final Date date) {
		Calendar calendar = getCalendarBegin(date);
		return calendar.getTime();
	}

	/**
	 * 获得指定时间当天的结束时间
	 * 
	 * @param date
	 *            指定时间
	 * @return 结束时间字符串
	 */
	public static String getDateEndTime(final Date date) {
		Calendar calendar = getCalendarEnd(date);
		return DateUtil.dateFormat(calendar.getTime(), DateUtil.DATEFORMAT_SECOND);
	}

	public static Date getDateEnd(final Date date) {
		Calendar calendar = getCalendarEnd(date);
		return calendar.getTime();
	}

	private static Calendar getCalendarEnd(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar;
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date getPastDayBegin(final Date date, int pastDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - pastDays);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date getPastDayEnd(final Date date, int pastDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - pastDays);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getNextDayBegin(final Date date, int nextDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + nextDays);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date getNextHourBegin(final Date date, int nextHours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, nextHours);
		return calendar.getTime();
	}
	
	/**
	 * @param date
	 * @param nextDays
	 * @return
	 */
	public static Date getSameTimeOfNextDays(final Date date, int nextDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + nextDays);
		return calendar.getTime();
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date getMonthBegin(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static String getDateString() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_TRIM);
		return sdf.format(date);
	}
	
	private static final int MILLISECONDS_FOR_DAY = 1000 * 60 * 60 * 24;
	private static final int MILLISECONDS_FOR_HOUR = 1000 * 60 * 60;
	private static final int MILLISECONDS_FOR_MINUTE = 1000 * 60;
	
	public static int calcDays(final Date date1, final Date date2) {
		long diff = date1.getTime() - date2.getTime();
		return (int) (diff / MILLISECONDS_FOR_DAY);
	}
	
	public static int calcHours(final Date date1, final Date date2) {
		long diff = date1.getTime() - date2.getTime();
		return (int) (diff / MILLISECONDS_FOR_HOUR);
	}
	
	public static int calcMinutes(final Date date1, final Date date2) {
		long diff = date1.getTime() - date2.getTime();
		return (int) (diff / MILLISECONDS_FOR_MINUTE);
	}
	
	public static void main(String[] args) {
		
	}
	
}
