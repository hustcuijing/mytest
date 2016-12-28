package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class AddrDao {
	public ResultSet areaList(Connection con,PageBean pageBean,String para)throws Exception{
		String sql = "select distinct area from location";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeQuery();
		
	}
	public ResultSet addrList(Connection con,PageBean pageBean,String area)throws Exception{
		String sql = "select distinct addr from location where area = '"+area+"'";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeQuery();
		
	}
	public ResultSet deviceList(Connection con,PageBean pageBean,String area,String addr)throws Exception{
		String sql = "select device_mac,device_name from device,location where area = '"+area+"' and addr = '" + addr +"' and device.location_id = location.location_id";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeQuery();
		
	}
	public ResultSet deviceListByArea(Connection con,PageBean pageBean,String area)throws Exception{
		String sql = "select device_mac,device_name from device,location where area = '"+area+"' and device.location_id = location.location_id";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeQuery();
		
	}
	public ResultSet deviceList(Connection con)throws Exception{
		String sql = "select device_name from device where  location_id = -1";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeQuery();
		
	}
	public ResultSet ipList(Connection con)throws Exception{
		String sql = "select  ip from device where  location_id != -1";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeQuery();
		
	}
	public List<String> deviceList(String area,String addr){
		List<String> results = new ArrayList<String>();
		String sql = "select device_mac from device,location where  device.location_id = location.location_id";
		if(StringUtil.isNotEmpty(area)){
			sql += " and area = ?";
		}
		if(StringUtil.isNotEmpty(addr)){
			sql += " and addr = ?";
		}
		Connection con = null;
		con = DBManager.getCon();
		PreparedStatement pstmt=DBManager.prepare(con, sql);
		
		ResultSet rs = null;
		int i = 1;
		
		try {
			if(StringUtil.isNotEmpty(area)){
				pstmt.setString(i++, area);
			}
			if(StringUtil.isNotEmpty(addr)){
				pstmt.setString(i++, addr);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				results.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return results;
		
	}
    public String  devicemac(String device_name){
    	String result = "";
		String sql = "select device_mac from device where device_name = ?";
		Connection con = null;
		con = DBManager.getCon();
		PreparedStatement pstmt=DBManager.prepare(con, sql);
		
		ResultSet rs = null;
		try {
			
			pstmt.setString(1, device_name);
		    rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return result;
    }
   
 
}
