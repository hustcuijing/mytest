package com.wifi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * Date类型数据按指定的String格式格式化成String
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            格式
	 * @return 返回转换后的String数据
	 */
	public static String formatDate(Date date, String format) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			result = sdf.format(date);
		}
		return result;
	}

	/**
	 * 把String类型数据按指定格式转换成Date数据
	 * 
	 * @param str
	 *            要转换的String数据
	 * @param format
	 *            转换格式
	 * @return 返回转换后的Date数据
	 * @throws Exception
	 *             抛出异常
	 */
	public static Date formatString(String str, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}
	public static int getDiffHours(String start_time,String end_time) throws Exception{
		Date d1 = formatString(start_time, "yyyy-MM-dd HH:mm:ss");
		Date d2 = formatString(end_time, "yyyy-MM-dd HH:mm:ss");

		System.out.println(d1 + "@@" + d2);
		System.out.println(d1.getTime() + " " + d2.getTime());
		long diff = d2.getTime() - d1.getTime();
		System.out.println(diff);
		int hours = (int)(diff/3600000);
		System.out.println("**" + diff/3600000);
		System.out.println(hours);
		return (int)(diff/3600000);
	}
	/**
	 * 获得离beginDate有pos天数的日期字符串
	 * @param beginDate 开始时间字符串
	 * @param pos 距离天数
	 * @return
	 */
	public static String getExistDates(String beginDate,int pos){
		Calendar c = Calendar.getInstance();
		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = formatString(beginDate, "yyyy-MM-dd");
			c.setTime(d1);
			c.add(Calendar.DAY_OF_MONTH, pos);
			result = df.format(c.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获得距离时间字符串date有minutes分钟的日期
	 * @param date 原始字符串
	 * @param minutes 距离的分钟数
	 * @return
	 */
	public static String addDateWithMinute(String date,int minutes){
		
		Calendar c = Calendar.getInstance();
		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = formatString(date, "yyyy-MM-dd HH:mm:ss");
			c.setTime(d1);
			c.add(Calendar.MINUTE, minutes);
			result = df.format(c.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获得一天之中的minute属于第几个分钟
	 * @param day
	 * @param minute
	 * @return
	 */
	public static int getInternalMinute(String day,String minute){
		int result = -1;
		try {
			Date d1 = formatString(day, "yyyy-MM-dd");
			Date d2 = formatString(minute, "yyyy-MM-dd HH:mm");
			long interenal = d2.getTime() - d1.getTime();
			if(interenal >= 0)
				result = (int)interenal/60000;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获得隔离的天数
	 * @param day
	 * @param minute
	 * @return
	 */
	public static int getInternalDay(String day1,String day2){
		int result = -1;
		try {
			Date d1 = formatString(day1, "yyyy-MM-dd");
			Date d2 = formatString(day2, "yyyy-MM-dd");
			long interenal = d2.getTime() - d1.getTime();//毫秒数
			if(interenal >= 0)
				result = (int)interenal/86400000;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println("");
	}
	//获取是第几分钟
	public static int getMinutes(String time){
		int m = 0;
		m = Integer.valueOf(time.substring(0,2)) * 60 + Integer.valueOf(time.substring(3,5));
		return m;
	}
	
	//将分钟加在
}
