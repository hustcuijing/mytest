package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.model.VisitInfoBean;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class VisitDaoImpl implements VisitDao {

	/**
	 * 有设备区域和地址
	 */
	public List<VisitInfoBean> getAllVisits(String start_time, String end_time,
			String usr_mac, String ap_mac, String area, String addr,
			String device_name, PageBean page) {
		List<VisitInfoBean> list = new ArrayList<VisitInfoBean>();
		AddrDao ad = new AddrDao();
		List<String> device_macs = new ArrayList<String>();// 存放此地之中的地址中的所有设备mac
		device_macs = ad.deviceList(area, addr);// 获取此地址中的所有设备mac
		System.out.println(device_macs.size());
		for (String s : device_macs)
			System.out.println("查询出的设备为：" + s);
		Random rand = new Random();
		for (int j = 0; j < page.getRows();) {// 获取page.getRows()行数据

			int m = rand.nextInt(device_macs.size());
			System.out.println("当前设备号：" + m);
			String string = "select device.device_mac,usr_mac,"
					+ "first_time,last_time,visit_times,area,addr,location.latitude,location.longtitude from location,device,"
					+ "t_visit_info_"+device_macs.get(m).replace(':', '_')
					+ " where location.location_id = device.location_id and device.device_mac = ?";
			StringBuilder sql = new StringBuilder(string);
			if (StringUtil.isNotEmpty(start_time)) {
				sql.append(" and first_time >= ? ");
			}
			if (StringUtil.isNotEmpty(end_time)) {
				sql.append(" and first_time <= ? ");
			}
			sql.append(" order by first_time desc limit " + page.getStart()
					+ rand.nextInt(500) + ",1");// 每一张表中获取一条
			Connection con = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			con = DBManager.getCon();
			System.out.println("连接成功过");
			psmt = DBManager.prepare(con, sql.toString());
			int i = 2;
			try {
				psmt.setString(1, device_macs.get(m));
				if (StringUtil.isNotEmpty(start_time)) {
					psmt.setString(i++, start_time);
				}
				if (StringUtil.isNotEmpty(end_time)) {
					psmt.setString(i++, end_time);
				}
				System.out.println("查寻开始");
				rs = psmt.executeQuery();
				System.out.println("查寻完毕");
				while (rs.next()) {
					VisitInfoBean vfb = new VisitInfoBean(rs.getString(1),
							rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getInt(5),rs.getString(6),rs.getString(7),rs.getDouble(8),rs.getDouble(9));
					list.add(vfb);
					j++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(psmt);
				DBManager.close(con);
			}
		}
		return list;
	}

	public int getVisitCount(String start_time, String end_time,
			String usr_mac, String ap_mac, String area, String addr,
			String device_name) {
		System.out.println("开始计算条数");
		System.out.println("计算条数时的时间" + start_time);
		int result = 0;
		AddrDao ad = new AddrDao();
		List<String> device_macs = new ArrayList<String>();// 存放此地之中的地址中的所有设备mac
		device_macs = ad.deviceList(area, addr);// 获取此地址中的所有设备mac
		for (int j = 0; j < device_macs.size(); j++) {
			if(!new deviceDaoImpl().isTableExits("t_visit_info_"+device_macs.get(j)))//如果不存在此表
				continue;
			StringBuilder sql = new StringBuilder(
					"select max(id),min(id) from  "
							+ device_macs.get(j).replace(':', '_')
							+ "  where id >= 1");
			if (StringUtil.isNotEmpty(start_time)) {
				sql.append(" and first_time >= ?");
			}
			if (StringUtil.isNotEmpty(end_time)) {
				sql.append(" and first_time <= ?");
			}

			Connection con = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;

			con = DBManager.getCon();
			psmt = DBManager.prepare(con, sql.toString());
			int i = 1;
			try {
				if (StringUtil.isNotEmpty(start_time)) {
					psmt.setString(i++, start_time);
				}
				if (StringUtil.isNotEmpty(end_time)) {
					psmt.setString(i++, end_time);
				}
				rs = psmt.executeQuery();
				if (rs.next())
					result += rs.getInt(1) - rs.getInt(2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(psmt);
				DBManager.close(con);
			}
		}
		System.out.println("记录数为" + result);
		return result;
	}

	public List<VisitInfoBean> getAllVisitsByNoAddrORArea(String start_time,
			String end_time, String area, String addr, PageBean page) {
		/**
		 * 1.有时间与无时间 2.有区域无地址 3.无区域，无地址
		 */
		List<VisitInfoBean> list = new ArrayList<VisitInfoBean>();
		AddrDao ad = new AddrDao();
		List<String> device_macs = new ArrayList<String>();// 存放此地之中的地址中的所有设备mac
		device_macs = ad.deviceList(area, addr);// 获取此地址中的所有设备mac
		Random rand = new Random();
		int deviceNum = device_macs.size();// 获取的设备数量
		for (int i = 0; i < page.getRows();) {// 获取page.getRows()行数据

			int m = rand.nextInt(deviceNum);
			System.out.println("当前设备号：" + m);
            if(!new deviceDaoImpl().isTableExits("t_visit_info_"+device_macs.get(m).replace(':', '_')))//判断此表是否存在
            	continue;
			String string =  "select device.device_mac,usr_mac,"
					+ "first_time,last_time,visit_times,area,addr,location.latitude,location.longtitude from location,device,"
					+ "t_visit_info_"+device_macs.get(m).replace(':', '_')
					+ " where location.location_id = device.location_id and device.device_mac = ?";
			StringBuilder sql = new StringBuilder(string);
			if (StringUtil.isNotEmpty(start_time)) {
				sql.append(" and first_time >= ? ");
			}
			if (StringUtil.isNotEmpty(end_time)) {
				sql.append(" and first_time <= ? ");
			}
			// 每张表随机获取获取一条记录
			sql.append(" order by first_time desc limit " + page.getStart()
					+ rand.nextInt(500) + ",1");
			Connection con = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			con = DBManager.getCon();
			System.out.println("连接成功过");
			psmt = DBManager.prepare(con, sql.toString());
			int j = 2;
			try {
				psmt.setString(1, device_macs.get(m));
				if (StringUtil.isNotEmpty(start_time)) {
					psmt.setString(j++, start_time);
				}
				if (StringUtil.isNotEmpty(end_time)) {
					psmt.setString(j++, end_time);
				}
				
				System.out.println("查寻开始" + sql);
				rs = psmt.executeQuery();
				System.out.println("查寻完毕");
				while (rs.next()) {
					VisitInfoBean vfb = new VisitInfoBean(rs.getString(1),
							rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getInt(5),rs.getString(6),rs.getString(7),rs.getDouble(8),rs.getDouble(9));
					list.add(vfb);
					i++;// 新增一条记录，则可以查询下一个表
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(psmt);
				DBManager.close(con);
			}

		}
		return list;
	}

}

