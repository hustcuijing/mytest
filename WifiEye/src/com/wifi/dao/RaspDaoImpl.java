package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wifi.model.DeviceBean;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class RaspDaoImpl implements RaspDao{

	public ResultSet getRaspList(Connection con,String device_name,PageBean page) {
		// TODO Auto-generated method stub
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String  sql = "select device_mac,device_name from device";
		if(StringUtil.isNotEmpty(device_name)){
			sql += " where device_name = ?";
		}
		sql +=" limit "+page.getStart()+","+page.getRows();
		try {
			psmt = con.prepareStatement(sql);
			if(StringUtil.isNotEmpty(device_name)){
				psmt.setString(1, device_name);
			}
			rs = psmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public int raspAdd(String device_mac, String device_name) {
		// TODO Auto-generated method stub
		String sql = "insert into device(device_mac,device_name) values(?,?)";
		
		//同时添加此设备表
		String sql2 = "use wifi;  ";
		String sql3 = "create table "+device_mac.replace(':', '_')+"( ID int(11)  primary key,device_mac char(18),"+
		"usr_mac char(18),ap_mac char(18),date_rate float,rssi_signal int,first_time varchar(25),last_time varchar(25),stay_time varchar(25),"+
		"online_times int,area varchar(50),addr  varchar(50),channel_id int,latitude double(13,13),longtitude double(13,13),"+
		"app_type int,app_info varchar(128));";

		Connection con = null;
		int result = 0;
		PreparedStatement psmt = null;
		Statement pstm2 = null;
		
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			pstm2 = con.createStatement();
			pstm2.addBatch(sql2);
			pstm2.addBatch(sql3);
//			pstm2 = con.prepareStatement(sql2);
			psmt.setString(1, device_mac);
			psmt.setString(2, device_name);
			result = psmt.executeUpdate();
//			pstm2.executeUpdate();
			pstm2.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}

	public boolean raspExist(String device_mac, String device_name) {
		String sql = "select * from device where device_mac = ? or device_name = ?";
		Connection con = null;
		boolean result = false;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, device_mac);
			psmt.setString(2, device_name);
			rs = psmt.executeQuery();
			if(rs.next())
				result = true;
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

	public int raspUpdate(String device_mac, String device_name,String device_id) {
		// TODO Auto-generated method stub
		
		String sql = "update device set device_mac = ?,device_name = ? where device_mac = ?";
		Connection con = null;
		int result = 0;
		PreparedStatement psmt = null;
		
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, device_mac);
			psmt.setString(2, device_name);
			psmt.setString(3, device_id);
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}

	public int getRaspCount(Connection con,String device_name) {
		// TODO Auto-generated method stub
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int result = 0;
		String  sql = "select count(*) from device";
		if(StringUtil.isNotEmpty(device_name)){
			sql += " where device_name = ?";
		}
		try {
			psmt = con.prepareStatement(sql);
			if(StringUtil.isNotEmpty(device_name)){
				psmt.setString(1, device_name);
			}
			rs = psmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
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

	public boolean raspExistUpdate(String device_mac, String device_name,String device_id) {
		// TODO Auto-generated method stub
		String sql = "select * from device where (device_mac = ? or device_name = ?) and device_mac != ?";
		Connection con = null;
		boolean result = false;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, device_mac);
			psmt.setString(2, device_name);
			psmt.setString(3, device_id);
			rs = psmt.executeQuery();
			if(rs.next())
				result = true;
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

	public int raspDelete(String delIds) {
		// TODO Auto-generated method stub
		String[] strs = delIds.split(",");
		Connection con = null;
		int result = 0;
		PreparedStatement psmt = null;
		
		con = DBManager.getCon();
		try {
			con.setAutoCommit(false);
			for(String str : strs){
				String sql = "delete from device where device_mac = '" + str + "'";
				psmt = con.prepareStatement(sql);
				psmt.executeUpdate();
			}
			con.commit();
			result  = 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				con.rollback();
				result = -1;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}
	public List<DeviceBean> getAllRasp(){
		List<DeviceBean> list = new ArrayList<DeviceBean>();
		String sql = "select device_name from device";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()){
				DeviceBean db = new DeviceBean();
				db.setDevice_name(rs.getString(1));
				list.add(db);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return list;
	}

}
