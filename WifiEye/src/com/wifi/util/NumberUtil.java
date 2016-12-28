package com.wifi.util;

import com.wifi.dao.deviceDao;
import com.wifi.dao.deviceDaoImpl;

public class NumberUtil {
    private static deviceDao dd = new deviceDaoImpl();
	private static int Hour_number = 3477;//每小时的数量
	private static int tableNumber = new deviceDaoImpl().getTableCount();//获得数据库中设备的总数量
	public static int getNumber(String start_time,String end_time){
		String fs = dd. minFirstTime();//数据库中的最小时间
		System.out.println("最小时间" + fs);
		if(!fs.equals("") && StringUtil.isEmpty(start_time))
		     start_time = fs;
		String es = dd.maxFirstTime();
		System.out.println(es);
		if(!es.equals("") && StringUtil.isEmpty(end_time))
			 end_time = es;
		int result = 0;
		try {
			System.out.println(start_time + "##" + end_time);
				int hours = DateUtil.getDiffHours(start_time, end_time);//获取共有多少小时
				System.out.println(hours);
				result = hours * Hour_number * tableNumber;//获取总数量
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
