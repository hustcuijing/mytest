package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wifi.model.GroupMember;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;

public class GroupMemberDaoImpl implements GroupMemberDao{

	public List<GroupMember> getMember(String name,PageBean pageBean) {
		// TODO Auto-generated method stub
		Connection  con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<GroupMember> list = new ArrayList<GroupMember>();
		String sql = "select grouptype.name,groupmember.usr_mac from grouptype,groupmember where grouptype.name = ? and grouptype.grouptype_id = groupmember.grouptype_id " +
				"order by groupmember.member_id desc limit " + pageBean.getStart() + "," + pageBean.getRows();
		con = DBManager.getCon();
		try {
			pstmt = DBManager.prepare(con, sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(new GroupMember(rs.getString(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public int deleteMember(String names,String usr_macs) {
		// TODO Auto-generated method stub
		GroupDao gd = new GroupDaoImpl();
		String[] namess = names.split(",");
		int id = gd.getIdByname(namess[0]);
		System.out.println("要删除的id" + id);
		System.out.println(usr_macs);
		String sql="delete from groupmember where grouptype_id = ? and usr_mac in ("+usr_macs+")";
		Connection  con = null;
		PreparedStatement pstmt = null;
		con = DBManager.getCon();
		pstmt = DBManager.prepare(con, sql);
		try {
			pstmt.setInt(1, id);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return 0;
	}

	public boolean isExitsMember(String usr_mac, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public void addMember(String usr_mac, String name) {
		// TODO Auto-generated method stub
		Connection  con = null;
		PreparedStatement pstmt = null;
		GroupDao gd = new GroupDaoImpl();
		int id = gd.getIdByname(name);
		ResultSet rs = null;
		String sql = "insert into groupmember(usr_mac,grouptype_id) values(?,?)" ;
		con = DBManager.getCon();
		try {
			pstmt = DBManager.prepare(con, sql);
			pstmt.setString(1, usr_mac);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getMemeberCount(String name) {
		// TODO Auto-generated method stub
		Connection  con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from grouptype,groupmember where grouptype.name = ? and grouptype.grouptype_id = groupmember.grouptype_id" ;
		con = DBManager.getCon();
		try {
			pstmt = DBManager.prepare(con, sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public boolean updateMemeber(String usr_mac, String name) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "update groupmember set usr_mac = ? where usr_mac = ";
		return false;
	}

	public List<String> getMemeberByGroup(String group) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select usr_mac from grouptype,groupmember where grouptype.name = ? and grouptype.grouptype_id = groupmember.grouptype_id";
		
	    con = DBManager.getCon();
	    pstmt = DBManager.prepare(con, sql);
	    try {
			pstmt.setString(1, group);
			rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		return list;
	}

}
