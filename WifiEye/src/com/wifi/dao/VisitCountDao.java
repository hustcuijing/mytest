package com.wifi.dao;

import java.util.List;

import com.wifi.model.PageBean;
import com.wifi.model.VisitCount;

public interface VisitCountDao {
	
	public int getAllCount(String start_time,String end_time,String area,String addr,String device_name);
	public List<VisitCount> getAllVisitCounts(String start_time,String end_time,String area,String addr,String device_name,PageBean page);
	public int getUniqueMacs(String start_time,String end_time,String device_name);
	public List<VisitCount> getAllVisitCounts2(String start_time,String end_time,String device_name,PageBean page);
	public VisitCount getVisitCount(String start_time,String end_time,String device_name);
}
