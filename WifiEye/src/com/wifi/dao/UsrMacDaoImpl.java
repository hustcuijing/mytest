package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wifi.dao.UsrMacDao;
import com.wifi.model.CompressUsrMac;
import com.wifi.model.T_UserMac;
import com.wifi.util.DBManager;

public class UsrMacDaoImpl implements UsrMacDao {

	/**
	 * 查找是否已经存在usr_mac的记录
	 */
	public boolean hasData(String usr_mac) {
		boolean result = false;
		String sql = "select usr_mac from t_usermac where usr_mac = ?";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			psmt.setString(1, usr_mac);
			rs = psmt.executeQuery();
			if(rs.next())
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}

	public int insert(CompressUsrMac c) {
		int result = 0;
		String sql = "insert into t_usermac values(?,?,?,null,null,?,?)";
		Connection con = null;
		PreparedStatement psmt = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			psmt.setString(1,c.getUsr_mac());
			psmt.setString(2, c.getDay());
			psmt.setString(3, c.getDay());
			psmt.setString(4, c.getTimeString());
			psmt.setString(5, c.getDevice_macString());
			
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		
		return result;
	}

	public int update(T_UserMac c) {
		String sql = "update t_usermac set start_time = ?,last_time = ?,time_compress=?,device_compress=?,cur_time_compress=?,cur_device_compress=? where usr_mac = ?";
		Connection con = null;
		PreparedStatement psmt = null;
		int result = 0;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			psmt.setString(1, c.getStart_time());
			psmt.setString(2, c.getLast_time());
			psmt.setString(3, c.getTime_compress());
			psmt.setString(4, c.getDevice_compress());
			psmt.setString(5, c.getCur_time_compress());
			psmt.setString(6, c.getCur_device_compress());
			psmt.setString(7, c.getUsr_mac());
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

	public T_UserMac getRecordByUsrmac(String usr_mac) {
		String sql = "select usr_mac,start_time,last_time,time_compress,device_compress,cur_time_compress,cur_device_compress from t_usermac where usr_mac = ?";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		T_UserMac user_mac = new T_UserMac();
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			psmt.setString(1, usr_mac);
			rs = psmt.executeQuery();
			if(rs.next()){
				user_mac.setUsr_mac(rs.getString(1));
				user_mac.setStart_time(rs.getString(2));
				user_mac.setLast_time(rs.getString(3));
				user_mac.setTime_compress(rs.getString(4));
				user_mac.setDevice_compress(rs.getString(5));
				user_mac.setCur_time_compress(rs.getString(6));
				user_mac.setCur_device_compress(rs.getString(7));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user_mac;
	}

}
