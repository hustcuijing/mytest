package com.wifi.dao;

import java.sql.Connection;
import java.util.List;

import com.wifi.model.LocationBean;
import com.wifi.model.PageBean;

public interface UnitDao {

	public List<LocationBean> getAllLocationInfo(Connection con,String area,String addr,int location_type_id,double latitude,double longtitude,PageBean page);
	public int getUnitCount(Connection con,String area,String addr,int location_type_id,double latitude,double longtitude);
	public boolean existLocationWithAddr(Connection con,String area,String addr);
	public int locationAdd(Connection con,String area,String addr,int location_type_id,double latitude,double longtitude);
	public int locationDelete(Connection con,String delIds);
	public int locationUpdate(Connection con,String area,String addr,int location_type_id,double latitude,double longtitude,int location_id);
	public boolean existLocationWithAddr(Connection con,String area,String addr,int location_id);
	public int getIdWithAreaAddr(String area,String addr);
}
