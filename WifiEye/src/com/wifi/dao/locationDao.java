package com.wifi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import com.wifi.model.PageBean;

public interface locationDao {

	public List<String> getUniqueArea();
	public List<String> getAddrByArea(String area);
	public ResultSet getTypeList(String name,PageBean page);
	public int getTypeCount(Connection con,String name);
	public boolean existTypeWithName(Connection con,String name);
	public int typeAdd(Connection con,String name);
	public int typeUpdate(Connection con,int id,String name);
	public int typeDelete(Connection con ,String delIds);
	public ResultSet nameList(Connection con);
	public int getIdByName(String type);
}
