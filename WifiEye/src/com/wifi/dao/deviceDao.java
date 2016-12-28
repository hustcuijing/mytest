package com.wifi.dao;

import java.util.List;
import java.util.Map;

import com.wifi.model.Address;
import com.wifi.model.DeviceBean;
import com.wifi.model.PageBean;


public interface deviceDao {

	public void setLocationNull(String device_name);
	public List<String> getDevice_name(String area,String addr);
	public int selectDevcieCount(String area, String addr, String device_name,String ip);
	public List<DeviceBean> getDeviceInfo(String area,String addr,String device_name,PageBean page,String ip);
	public int deviceAdd(String device_name,String ip,int location_id);
	public boolean deviceExistWithIp(String ip,String id);
	public int deviceDelete(String delIds);
	
	public List<String> getAllDeviceMacs();
	public Map<String, Integer> getDeviceIndex();
	public Map<Integer, String> getDeviceByIndex();
	public Map<String,Address> getDeviceAddress();
	public String minFirstTime();
	public String maxFirstTime();
	public boolean isTableExits(String tableName);
	public int getTableCount();
}
