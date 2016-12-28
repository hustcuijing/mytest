package com.wifi.dao;

import java.sql.ResultSet;
import java.util.List;

import com.wifi.model.PageBean;
import com.wifi.model.VisitBean;


public interface allMacDao {
	public int selectMacCount(String start_time, String end_time,String usr_mac,
			String area, String addr, String ap_mac,String device_name);
	public List<VisitBean> selectMac(String first_time,String last_time,String usr_mac,String area,String addr,String ap_mac,String device_name,PageBean page);
	public ResultSet getAllMacs(String first_time,String last_time,String usr_mac,String area,String addr,String ap_mac,String device_name,PageBean page);
}
