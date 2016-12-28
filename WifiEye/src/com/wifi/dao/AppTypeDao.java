package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.wifi.util.DBManager;

public class AppTypeDao {

	public static Map<Integer, String> types = new HashMap<Integer, String>();
	public static String matched(int id){
		if(types.isEmpty())
			getMap();
		return types.get(id);
	}
	public static void getMap(){
		String sql = "select id,type_name from appinfo_type";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while(rs.next()){
				types.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
	}
	public ResultSet getAllTypes(Connection con){
		String sql = "select id,type_name from appinfo_type";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
