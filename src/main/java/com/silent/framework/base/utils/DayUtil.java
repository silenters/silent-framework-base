package com.silent.framework.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author TanJin
 */
public class DayUtil {
	public static final long OFFSET_TIME = 28800000L;
	public static final long DAY_TIME = 86400000L;
	
	/**
	 * 获得time是几点,24小时制
	 * @param time
	 * @return
	 */
	public static int getHour(long time) {
		Date date = new Date(time);
		String hh = new SimpleDateFormat("HH").format(date);
		return StringUtils.parseInt(hh, 0);
	}

	/**
	 * 获得当前是几点,24小时制
	 * @return
	 */
	public static int getHour() {
		return getHour(System.currentTimeMillis());
	}

	/**
	 * 获得time的日期, yyyyMMdd
	 * @param time
	 * @return
	 */
	public static int getDay(long time) {
		Date date = new Date(time);
		String today = new SimpleDateFormat("yyyyMMdd").format(date);
		return StringUtils.parseInt(today, 0);
	}

	/**
	 * 获得今天日期, yyyyMMdd
	 * @return
	 */
	public static int getToday() {
		return getDay(System.currentTimeMillis());
	}

	/**
	 * 获得明天的日期, yyyyMMdd
	 * @return
	 */
	public static int getTomorrow() {
		return getDay(System.currentTimeMillis() + DAY_TIME);
	}

	/**
	 * 获得前一天的日期
	 * @return
	 */
	public static int getLastday() {
		return getDay(System.currentTimeMillis() - DAY_TIME);
	}
	
	/**
	 * 获得time那天开始的时间
	 * @param time
	 * @return
	 */
	public static long getDayStart(long time) {
		return ((time + OFFSET_TIME) / DAY_TIME) * DAY_TIME - OFFSET_TIME;
	}

	/**
	 * 获得今天开始的时间
	 * @return
	 */
	public static long getTodayStart() {
		return getDayStart(System.currentTimeMillis());
	}

	/**
	 * 获得day的Calendar
	 * @param day
	 * @return
	 */
	public static Calendar getCalendar(int day) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			date = format.parse(String.valueOf(day));
		} catch (ParseException e) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 月份 +1 or -1
	 * @param day
	 * @param month
	 * @return
	 */
	public static int addMonth(int day, int month) {
		Calendar calendar = getCalendar(day);
		calendar.add(Calendar.MONTH, month);
		return getDay(calendar.getTimeInMillis());
	}

	/**
	 * 日期 +1 or -1
	 * @param day
	 * @param date
	 * @return
	 */
	public static int addDate(int day, int date) {
		Calendar calendar = getCalendar(day);
		calendar.add(Calendar.DATE, date);
		return getDay(calendar.getTimeInMillis());
	}

	/**
	 * 获得现在的月份
	 * @return
	 */
	public static int getTodayMonth() {
		Date date = new Date();
		String today = new SimpleDateFormat("yyyyMM").format(date);
		return StringUtils.parseInt(today, 0);
	}
	
	/**
	 * 转换时间戳成 yyyy-MM-dd HH:mm:ss 格式
	 * @param time
	 * @return
	 */
	public static String timeToString(long time) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(time));
	}
}