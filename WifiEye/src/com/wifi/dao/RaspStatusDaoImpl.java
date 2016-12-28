package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wifi.model.DeviceBean;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;


public class RaspStatusDaoImpl implements RaspStatusDao{

	public List<String> getIps() {
		// TODO Auto-generated method stub
		String sql = "select ip from device";
		List<String> result = new ArrayList<String>();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while(rs.next())
				result.add(rs.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}

	public int updateStatus(Map<String, Integer> map) {
		String sql = "update device set status = ? where ip = ?";
		Connection con = null;
		PreparedStatement psmt = null;
		con = DBManager.getCon();
		int result = 0;
		
		try {
			con.setAutoCommit(false);
			for(String key : map.keySet()){
				int value = map.get(key);
				psmt = DBManager.prepare(con, sql);
				psmt.setInt(1, value);
				psmt.setString(2, key);
				psmt.executeUpdate();
				result++;
			}
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			result = -1;
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		
		return result;
	}

	public List<DeviceBean> getDeviceInfo(String area, String addr,
			String device_name, PageBean page, String ip, int status) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<DeviceBean> list = new ArrayList<DeviceBean>();
		
		int i = 1;
		StringBuilder sql = new StringBuilder("select device_mac,device_name,ip,area,addr,status,status_time from device,location where device.location_id = location.location_id ") ;
		if(StringUtil.isNotEmpty(area)){
			sql.append("and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sql.append("and addr = ?");
		}
		if(StringUtil.isNotEmpty(device_name)){
			sql.append("and device_name = ?");
		}
		if(StringUtil.isNotEmpty(ip)){
			sql.append(" and ip = ?");
		}
		if(status != -1){
			sql.append(" and status = ?");
		}
		if(null!=page){
			sql.append(" limit " + page.getStart() + "," + page.getRows() );
		}
		try {
			pstmt = con.prepareStatement(sql.toString());
		    if(StringUtil.isNotEmpty(area)){
		    	 pstmt.setString(i++, area);
			}
		    if(StringUtil.isNotEmpty(addr)){
		    	 pstmt.setString(i++, addr);
			}
			if(StringUtil.isNotEmpty(device_name)){
				 pstmt.setString(i++, device_name);
			}
			if(StringUtil.isNotEmpty(ip)){
				pstmt.setString(i++, ip);
			}
			if(status != -1){
				pstmt.setInt(i++, status);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				DeviceBean db = new DeviceBean();
				db.setDevice_mac(rs.getString(1));
				db.setDevice_name(rs.getString(2));
				db.setIp(rs.getString(3));
				db.setArea(rs.getString(4));
				db.setAddr(rs.getString(5));
				db.setStatus(rs.getInt(6));
				db.setStatus_time(rs.getString(7));
				list.add(db);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public int selectDevcieCount(String area, String addr, String device_name,
			String ip, int status) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		int i = 1;
		StringBuilder sql = new StringBuilder("select count(*) from device,location where device.location_id = location.location_id ") ;
		if(StringUtil.isNotEmpty(area)){
			sql.append("and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sql.append("and addr = ?");
		}
		if(StringUtil.isNotEmpty(device_name)){
			sql.append("and device_name = ?");
		}
		if(StringUtil.isNotEmpty(ip)){
			sql.append(" and ip = ?");
		}
		if(status != -1){
			sql.append(" and status = ?");
		}
		try {
			pstmt = con.prepareStatement(sql.toString());
		    if(StringUtil.isNotEmpty(area)){
		    	 pstmt.setString(i++, area);
			}
		    if(StringUtil.isNotEmpty(addr)){
		    	 pstmt.setString(i++, addr);
			}
			if(StringUtil.isNotEmpty(device_name)){
				 pstmt.setString(i++, device_name);
			}
			if(StringUtil.isNotEmpty(ip)){
				pstmt.setString(i++, ip);
			}
			if(status != -1){
				pstmt.setInt(i++, status);
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}

}
