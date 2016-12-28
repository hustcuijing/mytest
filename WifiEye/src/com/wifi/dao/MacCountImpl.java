package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.wifi.model.AllUniqueMacBean;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class MacCountImpl implements MacCount {

	public int countDevice(String start_time, String end_time, String usr_mac,
			String area, String addr, String device_name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			if (start_time != null && start_time.length() != 0)
				sdf.parse(start_time);
			if (end_time != null && end_time.length() != 0)
				sdf.parse(end_time);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 1;// ռλ��Ĭ�ϳ�ʼ���
		StringBuilder sql = new StringBuilder(
				"select count(distinct device_name) from interface,location,device where id >= 1 ");
		if (StringUtil.isNotEmpty(start_time)) {
			sql.append("and interface.recordtime >= ?");
		}
		if (StringUtil.isNotEmpty(end_time)) {
			sql.append("and interface.recordtime <= ?");
		}
		if (StringUtil.isNotEmpty(usr_mac)) {
			sql.append("and interface.usr_mac = ?");
		}
		if (StringUtil.isNotEmpty(area)) {
			sql.append("and location.area =?");
		}
		if (StringUtil.isNotEmpty(addr)) {
			sql.append("and location.addr=?");
		}
		if (StringUtil.isNotEmpty(device_name)) {
			sql.append("and device_name=?");
		}
		sql.append(" and location.location_id = device.location_id and device.device_mac = interface.device_mac");
		try {
			pstmt = con.prepareStatement(sql.toString());
			if (StringUtil.isNotEmpty(start_time)) {
				pstmt.setString(i++, start_time);
			}
			if (StringUtil.isNotEmpty(end_time)) {
				pstmt.setString(i++, end_time);
			}
			if (StringUtil.isNotEmpty(usr_mac)) {
				pstmt.setString(i++, usr_mac);
			}
			if (StringUtil.isNotEmpty(area)) {
				pstmt.setString(i++, area);
			}
			if (StringUtil.isNotEmpty(addr)) {
				pstmt.setString(i++, addr);
			}
			if (StringUtil.isNotEmpty(device_name)) {
				pstmt.setString(i++, device_name);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public List<AllUniqueMacBean> countAllUniqueMac(String start_time,
			String end_time, String usr_mac, String area, String addr,
			String device_name, PageBean page) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<AllUniqueMacBean> list = new ArrayList<AllUniqueMacBean>();
		try {
			if (StringUtil.isNotEmpty(start_time))
				sdf.parse(start_time);
			if (StringUtil.isNotEmpty(end_time))
				sdf.parse(end_time);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 1;// ռλ��Ĭ�ϳ�ʼ���
		StringBuilder sql = new StringBuilder(
				"select device_mac,device_name,sum(total) as total,count(*) as unitotal from ( select device_name,interface.device_mac,count(*) as total from interface,location,device where id >= 1 ");
		if (StringUtil.isNotEmpty(start_time)) {
			sql.append("and interface.recordtime >= ?");
		}
		if (StringUtil.isNotEmpty(end_time)) {
			sql.append("and interface.recordtime <= ?");
		}
		if (StringUtil.isNotEmpty(usr_mac)) {
			sql.append("and interface.usr_mac = ?");
		}
		if (StringUtil.isNotEmpty(area)) {
			sql.append("and location.area =?");
		}
		if (StringUtil.isNotEmpty(addr)) {
			sql.append("and location.addr=?");
		}
		if (StringUtil.isNotEmpty(device_name)) {
			sql.append("and device_name=?");
		}
		sql.append(" and device.device_mac = interface.device_mac group by device_mac,usr_mac");
		String sql1 = ") as t group by device_name limit " + page.getStart() + "," + page.getRows();
		sql.append(sql1);
		try {
			pstmt = con.prepareStatement(sql.toString());
			if (StringUtil.isNotEmpty(start_time)) {
				pstmt.setString(i++, start_time);
			}
			if (StringUtil.isNotEmpty(end_time)) {
				pstmt.setString(i++, end_time);
			}
			if (StringUtil.isNotEmpty(usr_mac)) {
				pstmt.setString(i++, usr_mac);
			}
			if (StringUtil.isNotEmpty(area)) {
				pstmt.setString(i++, area);
			}
			if (StringUtil.isNotEmpty(addr)) {
				pstmt.setString(i++, addr);
			}
			if (StringUtil.isNotEmpty(device_name)) {
				pstmt.setString(i++, device_name);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				AllUniqueMacBean temp = new AllUniqueMacBean();
				temp.setAddress(rs.getString(1) + rs.getString(2));
				temp.setDevice_name(rs.getString(3));
				temp.setAllcount(rs.getInt(4));
				temp.setUniquecount(rs.getInt(5));
				list.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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

	public ResultSet countMac(String start_time, String end_time,
			String usr_mac, String area, String addr, String device_name,
			PageBean page) {

		return null;

	}

	public ResultSet countUniMac(String start_time, String end_time,
			String usr_mac, String area, String addr, String device_name,
			PageBean page) {

		return null;
	}

}
