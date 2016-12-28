package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.wifi.model.LocationBean;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class UnitDaoImpl implements UnitDao{

	public List<LocationBean> getAllLocationInfo(Connection con,String area, String addr,
			int location_type_id, double latitude, double longtitude,
			PageBean page) {
		StringBuilder sb = new StringBuilder();
		String sql = "select location_id,area,addr,name as location_type,latitude,longtitude " +
					"from location,location_type " +
					"where location.location_type_id = location_type.location_type_id";
		sb.append(sql);
		if(StringUtil.isNotEmpty(area)){
			sb.append(" and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sb.append(" and addr = ?");
		}
		if(location_type_id != -1){
			sb.append(" and location.location_type_id = ?");
		}
		if(latitude != -1){
			sb.append(" and latitude = ?");
		}
		if(longtitude != -1){
			sb.append(" and longtitude = ?");
		}
		sb.append(" limit "+page.getStart() + ","+page.getRows());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 1;
		List<LocationBean> result = new ArrayList<LocationBean>();
		try {
			pstmt = con.prepareStatement(sb.toString());
			if(StringUtil.isNotEmpty(area)){
				pstmt.setString(i++, area);
			}
			if(StringUtil.isNotEmpty(addr)){
				pstmt.setString(i++, addr);
			}
			if(location_type_id != -1){
				pstmt.setInt(i++, location_type_id);
			}
			if(latitude != -1){
				pstmt.setDouble(i++,latitude );
			}
			if(longtitude != -1){
				pstmt.setDouble(i++,longtitude );
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				LocationBean locationBean = new LocationBean();
				locationBean.setLocation_id(rs.getInt(1));
				locationBean.setArea(rs.getString(2));
				locationBean.setAddr(rs.getString(3));
				locationBean.setLocation_type(rs.getString(4));
				locationBean.setLatitude(rs.getDouble(5));
				locationBean.setLongtitude(rs.getDouble(6));
				result.add(locationBean);
			}
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		
		return result;
	}

	public int getUnitCount(Connection con,String area, String addr, int location_type_id,
			double latitude, double longtitude) {
		StringBuilder sb = new StringBuilder();
		String sql = "select count(*) " +
					"from location,location_type " +
					"where location.location_type_id = location_type.location_type_id";
		sb.append(sql);
		if(StringUtil.isNotEmpty(area)){
			sb.append(" and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sb.append(" and addr = ?");
		}
		if(location_type_id != -1){
			sb.append(" and location.location_type_id = ?");
		}
		if(latitude != -1){
			sb.append(" and latitude = ?");
		}
		if(longtitude != -1){
			sb.append(" and longtitude = ?");
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 1;
		int count = 0;
		try {
			pstmt = con.prepareStatement(sb.toString());
			if(StringUtil.isNotEmpty(area)){
				pstmt.setString(i++, area);
			}
			if(StringUtil.isNotEmpty(addr)){
				pstmt.setString(i++, addr);
			}
			if(location_type_id != -1){
				pstmt.setInt(i++, location_type_id);
			}
			if(latitude != -1){
				pstmt.setDouble(i++,latitude );
			}
			if(longtitude != -1){
				pstmt.setDouble(i++,longtitude );
			}
			
			//System.out.println(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		
		return  count;
	}

	public boolean existLocationWithAddr(Connection con, String area,
			String addr) {
		boolean result = false;
		String sql = "select location_id from location where area = ? and addr = ?";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, area);
			psmt.setString(2, addr);
			rs = psmt.executeQuery();
			if(rs.next())
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(psmt != null)
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return result;
	}

	public int locationAdd(Connection con, String area, String addr,
			int location_type_id, double latitude, double longtitude) {
		String sql = "insert into location values(null,?,?,?,?,?)";
		int result = 0;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, area);
			psmt.setString(2, addr);
			psmt.setInt(3, location_type_id);
			psmt.setDouble(4, latitude);
			psmt.setDouble(5, longtitude);
			result = psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(psmt != null)
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
				
		return result;
	}

	public int locationDelete(Connection con, String delIds) {
		String sql = "delete  from location where location_id in (" + delIds + ")";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			psmt = con.prepareStatement(sql);
			result = psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(psmt != null)
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	public int locationUpdate(Connection con, String area, String addr,
			int location_type_id, double latitude, double longtitude,int location_id) {
		String sql = "update location set area = ?,addr = ?,location_type_id = ?,latitude = ?,longtitude = ? where location_id = ?";
		int result = 0;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, area);
			psmt.setString(2, addr);
			psmt.setInt(3, location_type_id);
			psmt.setDouble(4, latitude);
			psmt.setDouble(5, longtitude);
			psmt.setInt(6, location_id);
			result = psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(psmt != null)
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
				
		return result;
	}

	public boolean existLocationWithAddr(Connection con, String area,
			String addr, int location_id) {
		boolean result = false;
		String sql = "select location_id from location where area = ? and addr = ? and location_id != ?";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, area);
			psmt.setString(2, addr);
			psmt.setInt(3, location_id);
			rs = psmt.executeQuery();
			if(rs.next())
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(psmt != null)
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return result;
	}

	public int getIdWithAreaAddr(String area, String addr) {
		int result = 0;
		String sql = "select location_id from location where area = ? and addr = ?";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			con = DBManager.getCon();
			psmt = con.prepareStatement(sql);
			psmt.setString(1, area);
			psmt.setString(2, addr);
			rs = psmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(psmt != null)
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return result;
	}

}
