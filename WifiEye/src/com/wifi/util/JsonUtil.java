package com.wifi.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	/**
	 * 把结果集ResultSet数据转换成JSONArray数据
	 * @param rs 需要转换的ResultSet数据
	 * @return 返回JSONArray数据
	 * @throws Exception 返回异常
	 */
	public static JSONArray formatRsToJsonArray(ResultSet rs)throws Exception{
		ResultSetMetaData md=rs.getMetaData();
		int num=md.getColumnCount();
		JSONArray array=new JSONArray();
		while(rs.next()){
			JSONObject mapOfColValues=new JSONObject();
			for(int i=1;i<=num;i++){
				Object o=rs.getObject(i);
				if(o instanceof Date){
					mapOfColValues.put(md.getColumnName(i), DateUtil.formatDate((Date)o, "yyyy-MM-dd"));
				}else{
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));					
				}
			}
			array.add(mapOfColValues);
		}
		return array;
	}
	/**
	 * 把list数据转换成JSONArray数据
	 * @param list 需要转换的list数据
	 * @return 返回JSONArray数据
	 * @throws Exception 抛出异常
	 */
	public static JSONArray formatListToJsonArray(List list)throws Exception{
		
		JSONArray array= JSONArray.fromObject(list);
		
		return array;
	}
}
