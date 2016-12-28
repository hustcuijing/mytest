package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wifi.model.Address;
import com.wifi.model.CollipseBean;
import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.util.DBManager;

public class GetMacCollipseImpl implements GetMacCollipse {

	public List<GetRouteBean> getcollipse(String start_time, String end_time,
			String arearaw, String addrraw, PageBean pages) {
		// TODO Auto-generated method stub
		System.out.println(start_time);
		System.out.println(end_time);
		String[] first_time = start_time.split(",");
		String[] last_time = end_time.split(",");
		String[] area = arearaw.substring(0, arearaw.length()).split(",");
		String[] addr = addrraw.substring(0, addrraw.length()).split(",");

		List<String> device_macs = new ArrayList<String>();
		Map<String, List<Visit>> map = new HashMap<String, List<Visit>>();// 以地址+开始时间+结束时间为key;以查询出的相关mac信息为value
		RetainallMac rm = new RetainallMacImpl();// 取交集
		List<Visit> list3 = new ArrayList<Visit>();
		List<GetRouteBean> lg = new ArrayList<GetRouteBean>();
		// 根据地址查询出所有设备mac
		Connection con = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		System.out.println("开始搜");
		String device_mac = "";
		con = DBManager.getCon();
		String sql1 = "select device_mac from device,location where device.location_id = location.location_id and location.area = ? and location.addr = ?";
		for (int i = 0; i < area.length; i++) {
			device_macs = new ArrayList<String>();
			try {
				pstmt1 = DBManager.prepare(con, sql1);
				pstmt1.setString(1, area[i]);
				// System.out.println("地址" + i + area[i]);
				pstmt1.setString(2, addr[i]);
				rs1 = pstmt1.executeQuery();

				while (rs1.next()) {
					device_macs.add(rs1.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				List<Visit> list = new ArrayList<Visit>();
				for (int j = 0; j < device_macs.size(); j++) {// 一个区域中的设备mac得出，则处理处理每个设备中的用户mac，这些设备的开始时间结束时间一样
					device_mac = device_macs.get(j).replace(':', '_');

					String sql2 = "select usr_mac as '用户mac',first_time,last_time,online_times,device_mac,area,addr from "
							+ device_mac
							+ " where first_time >= ? and first_time <= ?";
					pstmt2 = DBManager.prepare(con, sql2);
					try {
						pstmt2.setString(1, first_time[i]);
						pstmt2.setString(2, last_time[i]);
						rs2 = pstmt2.executeQuery();
						while (rs2.next()) {
							Visit v = new Visit();
							v.setUsr_mac(rs2.getString(1));
							v.setFirst_time(rs2.getString(2));
							v.setLast_time(rs2.getString(3));
							v.setOnline_times(rs2.getInt(4));
							v.setDevice_mac(rs2.getString(5));
							v.setArea(rs2.getString(6));
							v.setAddr(rs2.getString(7));
							if (!map.containsKey(area[i] + addr[i]+ first_time[i] + last_time[i])) {// map中还没有此设备，则需要list
								list.add(v);
								map.put(area[i] + addr[i] + first_time[i] + last_time[i], list);

							} else {
								map.get(area[i] + addr[i] + first_time[i] + last_time[i]).add(v);// 直接添加
							}
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs2);
						DBManager.close(pstmt2);
					}
				}
				DBManager.close(rs1);
				DBManager.close(pstmt1);

			}
		}
		try {
			DBManager.closeCon(con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (map.size() == 0) {
			System.out.println(map.size());
			return null;
		}
		//
		// map = new HashMap<String, List<Visit>>();
		// List<Visit> l = new ArrayList<Visit>();
		// l.add(new Visit("1", "所示", "得到", "12:13:14", "1", "3", 0));
		// l.add(new Visit("1", "所示", "得到", "12:13:15", "1", "3", 0));
		// l.add(new Visit("1", "所示", "得到", "12:13:16", "1", "3", 0));
		// l.add(new Visit("1", "所示", "得到", "12:13:17", "1", "3", 0));
		// map.put("1", l);
		// List<Visit> l2 = new ArrayList<Visit>();
		// l2.add(new Visit("2", "额额", "方法", "12:13:14", "5", "6", 0));
		// l2.add(new Visit("2", "额额", "方法", "12:13:17", "5", "6", 0));
		// map.put("2", l2);
		// l = null;
		List<String> macs = new ArrayList<String>();// 存放map中的所有地址
		for (String mac : map.keySet()) {

			macs.add(mac);
		}
		// 到此，所有的mac查询完毕，下面开始碰撞，首先碰撞出所有的mac
		if (macs != null && map.size() != 0) {
			list3 = map.get(macs.get(0));// 降低一个list列表赋给list3;
			for (int i = 1; i < map.size(); i++) {
				list3 = rm.retainMac(list3, map.get(macs.get(i)));
				if (list3 == null)
					break;
			}
		}

		// list3中存放的是在所有地址中都出现的mac
		rm.deleteMac(map, list3);
		if (list3 != null && list3.size() != 0) {
			for (int i = 0; i < list3.size(); i++) {// 处理每一个碰撞的mac
				List<Visit> lvv = new ArrayList<Visit>();// 该mac所有出现的记录
				for (String dm : map.keySet()) {// 遍历每个地址中碰撞的mac的记录
					List<Visit> uList = map.get(dm);
					for (int j = 0; j < uList.size(); j++) {
						if (uList.get(j).getUsr_mac()
								.equals(list3.get(i).getUsr_mac())) {
							lvv.add(uList.get(j));
						}// if
					}// for
				}// for到此，一个usr_mac的所有记录处理完毕

				// 接下来处理此mac的记录
				Collections.sort(lvv);// 按时间排序
				Map<String, GetRouteBean> map2 = new HashMap<String, GetRouteBean>();
				String lastdevice_mac = null;
				String laststarttime = null;
				for (Visit r : lvv) {// 处理输入，根据出现的时间不同，设别不同进行排序，在时间的基础上对设备进行排序
					if (!map2.isEmpty()) {

						if (r.getDevice_mac().equals(lastdevice_mac)) {// 如果出现的设备mac不变，则更新结束时
							map2.get(laststarttime).setEnd_time(
									r.getLast_time());
						}

						else {// 设备mac变化
							map2.put(
									r.getFirst_time(),
									new GetRouteBean(r.getUsr_mac(), r
											.getFirst_time(), r.getLast_time(),
											r.getDevice_mac(), r.getArea(), r
													.getAddr()));
							lastdevice_mac = r.getDevice_mac();
							laststarttime = r.getFirst_time();
						}

					} else {
						// 首次添加map
						map2.put(
								r.getFirst_time(),
								new GetRouteBean(r.getUsr_mac(), r
										.getFirst_time(), r.getLast_time(), r
										.getDevice_mac(), r.getArea(), r
										.getAddr()));
						lastdevice_mac = r.getDevice_mac();
						laststarttime = r.getFirst_time();

					}
				}// for
					// map2中存放的是该mac的所有记录，已经按路径处理过后的
				lvv = null;
				List<GetRouteBean> lgr = new ArrayList<GetRouteBean>();
				for (String dv : map2.keySet()) {
					lgr.add(map2.get(dv));
				}
				map2 = null;// 释放空间
				Collections.sort(lgr);
				for (GetRouteBean g : lgr) {
					lg.add(g);
				}
				lgr = null;
				lg.add(new GetRouteBean("--------", "--------", "--------",
						"--------", "--------", "--------"));// 加一个空串，用于分割
				// 到此，一个碰撞amc处理完毕
			}// for 遍历所有碰撞到的用户mac
		}// if
		else {

			System.out.println("碰撞后大大小为0");
			return null;
		}

		return lg;
	}

	public List<CollipseBean> getcollipse2(String start_time, String end_time,
			String arearaw, String addrraw, PageBean pages) {
		// TODO Auto-generated method stub
		String[] first_time = start_time.split(",");
		String[] last_time = end_time.split(",");
		String[] area = arearaw.substring(0, arearaw.length()).split(",");
		String[] addr = addrraw.substring(0, addrraw.length()).split(",");
		for (String ft : first_time) {
			System.out.println(ft);
		}
		List<String> device_macs = new ArrayList<String>();
		Map<String, List<Visit>> map = new HashMap<String, List<Visit>>();// 以地址为key;以查询出的相关mac信息为value
		RetainallMac rm = new RetainallMacImpl();// 取交集
		List<Visit> list3 = new ArrayList<Visit>();
		List<CollipseBean> listColl = new ArrayList<CollipseBean>();
		// 根据地址查询出所有设备mac
		Connection con = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;

		String device_mac = "";
		con = DBManager.getCon();
		String sql1 = "select device_mac from device,location where device.location_id = location.location_id and location.area = ? and location.addr = ?";
		for (int i = 0; i < area.length; i++) {
			device_macs = new ArrayList<String>();
			try {
				pstmt1 = DBManager.prepare(con, sql1);
				pstmt1.setString(1, area[i]);
				System.out.println("地址" + i + area[i]);
				pstmt1.setString(2, addr[i]);
				rs1 = pstmt1.executeQuery();

				while (rs1.next()) {
					device_macs.add(rs1.getString(1));
				}
				System.out.println("设备数量" + device_macs.size());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				List<Visit> list = new ArrayList<Visit>();
				for (int j = 0; j < device_macs.size(); j++) {// 一个区域中的设备mac得出，则处理处理每个设备中的用户mac，这些设备的开始时间结束时间一样
					device_mac = device_macs.get(j).replace(':', '_');

					String sql2 = "select usr_mac as '用户mac',first_time,last_time,online_times,device_mac,area,addr from "
							+ device_mac
							+ " where first_time >= ? and first_time <= ?";
					// System.out.println(sql2);
					pstmt2 = DBManager.prepare(con, sql2);
					try {
						pstmt2.setString(1, first_time[i]);
						pstmt2.setString(2, last_time[i]);
						rs2 = pstmt2.executeQuery();
						while (rs2.next()) {
							Visit v = new Visit();
							v.setUsr_mac(rs2.getString(1));
							v.setFirst_time(rs2.getString(2));
							v.setLast_time(rs2.getString(3));
							v.setOnline_times(rs2.getInt(4));
							v.setDevice_mac(rs2.getString(5));
							v.setArea(rs2.getString(6));
							v.setAddr(rs2.getString(7));
							System.out.println("当前设备地址" + device_mac + area[i]
									+ addr[i] + v.getUsr_mac());
							if (!map.containsKey(area[i] + addr[i])) {// map中还没有此设备，则需要list
								list.add(v);
								map.put(area[i] + addr[i], list);

							} else {
								map.get(area[i] + addr[i]).add(v);// 直接添加
								if (map.get(area[i] + addr[i]).size() > 700) {
									return null;
								}
							}
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs2);
						DBManager.close(pstmt2);
					}
				}
				DBManager.close(rs1);
				DBManager.close(pstmt1);

			}
		}
		try {
			DBManager.closeCon(con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(map.size());

		List<String> macs = new ArrayList<String>();// 存放map中的所有地址
		for (String mac : map.keySet()) {

			macs.add(mac);
		}

		// 到此，所有的mac查询完毕，下面开始碰撞，首先碰撞出所有的mac
		if (macs != null && map.size() != 0) {
			list3 = map.get(macs.get(0));// 降低一个list列表赋给list3;
			for (int i = 1; i < map.size(); i++) {
				list3 = rm.retainMac(list3, map.get(macs.get(i)));
				if (list3 == null)
					break;
			}
		}
		// list3中存放的是在所有地址中都出现的mac

		rm.deleteMac(map, list3);

		// map中是只剩碰撞后的mac
		Map<String, List<Visit>> userMap = new HashMap<String, List<Visit>>();// 以用户mac为key,以其详细信息为value
		for (String address : map.keySet()) {
			for (Visit v : map.get(address)) {
				String mac = v.getUsr_mac();
				if (!userMap.containsKey(mac)) {
					List<Visit> l = new ArrayList<Visit>();
					l.add(v);
					userMap.put(mac, l);
				} else
					userMap.get(mac).add(v);
			}
		}
		//
		for (String mac : userMap.keySet()) {
			Collections.sort(userMap.get(mac));
			CollipseBean clb = new CollipseBean();
			clb.setUsr_mac(mac);
			clb.setInfo("");
			for (Visit v : userMap.get(mac)) {
				String info = v.getFirst_time()
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ v.getLast_time()
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ v.getOnline_times()
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ v.getDevice_mac()
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp "
						+ v.getArea()
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ v.getAddr();
				clb.setInfo(clb.getInfo() + info + "</br>");

			}
			listColl.add(clb);

		}

		return listColl;// 碰撞出的mac
	}

	public void stopQuery() {
		// TODO Auto-generated method stub
		System.out.println("执行取消查询操作！");
		String s = "select id from information_schema.processlist where  info like '%用户mac%' ";
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;

		con = DBManager.getCon();
		pstmt = DBManager.prepare(con, s);
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("要杀死的进程" + rs.getInt(1));
				pstmt1 = DBManager.prepare(con, "kill " + rs.getInt(1));
				pstmt1.execute();
				DBManager.close(pstmt1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(pstmt);
			DBManager.close(con);
		}
		System.out.println("取消操作执行完毕！");
	}

	public List<GetRouteBean> getcollipseNew1(String start_time,
			String end_time, String arearaw, String addrraw, PageBean pages) {
		// TODO Auto-generated method stub
		// 解析出开始时间，结束时间,区域,地址,,去掉最后一个逗号后进行分割
		System.out.println(start_time + "&" + end_time);
		String[] first_time = start_time.trim().substring(0, start_time.length() - 1).split(",");
		String[] last_time = end_time.trim().substring(0, end_time.length() - 1).split(",");
		String[] area = arearaw.substring(0, arearaw.length() - 1).split(",");
		String[] addr = addrraw.substring(0, addrraw.length() - 1).split(",");

		List<GetRouteBean> lg = new ArrayList<GetRouteBean>();// 存放结果
		Map<String, List<Visit>> map = new HashMap<String, List<Visit>>();// 以地址为key;以查询出的相关mac信息为value
		Set<String> usr_macs = new HashSet<String>();// 存放每一步碰撞后所剩的用户mac
		// 获取所有设备mac
		AddrDao dd = new AddrDao();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		System.out.println("开始搜索！");
		// 处理第一个区域地址内的符合条件的用户mac
		List<String> device_mac = new ArrayList<String>();
		device_mac = dd.deviceList(area[0], addr[0]);
		if (device_mac == null || device_mac.size() == 0)
			return null;// 找不到设备，则返回空，没有符合条件的数据
		for (String device : device_mac) {
			List<Visit> list = new ArrayList<Visit>();// 存放此设备中的用户mac信息
			String sql = "select usr_mac,first_time,last_time,online_times,device_mac,area,addr from "
					+ device.replace(':', '_') + " where first_time >= ? and first_time <= ?";
			System.out.println(sql);
			System.out.println(first_time[0] + ":" + last_time[0]);
			con = DBManager.getCon();
			pstmt = DBManager.prepare(con, sql);
			try {
				pstmt.setString(1, first_time[0]);
				pstmt.setString(2, last_time[0]);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Visit v = new Visit();
					v.setUsr_mac(rs.getString(1));
					v.setFirst_time(rs.getString(2));
					v.setLast_time(rs.getString(3));
					v.setOnline_times(rs.getInt(4));
					v.setDevice_mac(rs.getString(5));
					v.setArea(rs.getString(6));
					v.setAddr(rs.getString(7));
					if (!map.containsKey(area[0] + addr[0] + first_time[0] + last_time[0])) {// map中还没有此设备，则需要list
						list.add(v);
						map.put(area[0] + addr[0] + first_time[0] + last_time[0], list);
						usr_macs.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
					} else {
						map.get(area[0] + addr[0] + first_time[0] + last_time[0]).add(v);// 直接添加
						usr_macs.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
					}
				}// while 对查询出的结果进行处理
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(pstmt);
				DBManager.close(con);
			}
		}// for，循环所有此区域地址内的设备表

		if (usr_macs.size() == 0)// 若在第一个地址内没有找到符合条件的用户mac，返回空
			return null;

		// 处理剩下的区域
		for (int i = 1; i < area.length; i++) {
			device_mac = dd.deviceList(area[i], addr[i]);
			if (device_mac == null || device_mac.size() == 0)
				return null;// 找不到设备，则返回空，没有符合条件的数据
			Set<String> usr_macsT = new HashSet<String>();// 存放每一区域碰撞后所剩的用户mac
			List<String> sqls = getSqlString(usr_macs, 1000);// 10个为一组
			for (String device : device_mac) {//此区域内的所有设备
				if (sqls.size() == 0)
					return null;// sql语句不符合，返回空
				sqls = null;
				List<Visit> list = new ArrayList<Visit>();// 存放此设备中的用户mac信息
				String sql = "select usr_mac,first_time,last_time,online_times,device_mac,area,addr from "
						+ device.replace(':', '_')
//						+ " where first_time >= ? and first_time <= ? and usr_mac in (";
				+ " where first_time >= ? and first_time <= ?";
//				for (String sqlm : sqls) {// 对此设备循环处理每一条sql
					con = DBManager.getCon();
//					pstmt = DBManager.prepare(con, sql + sqlm + ")");
					pstmt =DBManager.prepare(con, sql);
//					System.out.println(sql + sqlm + ")");
					try {
						pstmt.setString(1, first_time[i]);
						pstmt.setString(2, last_time[i]);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Visit v = new Visit();
							v.setUsr_mac(rs.getString(1));
							v.setFirst_time(rs.getString(2));
							v.setLast_time(rs.getString(3));
							v.setOnline_times(rs.getInt(4));
							v.setDevice_mac(rs.getString(5));
							v.setArea(rs.getString(6));
							v.setAddr(rs.getString(7));
							
							if (!map.containsKey(area[i] + addr[i] + first_time[i] + last_time[i])) {// map中还没有此设备，则需要list
								list.add(v);
								map.put(area[i] + addr[i] + first_time[i] + last_time[i], list);
								usr_macsT.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
							} else {
								map.get(area[i] + addr[i] + first_time[i] + last_time[i]).add(v);// 直接添加
								usr_macsT.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
							}
						}// while 对查询出的结果进行处理
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs);
						DBManager.close(pstmt);
						DBManager.close(con);
					}
//				}// 循环处理所有sql		
			}// 遍历此区域内的所有设备表
			// 处理完莫伊区域后，取usr_macs,usr_macT的交集，并将map中所有不存在交集用户mac中的用户mac记录删除
			usr_macs.retainAll(usr_macsT);// 得到交集
			usr_macsT = null;
			getUsr_macs(usr_macs, map);// 更新map
		}//处理所有区域
		
		System.out.println("map的大小为" + map.size());
		System.out.println("碰撞出的mac有" + usr_macs.size());
		//将结果处理为RouteBean形式
		List<String> list3 = new ArrayList<String>(usr_macs);
		usr_macs = null;
		if (list3 != null && list3.size() != 0) {
			for (int i = 0; i < list3.size(); i++) {// 处理每一个碰撞的mac
				List<Visit> lvv = new ArrayList<Visit>();// 该mac所有出现的记录
				for (String dm : map.keySet()) {// 遍历每个地址中碰撞的mac的记录
					List<Visit> uList = map.get(dm);
					for (int j = 0; j < uList.size(); j++) {
						if (uList.get(j).getUsr_mac()
								.equals(list3.get(i))) {
							lvv.add(uList.get(j));
						}// if
					}// for
				}// for到此，一个usr_mac的所有记录处理完毕

				// 接下来处理此mac的记录
				Collections.sort(lvv);// 按时间排序
				Map<String, GetRouteBean> map2 = new HashMap<String, GetRouteBean>();
				String lastdevice_mac = null;
				String laststarttime = null;
				for (Visit r : lvv) {// 处理输入，根据出现的时间不同，设别不同进行排序，在时间的基础上对设备进行排序
					if (!map2.isEmpty()) {

						if (r.getDevice_mac().equals(lastdevice_mac)) {// 如果出现的设备mac不变，则更新结束时
							map2.get(laststarttime).setEnd_time(
									r.getLast_time());
						}

						else {// 设备mac变化
							map2.put(
									r.getFirst_time(),
									new GetRouteBean(r.getUsr_mac(), r
											.getFirst_time(), r.getLast_time(),
											r.getDevice_mac(), r.getArea(), r
													.getAddr()));
							lastdevice_mac = r.getDevice_mac();
							laststarttime = r.getFirst_time();
						}

					} else {
						// 首次添加map
						map2.put(
								r.getFirst_time(),
								new GetRouteBean(r.getUsr_mac(), r
										.getFirst_time(), r.getLast_time(), r
										.getDevice_mac(), r.getArea(), r
										.getAddr()));
						lastdevice_mac = r.getDevice_mac();
						laststarttime = r.getFirst_time();

					}
				}// for
					// map2中存放的是该mac的所有记录，已经按路径处理过后的
				lvv = null;
				List<GetRouteBean> lgr = new ArrayList<GetRouteBean>();
				for (String dv : map2.keySet()) {
					lgr.add(map2.get(dv));
				}
				map2 = null;// 释放空间
				Collections.sort(lgr);
				for (GetRouteBean g : lgr) {
					lg.add(g);
				}
				lgr = null;
				lg.add(new GetRouteBean("--------", "--------", "--------",
						"--------", "--------", "--------"));// 加一个空串，用于分割
				// 到此，一个碰撞amc处理完毕
			}// for 遍历所有碰撞到的用户mac
		}// if
		else {

			System.out.println("碰撞后大大小为0");
			return null;
		}
		return lg;
	}

	public static List<String> getSqlString(Set<String> usr_macs, int size) {
		List<String> sqls = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		if (usr_macs.size() <= size) {
			for (String um : usr_macs)
				sb.append("'" + um + "'"  + ",");
			sqls.add(sb.toString().substring(0, sb.toString().length() - 1));// 去掉最后一个逗号添加到sqls
			return sqls;
		} else {
			int m = usr_macs.size() / size;
			Iterator<String> it = usr_macs.iterator();
			for (int i = 0; i < m; i++) {// m个sql串
				sb = new StringBuilder();
				for (int j = 0; j < size; j++)
					sb.append("'" + it.next() + "'" + ",");// 每个串添加size个用户mac
				if(sb.length() > 0)
				  sqls.add(sb.toString().substring(0, sb.toString().length() - 1));// 去掉最后一个逗号添加到sqls
			}
			if (m * size < usr_macs.size()) {// 添加剩下的
				sb = new StringBuilder();
				for (int j = m * size; j < size; j++)
					sb.append("'" + it.next() + "'" + ",");// 每个串添加size个用户mac
				if(sb.length() > 0)
				  sqls.add(sb.toString().substring(0, sb.toString().length() - 1));// 去掉最后一个逗号添加到sqls
			}
		}
		return sqls;
	}

	public static void getUsr_macs(Set<String> usr_macs,
			Map<String, List<Visit>> map) {
		/**
		 * 将map中所有不存在交集用户mac中的用户mac记录删除
		 */
		for (String aa : map.keySet()) {
			List<Visit> list = map.get(aa);
			Iterator<Visit> iter = list.iterator();
			while (iter.hasNext()) {
				if (!usr_macs.contains(iter.next().getUsr_mac())) {// 不包括
					iter.remove();// 删除此元素
				}
			}
		}
	}

	public List<GetRouteBean> getcollipseNew2(String start_time,
			String end_time, String arearaw, String addrraw, PageBean pages) {
		// TODO Auto-generated method stub
		System.out.println(start_time + "&" + end_time);
		String[] first_time = start_time.trim().substring(0, start_time.length() - 1).split(",");
		String[] last_time = end_time.trim().substring(0, end_time.length() - 1).split(",");
		String[] area = arearaw.substring(0, arearaw.length() - 1).split(",");
		String[] addr = addrraw.substring(0, addrraw.length() - 1).split(",");

		List<GetRouteBean> lg = new ArrayList<GetRouteBean>();// 存放结果
		Map<String, List<Visit>> map = new HashMap<String, List<Visit>>();// 以地址为key;以查询出的相关mac信息为value
		Set<String> usr_macs = new HashSet<String>();// 存放每一步碰撞后所剩的用户mac
		Map<String,Address> dmap = new deviceDaoImpl().getDeviceAddress();//设备为key,地址信息为value
		// 获取所有设备mac
		AddrDao dd = new AddrDao();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		System.out.println("开始搜索！");
		// 处理第一个区域地址内的符合条件的用户mac
		List<String> device_mac = new ArrayList<String>();
		device_mac = dd.deviceList(area[0], addr[0]);
		if (device_mac == null || device_mac.size() == 0)
			return null;// 找不到设备，则返回空，没有符合条件的数据
		for (String device : device_mac) {
			if(!new deviceDaoImpl().isTableExits("t_visit_info_" + device.replace(':', '_')))//此表不存在
				continue;
			List<Visit> list = new ArrayList<Visit>();// 存放此设备中的用户mac信息
			String sql = "select usr_mac,first_time,last_time,visit_times,device_mac from "
					+ "t_visit_info_" + device.replace(':', '_') + " where first_time >= ? and first_time <= ?";
			System.out.println(sql);
			System.out.println(first_time[0] + "|" + last_time[0]);
			con = DBManager.getCon();
			pstmt = DBManager.prepare(con, sql);
			try {
				pstmt.setString(1, first_time[0]);
				pstmt.setString(2, last_time[0]);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Visit v = new Visit();
					v.setUsr_mac(rs.getString(1));
					v.setFirst_time(rs.getString(2));
					v.setLast_time(rs.getString(3));
					v.setOnline_times(rs.getInt(4));
					v.setDevice_mac(rs.getString(5));
					v.setArea(dmap.get(device).getArea());
					v.setAddr(dmap.get(device).getAddr());
					v.setArads_timee_time(area[0] + addr[0] + first_time[0] + last_time[0]);
					if (!map.containsKey(area[0] + addr[0] + first_time[0] + last_time[0])) {// map中还没有此设备，则需要list
						list.add(v);
						map.put(area[0] + addr[0] + first_time[0] + last_time[0], list);
						usr_macs.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
					} else {
						map.get(area[0] + addr[0] + first_time[0] + last_time[0]).add(v);// 直接添加
						usr_macs.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
					}
				}// while 对查询出的结果进行处理
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(pstmt);
				DBManager.close(con);
			}
		}// for，循环所有此区域地址内的设备表

		if (usr_macs.size() == 0)// 若在第一个地址内没有找到符合条件的用户mac，返回空
			return null;

		// 处理剩下的区域
		for (int i = 1; i < area.length; i++) {
			device_mac = dd.deviceList(area[i], addr[i]);
			if (device_mac == null || device_mac.size() == 0)
				return null;// 找不到设备，则返回空，没有符合条件的数据
			Set<String> usr_macsT = new HashSet<String>();// 存放每一区域碰撞后所剩的用户mac
			for (String device : device_mac) {//此区域内的所有设备
				if(!new deviceDaoImpl().isTableExits("t_visit_info_"+ device.replace(':', '_')))
					continue;
				List<Visit> list = new ArrayList<Visit>();// 存放此设备中的用户mac信息
				String sql = "select usr_mac,first_time,last_time,visit_times,device_mac from t_visit_info_"
						+ device.replace(':', '_')
//						+ " where first_time >= ? and first_time <= ? and usr_mac in (";
				+ " where first_time >= ? and first_time <= ?";
//				for (String sqlm : sqls) {// 对此设备循环处理每一条sql
					con = DBManager.getCon();
//					pstmt = DBManager.prepare(con, sql + sqlm + ")");
					pstmt =DBManager.prepare(con, sql);
//					System.out.println(sql + sqlm + ")");
					try {
						pstmt.setString(1, first_time[i]);
						pstmt.setString(2, last_time[i]);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Visit v = new Visit();
							v.setUsr_mac(rs.getString(1));
							v.setFirst_time(rs.getString(2));
							v.setLast_time(rs.getString(3));
							v.setOnline_times(rs.getInt(4));
							v.setDevice_mac(rs.getString(5));
							v.setArea(dmap.get(device).getArea());
							v.setAddr(dmap.get(device).getAddr());
							v.setArads_timee_time(area[i] + addr[i] + first_time[i] + last_time[i]);
							if (!map.containsKey(area[i] + addr[i] + first_time[i] + last_time[i])) {// map中还没有此设备，则需要list
								list.add(v);
								map.put(area[i] + addr[i] + first_time[i] + last_time[i], list);
								usr_macsT.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
							} else {
								map.get(area[i] + addr[i] + first_time[i] + last_time[i]).add(v);// 直接添加
								usr_macsT.add(v.getUsr_mac());// 添加到碰撞到的用户mac中
							}
						}// while 对查询出的结果进行处理
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs);
						DBManager.close(pstmt);
						DBManager.close(con);
					}
//				}// 循环处理所有sql		
			}// 遍历此区域内的所有设备表
			// 处理完莫伊区域后，取usr_macs,usr_macT的交集，并将map中所有不存在交集用户mac中的用户mac记录删除
			usr_macs.retainAll(usr_macsT);// 得到交集
			usr_macsT = null;
			getUsr_macs(usr_macs, map);// 更新map
		}//处理所有区域
		
		System.out.println("map的大小为" + map.size());
		System.out.println("碰撞出的mac有" + usr_macs.size());
		//将结果处理为RouteBean形式
		List<String> list3 = new ArrayList<String>(usr_macs);
		usr_macs = null;
		/**
		 *将每个key中的mac信息进行压缩为GetRouteBean形式
		 */
		if (list3 != null && list3.size() != 0) {
			for (int i = 0; i < list3.size(); i++) {// 处理每一个碰撞的mac
				List<Visit> lvv = new ArrayList<Visit>();// 该mac所有出现的记录
				for (String dm : map.keySet()) {// 遍历每个地址中碰撞的mac的记录
					List<Visit> uList = map.get(dm);
					for (int j = 0; j < uList.size(); j++) {
						if (uList.get(j).getUsr_mac()
								.equals(list3.get(i))) {
							lvv.add(uList.get(j));
						}// if
					}// for
				}// for到此，一个usr_mac的所有记录处理完毕

				// 接下来处理此mac的记录
				Collections.sort(lvv);// 按时间排序
				Map<String, GetRouteBean> map2 = new HashMap<String, GetRouteBean>();
				String lastdevice_mac = null;
				String laststarttime = null;
				for (Visit r : lvv) {// 处理输入，根据出现的时间不同，设别不同进行排序，在时间的基础上对设备进行排序
					if (!map2.isEmpty()) {

						if (r.getArads_timee_time().equals(lastdevice_mac)) {// 如果出现的设备mac不变，则更新结束时
							map2.get(laststarttime).setEnd_time(
									r.getLast_time());
						}

						else {// 设备mac变化
							map2.put(
									r.getFirst_time(),
									new GetRouteBean(r.getUsr_mac(), r
											.getFirst_time(), r.getLast_time(),
											r.getDevice_mac(), r.getArea(), r
													.getAddr()));
							lastdevice_mac = r.getArads_timee_time();
							laststarttime = r.getFirst_time();
						}

					} else {
						// 首次添加map
						map2.put(
								r.getFirst_time(),
								new GetRouteBean(r.getUsr_mac(), r
										.getFirst_time(), r.getLast_time(), r
										.getDevice_mac(), r.getArea(), r
										.getAddr()));
						lastdevice_mac = r.getArads_timee_time();
						laststarttime = r.getFirst_time();

					}
				}// for
					// map2中存放的是该mac的所有记录，已经按路径处理过后的
				lvv = null;
				List<GetRouteBean> lgr = new ArrayList<GetRouteBean>();
				for (String dv : map2.keySet()) {
					lgr.add(map2.get(dv));
				}
				map2 = null;// 释放空间
				Collections.sort(lgr);
				for (GetRouteBean g : lgr) {
					lg.add(g);
				}
				lgr = null;
				lg.add(new GetRouteBean("--------", "--------", "--------",
						"--------", "--------", "--------"));// 加一个空串，用于分割
				// 到此，一个碰撞mac处理完毕
			}// for 遍历所有碰撞到的用户mac
		}// if
		else {
			System.out.println("碰撞后大大小为0");
			return null;
		}
		return lg;
	}
}
