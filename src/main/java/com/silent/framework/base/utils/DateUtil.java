package com.silent.framework.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * @author TanJin
 * @date 2015-4-7
 */
public class DateUtil {
	
	public static final long OFFSET_TIME = 28800000L;
	public static final long DAY_TIME = 86400000L;
	
	public static final String DATE_FORMAT_SECONDS_ONE = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_SECONDS_TWO = "MM月dd日  HH时mm分";
	public static final String DATE_FORMAT_HOUR_MINUTES = "HH:mm";
	public static final String DATE_FORMAT_SECONDS_THREE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_SECONDS_FOUR = "MM月dd日  HH:mm";
	public static final String DATE_FORMAT_SECONDS_FIVE = "yyyy-MM-dd HH:mm";

	public static final String DATE_FROMAT_YEAR_MONTH_DAY_ONE = "yyyyMMdd";
	public static final String DATE_FROMAT_YEAR_MONTH_DAY_TWO = "yyyy年MM月dd日";
	public static final String DATE_FROMAT_YEAR_MONTH_DAY_THREE = "yyyy-MM-dd";
	public static final String DATE_FROMAT_YEAR_MONTH_DAY_FOUR = "yyyy/MM/dd";
	public static final String DATE_FORMAT_YEAR_MONTH_DAY_FIVE = "yyyy.MM.dd";
	
	public static final String DATE_FORMAT_YEAR_MONTH_ONE = "yyyy年MM月";
	
	public static final String DATE_FORMAT_MONTH_DAY_ONE = "MM-dd";
	public static final String DATA_FORMAT_MONTH_DAY_TWO = "MM.dd";
	public static final String DATE_FORMAT_MONTH_DAY_THREE = "MM月dd日";
	
	/**
	 * 根据时间戳获取yyyyMMddHHmmss类型的时间信息
	 * 精确到秒
	 * @date 2015-4-7
	 */
	public static String formatTime(long date, String format) {
		Date dat = new Date(date);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);
		SimpleDateFormat formate = new SimpleDateFormat(format);
		String currentDate = formate.format(gc.getTime());
		return currentDate;
	}
	
	/**
	 * 根据时间字符串获取时间戳
	 * @date 2015-9-8
	 */
	public static long getTimeStamp(String strDate, String format){
		if(StringUtils.isEmpty(format)) {
			return 0;
		}
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		try {
			Date date = fmt.parse(strDate);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 根据时间戳获取当天开始的时间
	 * @date 2015-4-17
	 */
	public static long getDayStartTime(long time) {
		return (time + 28800000L) / 86400000L * 86400000L - 28800000L;
	}
	
	/**
	 * 获取指定时间的小时
	 * @date 2015-9-15
	 */
	public static int getHour(long time){
		Date date = new Date(time);
		String today = new SimpleDateFormat("HH").format(date);
		return StringUtils.parseInt(today, 0);
	}
	
	/**
	 * 获取截止到小时级时间戳
	 * @date 2015-4-20
	 */
	public static Date getCurrentTimeToHour(int hour){
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取 Calendar
	 * @param day  yyyy-MM-dd型字符串
	 * @date 2015-11-3
	 */
	public static Calendar getCalendar(String day){
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.parse(day);
		} catch (ParseException e) {
			date = new Date();
		}	
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar;
	}
	
	/**
	 * 获取 Calendar
	 * @param time  时间戳
	 * @date 2015-11-3
	 */
	public static Calendar getCalendar(long time){
		Date date = new Date(time);
		
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar;
	}
	
	/**
	 * 获取 Calendar
	 * @date 2015-11-3
	 */
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();  
        return calendar; 
	}
	
	/**
	 * 根据时间戳获取指定时间戳的秒数
	 * @date 2015-11-24
	 */
	public static int getSeconds(long time){
		Calendar calendar = getCalendar(time);
		return calendar.get(Calendar.SECOND);
	}
	
	/**
	 * 获取星期，星期日：1，星期一：2....
	 * @date 2015-11-23
	 */
	public static int getWeek(long time){
		Calendar calendar = getCalendar(time);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 根据时间戳获取指定时间戳所在当天是星期几
	 * @date 2015-11-24
	 */
	public static String getWeekStr(long time){
		Calendar calendar = getCalendar(time);
		int weekMark = calendar.get(Calendar.DAY_OF_WEEK);
		return getWeek(weekMark);
	}
	
	/**
	 * 根据星期标号获取指定标号对应的星期几
	 * 1 代表 星期日
	 * 2 代表星期一
	 * 3代表星期二
	 * ....
	 * 7代表星期六
	 * 
	 * 数值超过1-7，则返回null
	 * @date 2015-11-24
	 */
	public static String getWeek(int weekMark){
		switch (weekMark) {
		case 1:
			return "星期日";
		case 2:
			return "星期一";
		case 3:
			return "星期二";
		case 4:
			return "星期三";
		case 5:
			return "星期四";
		case 6:
			return "星期五";
		case 7:
			return "星期六";
		default:
			return null;
		}
	}
	
	/**
	 * 根据星期标号字符串获取星期名称字符串
	 * @date 2015-11-25
	 */
	public static String getWeeks(String weekMarks){
		String[] weekArray = weekMarks.split(",");
		StringBuilder str = new StringBuilder();
		for(String weekStr : weekArray){
			int weekMark = 0;
			try {
				weekMark = Integer.parseInt(weekStr);
			} catch (NumberFormatException e) {
				weekMark = 0;
			} catch (NullPointerException e) {
				weekMark = 0;
			}
			
			String weekName = getWeek(weekMark);
			if((weekName != null) && (!weekName.isEmpty())){
				str.append(weekName).append(",");
			}
		}
		if(str.length() > 0){
			str.deleteCharAt(str.length() - 1);
		}
		return str.toString();
	}
	
	/**
	 * 获取当前年份
	 * @date 2015-11-4
	 */
	public static int getCurrentYear(){
        return getCalendar().get(Calendar.YEAR);
	}
	
	/** 
     * @return 获得本月 
     */  
    public static int getCurrentMonth(){  
        return getCalendar().get(Calendar. MONTH)+1;  
    }
    
    /** 
     * @return 获得当前时间 
     */  
    public static Date getNow(){  
        return getCalendar().getTime();
    }
	
	/**
	 * 获取指定时间的年份
	 * @param date  yyyy-MM-dd型字符串
	 * @date 2015-11-4
	 */
	public static int getYear(String date){
		Calendar calendar = getCalendar(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 获取指定时间的年份
	 * @param time  时间戳
	 * @date 2015-11-4
	 */
	public static int getYear(long time){
		Calendar calendar = getCalendar(time);
		return calendar.get(Calendar.YEAR);
	}
	
}
