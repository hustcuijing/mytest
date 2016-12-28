package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wifi.model.PageBean;
import com.wifi.model.VisitCount;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class VisitCountDaoImpl implements VisitCountDao{

	public int getAllCount(String start_time, String end_time, String area,
			String addr, String device_name) {
		// TODO Auto-generated method stub
		String string = "select count(*) from (select device_mac from visit where id >= 1 ";
		StringBuilder sql = new StringBuilder(string);
		if(StringUtil.isNotEmpty(start_time)){
			sql.append(" and first_time >= ?");
		}
		if(StringUtil.isNotEmpty(end_time)){
			sql.append(" and first_time <= ?");
		}
		
		if(StringUtil.isNotEmpty(area)){
			sql.append(" and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sql.append(" and addr = ?");
		}
		if(StringUtil.isNotEmpty(device_name)){
			sql.append(" and device_mac = ?");
		}
		sql.append(" group by device_mac) as t");
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int result = 0;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql.toString());
		int i = 1;
		try {
			if(StringUtil.isNotEmpty(start_time)){
				psmt.setString(i++,start_time);
			}
			if(StringUtil.isNotEmpty(end_time)){
				psmt.setString(i++,end_time);
			}
			
			if(StringUtil.isNotEmpty(area)){
				psmt.setString(i++,area);
			}
			if(StringUtil.isNotEmpty(addr)){
				psmt.setString(i++,addr);
			}
			if(StringUtil.isNotEmpty(device_name)){
				psmt.setString(i++,device_name);
			}
			rs = psmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<VisitCount> getAllVisitCounts(String start_time,
			String end_time, String area, String addr, String device_name,
			PageBean page) {
		// TODO Auto-generated method stub
		List<VisitCount> list = new ArrayList<VisitCount>();
		String string = "select device_mac,count(*) as total from visit where id >= 1 ";// group by device_mac,usr_mac) as t group by device_mac";
		StringBuilder sql = new StringBuilder(string);
		if(StringUtil.isNotEmpty(start_time)){
			sql.append(" and first_time >= ?");
		}
		if(StringUtil.isNotEmpty(end_time)){
			sql.append(" and first_time <= ?");
		}
		
		if(StringUtil.isNotEmpty(area)){
			sql.append(" and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sql.append(" and addr = ?");
		}
		if(StringUtil.isNotEmpty(device_name)){
			sql.append(" and device_mac = ?");
		}
		sql.append(" group by device_mac limit " + page.getStart() + "," + page.getRows());
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql.toString());
		int i = 1;
		try {
			if(StringUtil.isNotEmpty(start_time)){
				psmt.setString(i++,start_time);
			}
			if(StringUtil.isNotEmpty(end_time)){
				psmt.setString(i++,end_time);
			}
			
			if(StringUtil.isNotEmpty(area)){
				psmt.setString(i++,area);
			}
			if(StringUtil.isNotEmpty(addr)){
				psmt.setString(i++,addr);
			}
			if(StringUtil.isNotEmpty(device_name)){
				psmt.setString(i++,device_name);
			}
			rs = psmt.executeQuery();
			while(rs.next()){
				VisitCount vc = new VisitCount();
				vc.setDevice_name(rs.getString(1));
				vc.setTotal(rs.getInt(2));
				list.add(vc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public int getUniqueMacs(String start_time, String end_time,
			String device_name) {
		// TODO Auto-generated method stub
		String string = "select count(distinct usr_mac) as total from t_visit_info_" +device_name.replace(':', '_')+" where id >= 1 ";// group by device_mac,usr_mac) as t group by device_mac";
		StringBuilder sql = new StringBuilder(string);
		if(StringUtil.isNotEmpty(start_time)){
			sql.append(" and first_time >= ?" );
		}
		if(StringUtil.isNotEmpty(end_time)){
			sql.append(" and first_time <= ?" );
		} 
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int result = 0;
		connection = DBManager.getCon();
		psmt = DBManager.prepare(connection, sql.toString());
		int i = 1;
		try {
			if(StringUtil.isNotEmpty(start_time)){
				psmt.setString(i++, start_time);
			}
			if(StringUtil.isNotEmpty(end_time)){
				psmt.setString(i++, end_time);
			} 
			rs = psmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<VisitCount> getAllVisitCounts2(String start_time,
			String end_time, String device_name,
			PageBean page) {
		List<VisitCount> list = new ArrayList<VisitCount>();
		String string = "select device_mac,sum(total_mac) as total from t_visit_count ";// group by device_mac,usr_mac) as t group by device_mac";
		StringBuilder sql = new StringBuilder(string);
		boolean isFirst = true;
		if(StringUtil.isNotEmpty(start_time)){
			if(isFirst){
				sql.append(" where begin_time >= ?");
				isFirst = false;
			}else
				sql.append(" and begin_time >= ?");
		}
		if(StringUtil.isNotEmpty(end_time)){
			if(isFirst){
				sql.append(" where begin_time < ?");
				isFirst = false;
			}else
				sql.append(" and begin_time < ?");
		}
		
		if(StringUtil.isNotEmpty(device_name)){
			if(isFirst)
				sql.append(" where device_mac in " + device_name);
			else
				sql.append(" and device_mac in " + device_name);
		}
		sql.append(" group by device_mac limit " + page.getStart() + "," + page.getRows());
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql.toString());
		int i = 1;
		try {
			if(StringUtil.isNotEmpty(start_time)){
				psmt.setString(i++,start_time);
			}
			if(StringUtil.isNotEmpty(end_time)){
				psmt.setString(i++,end_time);
			}
			
			
			rs = psmt.executeQuery();
			while(rs.next()){
				VisitCount vc = new VisitCount();
				vc.setDevice_name(rs.getString(1));
				vc.setTotal(rs.getInt(2));
				list.add(vc);
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

	public VisitCount getVisitCount(String start_time, String end_time,
			String device_name) {
		// TODO Auto-generated method stub
		VisitCount list = new VisitCount();
		String string = "select device_mac,sum(total_mac) as total from visit where id >= 1 ";// group by device_mac,usr_mac) as t group by device_mac";
		StringBuilder sql = new StringBuilder(string);
		if(StringUtil.isNotEmpty(start_time)){
			sql.append(" and begin_time >= ?");
		}
		if(StringUtil.isNotEmpty(end_time)){
			sql.append(" and begin_time <= ?");
		}
		
		if(StringUtil.isNotEmpty(device_name)){
			sql.append(" and device_mac = ?");
		}
		sql.append(" group by device_mac limit ");
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql.toString());
		int i = 1;
		try {
			if(StringUtil.isNotEmpty(start_time)){
				psmt.setString(i++,start_time);
			}
			if(StringUtil.isNotEmpty(end_time)){
				psmt.setString(i++,end_time);
			}
			
			
			if(StringUtil.isNotEmpty(device_name)){
				psmt.setString(i++,device_name);
			}
			rs = psmt.executeQuery();
			while(rs.next()){
				list.setDevice_name(rs.getString(1));
				list.setTotal(rs.getInt(2));
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
