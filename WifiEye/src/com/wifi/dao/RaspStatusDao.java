package com.wifi.dao;

import java.util.List;
import java.util.Map;

import com.wifi.model.DeviceBean;
import com.wifi.model.PageBean;

interface RaspStatusDao {
	
	public List<String> getIps();
	public int updateStatus(Map<String,Integer> map);
	public List<DeviceBean> getDeviceInfo(String area,String addr,String device_name,PageBean page,String ip,int status);
	public int selectDevcieCount(String area, String addr, String device_name,String ip,int status);
}
