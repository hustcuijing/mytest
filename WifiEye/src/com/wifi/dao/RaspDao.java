package com.wifi.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import com.wifi.model.PageBean;

public interface RaspDao {

	public ResultSet getRaspList(Connection con,String device_name,PageBean pageBean);
	public int raspAdd(String device_mac,String device_name);
	public boolean raspExist(String device_mac,String device_name);
	public int raspUpdate(String device_mac,String device_name,String device_id);
	public int getRaspCount(Connection con,String device_name);
	public boolean raspExistUpdate(String device_mac,String device_name,String device_id);
	public int raspDelete(String delIds);
}
