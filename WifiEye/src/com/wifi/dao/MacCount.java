package com.wifi.dao;

import java.sql.ResultSet;
import java.util.List;

import com.wifi.model.AllUniqueMacBean;
import com.wifi.model.PageBean;


public interface MacCount {

	public int countDevice(String start_time, String end_time,String usr_mac, String area,String addr,String device_name);
	public List<AllUniqueMacBean>  countAllUniqueMac(String start_time, String end_time,String usr_mac,String area,String addr,String device_name,PageBean page);
	public ResultSet countMac(String start_time, String end_time,String usr_mac,String area,String addr,String device_name,PageBean page);
	public ResultSet countUniMac(String start_time, String end_time,String usr_mac,String area,String addr,String device_name,PageBean page);
}
