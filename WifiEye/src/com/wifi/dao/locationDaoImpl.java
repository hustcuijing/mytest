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


public class locationDaoImpl implements locationDao{

	public List<String> getUniqueArea() {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		
		String sql = "select area from location" ;
		try {
		    pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String area = rs.getString(1);
				if(!list.contains(area))
				   list.add(area);
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

	public List<String> getAddrByArea(String area) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		
		String sql = "select addr from location where area = ?" ;
		try {
		    pstmt = con.prepareStatement(sql);
		    pstmt.setString(1, area);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String addr = rs.getString(1);
				if(!list.contains(addr))
				   list.add(addr);
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

	public ResultSet getTypeList(String name, PageBean page) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		String sql = "select * from location_type where location_type_id >= 1";
		int i = 1;
		if(StringUtil.isNotEmpty(name)){
			sql += " and name = ? ";
		}
		
		sql += " order by location_type_id desc limit "+page.getStart()+","+page.getRows();
		try {
		    pstmt = con.prepareStatement(sql);
		    if(StringUtil.isNotEmpty(name)){
		    	pstmt.setString(i++, name);
		    }
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public int getTypeCount(Connection con,String name) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select count(*) as total from location_type where location_type_id >= 1";
		int i = 1;
		if(StringUtil.isNotEmpty(name)){
			sql += " and name = ?";
		}
		try {
		    pstmt = con.prepareStatement(sql);
		    if(StringUtil.isNotEmpty(name)){
		    	pstmt.setString(i++, name);
		    }
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt("total");
			}else{
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return 0;
	}

	public boolean existTypeWithName(Connection con, String name) {
		String sql="select * from location_type where name=?";
		PreparedStatement pstmt;
		boolean result = false;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			result =  pstmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int typeAdd(Connection con, String name) {
		String sql="insert into location_type values(null,?)";
		int result = 0;
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int typeUpdate(Connection con, int id,String name) {
		String sql="update location_type set name = ? where location_type_id = ?";
		int result = 0;
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int typeDelete(Connection con, String delIds) {
		String sql="delete from location_type where location_type_id in ("+delIds+")";
		int result = 0;
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public ResultSet nameList(Connection con) {
		// TODO Auto-generated method stub
		String sql="select * from location_type";
		ResultSet result = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			result = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int getIdByName(String type) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select location_type_id from location_type where name = ?";
		int result = -1;
		try {
		    pstmt = con.prepareStatement(sql);
		    pstmt.setString(1, type);
			rs = pstmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null)
					rs.close();
				if(pstmt != null)
					pstmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return result;
	}

}
