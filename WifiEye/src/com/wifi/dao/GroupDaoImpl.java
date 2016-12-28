package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wifi.model.GroupTypeBean;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;

public class GroupDaoImpl implements GroupDao {

	public List<GroupTypeBean> getNameList() {
		// TODO Auto-generated method stub
		Connection con = DBManager.getCon();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select grouptype_id,name from grouptype";
		List<GroupTypeBean> list = new ArrayList<GroupTypeBean>();
		try {
			pstmt = DBManager.prepare(con, sql);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new GroupTypeBean(rs.getInt(1), rs.getString(2)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return list;
	}

	public List<GroupTypeBean> getNameListMain(String name, PageBean pageBean) {
		// TODO Auto-generated method stub
		String sql = "select grouptype_id,name  from grouptype where grouptype_id >= 1";
		List<GroupTypeBean> list = new ArrayList<GroupTypeBean>();
		if (name != null && !name.equals(""))
			sql += " and name = ?";
		sql += " order by grouptype_id desc limit " + pageBean.getStart() + ","
				+ pageBean.getRows();
		Connection con = DBManager.getCon();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = DBManager.prepare(con, sql);
			if (name != null && !name.equals(""))
				pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new GroupTypeBean(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return list;
	}

	public int listCount(String name) {
		// TODO Auto-generated method stub
		String sql = "select count(*)  from grouptype where grouptype_id >= 1";
		List<GroupTypeBean> list = new ArrayList<GroupTypeBean>();
		if (name != null && !name.equals(""))
			sql += " and name = ?";
		Connection con = DBManager.getCon();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = DBManager.prepare(con, sql);
			if (name != null && !name.equals(""))
				pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return 0;
	}

	public boolean isExitsGroupName(String name) {
		// TODO Auto-generated method stub
		String sql = "select name  from grouptype where grouptype_id >= 1";
		sql += " and name = ?";
		Connection con = DBManager.getCon();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = DBManager.prepare(con, sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return false;
	}

	public void addGroupName(String name) {
		// TODO Auto-generated method stub
		String sql = "insert into grouptype(name) values (?)";
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;

		try {
			pstmt = DBManager.prepare(con, sql);
			pstmt.setString(1, name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			DBManager.close(pstmt);
			DBManager.close(con);
		}
	}

	public void updateGroupName(int groupType_id, String name) {
		// TODO Auto-generated method stub
		String sql = "update grouptype set name = ? where groupType_id = ?";
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		try {
			pstmt = DBManager.prepare(con, sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, groupType_id);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			DBManager.close(pstmt);
			DBManager.close(con);
		}
	}

	public int deleteGroupName(String groupType_ids) {
		// TODO Auto-generated method stub
		Connection con = DBManager.getCon();
		String sql="delete from grouptype where groupType_id in ("+groupType_ids+")";
		System.out.println(sql);
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

	public int getIdByname(String name) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select groupType_id from grouptype where name = ? ";
		con = DBManager.getCon();
		pstmt = DBManager.prepare(con, sql);
		try {
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return 0;
	}
	
}
