package com.wifi.dao;

import java.util.List;

import com.wifi.model.AppInfo;
import com.wifi.model.PageBean;

public interface AppInfoDao {

	public int getAllCount(String start_time,String  end_time,String area,String addr,int app_type);
	public List<AppInfo> getAllAppInfos(String start_time,String end_time,String area,String addr,int app_type,PageBean page);
}
