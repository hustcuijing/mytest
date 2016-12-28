package com.wifi.dao;

import java.sql.ResultSet;
import java.util.List;

import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.model.VisitInfoBean;

public interface VisitDao {
	
	public List<VisitInfoBean> getAllVisits(String start_time,String end_time,String usr_mac,
									String ap_mac,String area,String addr,String device_name,PageBean page);
	public int getVisitCount(String start_time,String end_time,String usr_mac,
								String ap_mac,String area,String addr,String device_name);

	public List<VisitInfoBean> getAllVisitsByNoAddrORArea(String start_time,
			String end_time, String area, String addr, PageBean page) ;
}
