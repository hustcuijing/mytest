package com.wifi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JSpinner.DateEditor;

import com.wifi.dao.GroupMemberDaoImpl;
import com.wifi.dao.deviceDaoImpl;
import com.wifi.dao.UsrMacDaoImpl;
import com.wifi.model.Address;
import com.wifi.model.T_UserMac;
import com.wifi.model.UserTimeDevice;
import com.wifi.model.UsrMacInfo;

public class DataUtil {

	public static List<UsrMacInfo> getInfos(String usr_mac, String start_time,
			String end_time) {

		UsrMacDaoImpl um = new UsrMacDaoImpl();
		deviceDaoImpl dd = new deviceDaoImpl();

		Map<Integer, String> device_index = dd.getDeviceByIndex();// key为设备序号，value为设备mac
		Map<String, Address> device_addr = dd.getDeviceAddress();// key为mac，value为该设备对应的地址信息

		T_UserMac t_UserMac = um.getRecordByUsrmac(usr_mac);// 获取此用户mac对应的一跳数据

		// 将今天的写入到压缩串中，时间和设备
		String _time = StringUtil.addTwoStrings(t_UserMac.getTime_compress(),
				StringUtil.compress(t_UserMac.getCur_time_compress()));
		String _device = StringUtil.addTwoStrings(
				t_UserMac.getDevice_compress(),
				t_UserMac.getCur_device_compress());

		String[] dayTime = _time.split("z");// 将压缩时间分解
		String devcieCopmress = _device;// 压缩的设备名

		List<UsrMacInfo> result = new ArrayList<UsrMacInfo>();// 结果
		String UserEndTime = DateUtil.addDateWithMinute(
				t_UserMac.getLast_time(), 1440);// 更新记录的最后时间，因为当天也抓到了数据

		int start_internal = DateUtil.getInternalDay(t_UserMac.getStart_time(),
				start_time);// 后者比前者大几天
		// System.out.println("开始时间比记录的开始时间大" + start_internal+"天");
		int start_last_internal = start_time.compareTo(UserEndTime);// 开始时间是否小于设备记录的结束时间

		int end_internal = DateUtil.getInternalDay(UserEndTime, end_time);// 结束时间与给的结束时间差几天
		System.out.println("该用户的开始时间" + t_UserMac.getStart_time());
		int end_first_internal = end_time.compareTo(t_UserMac.getStart_time());// 结束时间是否小于设备的开始时间

		String st = start_time;// 默认开始时间
		String et = end_time;
		int deviceIndex = 0;// 设备默认开始地址
		int startIndex = 0;// 默认时间开始地址
		int seTime = 0;

		if (start_last_internal > 0 || end_first_internal < 0) {// 时间超出范围
			return null;
		} else {
			if (start_internal < 0) {// 后者比前者小
				st = t_UserMac.getStart_time();
			} else {
				// 更新设备开始地址
				deviceIndex = StringUtil.getDevcieIndex(
						t_UserMac.getTime_compress(), start_internal) * 4;
				System.out.println("设备开始指针" + deviceIndex);
				// 更新时间开始index
				startIndex = start_internal;
			}
			if (end_internal > 0) {// 后者比前者大
				et = UserEndTime;// 默认结束时间
			}

			seTime = DateUtil.getInternalDay(st, et);
			// 至此，开始时间，结束时间，应该开始的位置均已获得，以下解压缩字符串
			// System.out.println(st + "时间" + et + "开始时间与结束时间差"+seTime);

			for (int i = 0; i <= seTime; i++) {
				if (i != 0)// 重新开始计算一天
					st = DateUtil.addDateWithMinute(st, 1440);// 加一天

				String sp = dayTime[i + startIndex + 1];// 获得当前天的压缩时间串，z使得数组第一个值为空串
				System.out.println("当天压缩时间串" + sp);

				if (sp.length() == 3)// 表明今天无记录
					continue;

				for (int j = 3; j < sp.length(); j += 4) {// 从第一条数据开始，轮询现在天数的每一个记录
					String record_time = DateUtil.addDateWithMinute(
							st,
							Integer.valueOf(String.valueOf(sp.charAt(j))
									+ String.valueOf(sp.charAt(j + 1))
									+ String.valueOf(sp.charAt(j + 2))
									+ String.valueOf(sp.charAt(j + 3))));// i *
																			// 1440表示隔了几天

					if (record_time.compareTo(et) <= 0
							&& record_time.compareTo(st) >= 0) {// 在给定的时间范围内
						int deviceId = Integer.valueOf(String
								.valueOf(devcieCopmress.charAt(deviceIndex))
								+ String.valueOf(devcieCopmress
										.charAt(deviceIndex + 1))
								+ String.valueOf(devcieCopmress
										.charAt(deviceIndex + 2))
								+ String.valueOf(devcieCopmress
										.charAt(deviceIndex + 3)));
						// System.out.print("设备编号" + deviceId);
						String device_mac = device_index.get(deviceId);
						// System.out.println("设备编号" + device_mac);
						Address ad = device_addr.get(device_mac);
						// System.out.println("设备地址" + ad.getAddr() +
						// ad.getArea());
						UsrMacInfo u = new UsrMacInfo(usr_mac, record_time,
								device_mac, ad.getArea(), ad.getAddr(),
								ad.getLongtitude(), ad.getLatitude());
						result.add(u);

					}
					deviceIndex += 4;// 无论此日期是否在范围内，都算作处理了一个日期,更新设备开始的index
				}// 一天的数据处理完毕
			}

		}
		return result;
	}

	/**
	 * 在时间字符串前面加零
	 * 
	 * @param time
	 * @param day
	 * @return
	 */
	public static String addBeforeTime(String time, int day) {
		StringBuilder sb = new StringBuilder();
		String add = "000405a0";
		for (int i = 0; i < day; i++) {
			sb.append(add);
		}
		sb.append(time);
		return sb.toString();
	}

	/**
	 * 在时间字符串后面加零
	 * 
	 * @param time
	 * @param day
	 * @return
	 */
	public static String addAfterTime(String time, int day) {
		StringBuilder sb = new StringBuilder(time);
		String add = "000405a0";
		for (int i = 0; i < day; i++) {
			sb.append(add);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		List<UsrMacInfo> all = getInfos("ff:ff:ff:ff:ff:ff",
				"2016-01-01 00:00:00", "2016-01-04 00:00:00");
		for (UsrMacInfo usrMacInfo : all) {
			System.out.println(usrMacInfo);
		}
	}

	public static List<UsrMacInfo> getInfos2(String start_time,
			String end_time, String usr_mac) {
		if (start_time.compareTo(end_time) >= 0)
			return null;
		List<UsrMacInfo> l = new ArrayList<UsrMacInfo>();// 存放地址信息
		int sYear = Integer.parseInt(start_time.substring(0, 4));// 获取开始的年份
		int sMonth = Integer.parseInt(start_time.substring(5, 7));// 获取开始的月份
		int sDay = Integer.parseInt(start_time.substring(8, 10));// 获取开始的天数
		String sTime = start_time.trim().substring(11);// 开始时间

		int eYear = Integer.parseInt(end_time.substring(0, 4));// 获取结束的年份
		int eMonth = Integer.parseInt(end_time.substring(5, 7));// 获取结束的月份
		int eDay = Integer.parseInt(end_time.substring(8, 10));// 获取结束的天数
		String eTime = end_time.trim().substring(11);// 结束时间

		System.out.println(sMonth + "结束月份" + eMonth);
		UsrMacDaoImpl um = new UsrMacDaoImpl();
		deviceDaoImpl dd = new deviceDaoImpl();

		Map<String, String> name_mac = dd.getDeviceByName();// key为设备名，value为设备mac
		Map<String, Address> device_addr = dd.getDeviceAddress();// key为mac，value为该设备对应的地址信息

		// 找出需要进行数据查询的t_usr_compress表
		List<String> list = new ArrayList<String>();// 存放每一个需要进行查询的表名
		if (eYear > sYear) {// 不在同一年
			String month;
			String day;
			if (sDay < 10)
				day = "0" + sDay;
			else
				day = String.valueOf(sDay);
			for (int i = sMonth; i <= 12; i++) { // 添加第一年
				if (i < 10)
					month = "0" + i;
				else
					month = String.valueOf(i);
				list.add("_" + sYear + "_" + month);
			}
			for (int i = sYear + 1; i <= eYear - 1; i++) {// 添加中间的表
				for (int j = 1; j <= 12; j++) {
					if (j < 10)
						month = "0" + j;
					else
						month = String.valueOf(j);
					list.add("_" + i + "_" + month);
				}
			}
			for (int i = 1; i <= eMonth; i++) { // 添加最后一年
				if (i < 10)
					month = "0" + i;
				else
					month = String.valueOf(i);
				list.add("_" + eYear + "_" + month);
			}
		}
		if (eYear == sYear) {// 在同一年,直接添加月份
			String month;
			for (int i = sMonth; i <= eMonth; i++) {
				if (i < 10)
					month = "0" + i;
				else
					month = String.valueOf(i);
				list.add("_" + sYear + "_" + month);
			}
		}// 所有需要查找的表添加完毕
		System.out.println("轨迹需查询的表的大小为" + list.size());
		// list中的第一个总是开始时间为天数，时间；最后一个总是结束时间为天数，时间；如果只有一个时间，则说明在同一个月，那么开始天数，结束天数都在这个月
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		// 首先判断只有一个的情况，即list的大小为1
		if (list.size() == 1) {
			System.out.println("查询出的表名为" + list.get(0));
			try {
				con = DBManager.getCon();
				rs = con.getMetaData().getTables(null, null,
						"t_usr_mac_compress" + list.get(0), null);
				if (rs == null){// 即不存在此表
					System.out.println("不存在表 t_usr_mac_compress" + list.get(0));
					return null;// 返回为空
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(con);
			}
			int m = DateUtil.getMinutes(sTime);// 获取开始时是第几分钟
			int n = DateUtil.getMinutes(eTime);// 获取结束时是第几分钟
			System.out.println("开始分钟：" + m + "//结束分钟： " + n);
			// 若存在此表，则找到表，接下来计算日其
			sql = "select day,device_compress from t_usr_mac_compress"
					+ list.get(0)
					+ " where day >= ? and day<= ? and usr_mac = ?";// 将该天设备串读出
			con = DBManager.getCon();
			pstmt = DBManager.prepare(con, sql);

			try {
				pstmt.setInt(1, sDay);
				pstmt.setInt(2, eDay);
				pstmt.setString(3, usr_mac);
				rs = pstmt.executeQuery();
				if (sDay == eDay && rs.next()) {// 则只查出一条数据
					String device_compress =rs.getString(2);
					if (m > n)// 在一天，且开始时间大于结束时间
						return null;
					if(device_compress.equals(""))
						return null;
					device_compress = device_compress.substring(m * 4,
							n * 4);// 在一天的截取设备字符串
					for (int i = 0, j = 0; i <= device_compress.length()
							&& j < device_compress.length(); i++) {
						if (!device_compress.substring(j, j + 4).contains("x")) {// 设备不为xxxx,则表示出现
							String day;
							if (rs.getInt(1) < 10)
								day = "0" + rs.getInt(1);
							else
								day = "" + rs.getInt(1);
							String record_time = DateUtil.addDateWithMinute(
									list.get(0).substring(1, 5) + "-"
											+ list.get(0).substring(6) + "-"
											+ day + " 00:00:00", i + m);// 计算记录时间
							 String deviceId =device_compress
									.substring(j, j + 4);// 获取设备号
							String device_mac = name_mac.get(deviceId);//获取设备mac
							Address ad = device_addr.get(device_mac);
							UsrMacInfo u = new UsrMacInfo(usr_mac, record_time,
									device_mac, ad.getArea(), ad.getAddr(),
									ad.getLongtitude(), ad.getLatitude());
							l.add(u);
							j += 4;
						} else
							j += 4;

					}
				} else {// 多条数据
					System.out.println("有多条数据");
					while (rs.next()) {
						if (rs.getInt(1) == sDay) {// 是第一天的数据
							System.out.println("第" + sDay + "天");
							String device_compress = rs.getString(2).substring(
									m * 4);// 从开始的分钟开始截取
							for (int i = 0, j = 0; i <= device_compress
									.length() && j < device_compress.length(); i++) {
								if (!device_compress.substring(j, j + 4)
										.contains("x")) {// 设备不为xxxx,则表示出现
									String day;
									if (rs.getInt(1) < 10)
										day = "0" + rs.getInt(1);
									else
										day = "" + rs.getInt(1);
									String record_time = DateUtil
											.addDateWithMinute(list.get(0)
													.substring(1, 5)
													+ "-"
													+ list.get(0).substring(6)
													+ "-" + day + " 00:00:00",
													i + m);// 计算记录时间
									String deviceId =device_compress
											.substring(j, j + 4);// 获取设备号
									String device_mac = name_mac.get(deviceId);//获取设备mac
									Address ad = device_addr.get(device_mac);
									UsrMacInfo u = new UsrMacInfo(usr_mac,
											record_time, device_mac,
											ad.getArea(), ad.getAddr(),
											ad.getLongtitude(),
											ad.getLatitude());
									l.add(u);
									j += 4;
								} else
									j += 4;

							}

						}// 第一天计算
						else if (rs.getInt(1) == eDay) {// 是最后一天的数据
							String device_compress = rs.getString(2).substring(
									0, n * 4);// 从第一分钟截取到最后一分钟
							for (int i = 0, j = 0; i <= device_compress
									.length() && j < device_compress.length(); i++) {
								if (!device_compress.substring(j,j + 4)
										.contains("x")) {// 设备不为xxxx,则表示出现
									String day;
									if (rs.getInt(1) < 10)
										day = "0" + rs.getInt(1);
									else
										day = "" + rs.getInt(1);
									String record_time = DateUtil
											.addDateWithMinute(list.get(0)
													.substring(1, 5)
													+ "-"
													+ list.get(0).substring(6)
													+ "-" + day + " 00:00:00",
													i + 1);
									String deviceId =device_compress
											.substring(j, j + 4);// 获取设备号
									String device_mac = name_mac.get(deviceId);//获取设备mac
									Address ad = device_addr.get(device_mac);
									UsrMacInfo u = new UsrMacInfo(usr_mac,
											record_time, device_mac,
											ad.getArea(), ad.getAddr(),
											ad.getLongtitude(),
											ad.getLatitude());
									l.add(u);
									j += 4;
								} else
									// 不出现
									j += 4;
							}
						}// 最后一天计算
						else {
							// 中间天数直接计算
							System.out.println("第" + rs.getInt(1) + "天");
							String device_compress = rs.getString(2);
							for (int i = 0, j = 0; i <= device_compress
									.length() && j < device_compress.length(); i++) {
								if (!device_compress.substring(j, j + 4)
										.contains("x")) {// 设备不为xxxx,则表示出现
									String day;
									if (rs.getInt(1) < 10)
										day = "0" + rs.getInt(1);
									else
										day = "" + rs.getInt(1);
									String record_time = DateUtil
											.addDateWithMinute(list.get(0)
													.substring(1, 5)
													+ "-"
													+ list.get(0).substring(6)
													+ "-" + day + " 00:00:00",
													i + 1);
									String deviceId =device_compress
											.substring(j, j + 4);// 获取设备号
									String device_mac = name_mac.get(deviceId);//获取设备mac
									Address ad = device_addr.get(device_mac);
									UsrMacInfo u = new UsrMacInfo(usr_mac,
											record_time, device_mac,
											ad.getArea(), ad.getAddr(),
											ad.getLongtitude(),
											ad.getLatitude());
									l.add(u);
									j += 4;
								} else
									j += 4;
							}
						}// 中间天数计算
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(pstmt);
				DBManager.close(con);
			}

		}// 只有一个表的情况处理完毕

		else {// 处理有多个表的情况 ，即list的size不为1，分为开始表，结束表和中间表
				// 首先算出开始表与结束表
			String stable = "_" + sYear + "_" + sMonth;
			String etable = "_" + eYear + "_" + eMonth;

			for (String table : list) {
				try {
					con = DBManager.getCon();
					rs = con.getMetaData().getTables(null, null,
							"t_usr_mac_compress" + table, null);
					if (rs == null)// 即不存在此表
						continue;// 寻找下一个表
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (rs == null)
						DBManager.close(rs);
					DBManager.close(con);
				}
				// 找到表
				// 1.若为开始表，即开始时间在此表
				if (table.equals(stable)) {
					int m = DateUtil.getMinutes(sTime);// 获取开始时是第几分钟
					int n = DateUtil.getMinutes(eTime);// 获取结束时时是第几分钟
					// 若存在此表，则找到表，接下来计算日其
					sql = "select day,device_compress from "
							+ "t_usr_mac_compress" + table
							+ " where day >= ? and usr_mac = ?";// 将该天的时间压缩，设备压缩读出
					con = DBManager.getCon();
					pstmt = DBManager.prepare(con, sql);
					try {
						pstmt.setInt(1, sDay);
						pstmt.setString(2, usr_mac);
						rs = pstmt.executeQuery();
						while (rs.next()) {// 轮询查询出的结果
							if (rs.getInt(1) == sDay) {// 是第一天的数据
								String device_compress = rs.getString(2)
										.substring(m * 4);
								for (int i = 0, j = 0; i <= device_compress
										.length()
										&& j < device_compress.length(); i++) {
									if (!device_compress.substring(j, j + 4)
											.contains("x")) {// 设备不为xxxx,则表示出现
										String day;
										if (rs.getInt(1) < 10)
											day = "0" + rs.getInt(1);
										else
											day = "" + rs.getInt(1);
										String record_time = DateUtil
												.addDateWithMinute(
														table.substring(1, 5)
																+ "-"
																+ table.substring(6)
																+ "-" + day
																+ " 00:00:00",
														i + m);
										String deviceId =device_compress
												.substring(j, j + 4);// 获取设备号
										String device_mac = name_mac.get(deviceId);//获取设备mac
										Address ad = device_addr
												.get(device_mac);
										UsrMacInfo u = new UsrMacInfo(usr_mac,
												record_time, device_mac,
												ad.getArea(), ad.getAddr(),
												ad.getLongtitude(),
												ad.getLatitude());
										l.add(u);
										j += 4;
									} else
										j += 4;
								}

							}// 第一天计算
							else {
								// 其余中间天数直接计算
								
								String device_compress = rs.getString(2);
								for (int i = 0, j = 0; i <= device_compress
										.length()
										&& j < device_compress.length(); i++) {
									if (!device_compress.substring(j,j + 4)
											.contains("x")) {// 设备不为xxxx,则表示出现
										String day;
										if (rs.getInt(1) < 10)
											day = "0" + rs.getInt(1);
										else
											day = "" + rs.getInt(1);
										String record_time = DateUtil
												.addDateWithMinute(
														table.substring(1, 5)
																+ "-"
																+ table.substring(6)
																+ "-" + day
																+ " 00:00:00",
														i + 1);
										String deviceId =device_compress
												.substring(j,j + 4);// 获取设备号
										String device_mac = name_mac.get(deviceId);//获取设备mac
										Address ad = device_addr
												.get(device_mac);
										UsrMacInfo u = new UsrMacInfo(usr_mac,
												record_time, device_mac,
												ad.getArea(), ad.getAddr(),
												ad.getLongtitude(),
												ad.getLatitude());
										l.add(u);
										j += 4;
									} else
										j += 4;
								}
							}// 中间天数计算
						}// 查询出的结果计算
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs);
						DBManager.close(pstmt);
						DBManager.close(con);
					}
				}// 开始表计算完毕
					// 2.若为结束表，即结束时间在此表
				else if (table.equals(etable)) {
					int m = DateUtil.getMinutes(sTime);// 获取开始时是第几分钟
					int n = DateUtil.getMinutes(eTime);// 获取结束时时是第几分钟
					// 若存在此表，则找到表，接下来计算日其
					sql = "select day,device_compress from "
							+ "t_usr_mac_compress" + table
							+ " where day <= ? and usr_mac = ?";// 将该天的时间压缩，设备压缩读出
					con = DBManager.getCon();
					pstmt = DBManager.prepare(con, sql);
					try {
						pstmt.setInt(1, eDay);
						pstmt.setString(2, usr_mac);
						rs = pstmt.executeQuery();
						while (rs.next()) {// 轮询查询出的结果
							if (rs.getInt(1) == eDay) {// 是最后一天的数据
								String device_compress = rs.getString(2).substring(0,n * 4);

								for (int i = 0, j = 0; i <= device_compress
										.length()
										&& j < device_compress.length(); i++) {
									if (!device_compress.substring(j,j + 4)
											.contains("x")) {// 设备不为xxxx,则表示出现
										String day;
										if (rs.getInt(1) < 10)
											day = "0" + rs.getInt(1);
										else
											day = "" + rs.getInt(1);
										String record_time = DateUtil
												.addDateWithMinute(
														table.substring(1, 5)
																+ "-"
																+ table.substring(6)
																+ "-" + day
																+ " 00:00:00",
														i + 1);
										String deviceId =device_compress
												.substring(j,j + 4);// 获取设备号
										String device_mac = name_mac.get(deviceId);//获取设备mac
										Address ad = device_addr
												.get(device_mac);
										UsrMacInfo u = new UsrMacInfo(usr_mac,
												record_time, device_mac,
												ad.getArea(), ad.getAddr(),
												ad.getLongtitude(),
												ad.getLatitude());
										l.add(u);
										j += 4;
									} else
										j += 4;
								}

							}// 最后一天计算
							else {
								// 中间天数直接计算
								String device_compress = rs.getString(2);

								for (int i = 0, j = 0; i <= device_compress
										.length()
										&& j < device_compress.length(); i++) {
									if (!device_compress.substring(j, j + 4)
											.contains("x")) {// 设备不为xxxx,则表示出现
										String day;
										if (rs.getInt(1) < 10)
											day = "0" + rs.getInt(1);
										else
											day = "" + rs.getInt(1);
										String record_time = DateUtil
												.addDateWithMinute(
														table.substring(1, 5)
																+ "-"
																+ table.substring(6)
																+ "-" + day
																+ " 00:00:00",
														i + 1);
										String deviceId =device_compress
												.substring(j, j + 4);// 获取设备号
										String device_mac = name_mac.get(deviceId);//获取设备mac
										Address ad = device_addr
												.get(device_mac);
										UsrMacInfo u = new UsrMacInfo(usr_mac,
												record_time, device_mac,
												ad.getArea(), ad.getAddr(),
												ad.getLongtitude(),
												ad.getLatitude());
										l.add(u);
										j += 4;
									} else
										j += 4;
								}
							}// 中间天数计算
						}// 查询出的结果计算
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs);
						DBManager.close(pstmt);
						DBManager.close(con);
					}
				}// 结束表
					// 3.若为中间表，则全查即可
				else {
					int m = DateUtil.getMinutes(sTime);// 获取开始时是第几分钟
					int n = DateUtil.getMinutes(eTime);// 获取结束时时是第几分钟
					// 若存在此表，则找到表，接下来计算日其
					sql = "select day,device_compress from "
							+ "t_usr_mac_compress" + table + " where usr_mac = ?";// 将该天的时间压缩，设备压缩读出
					con = DBManager.getCon();
					pstmt = DBManager.prepare(con, sql);
					try {
						pstmt.setString(1, usr_mac);
						rs = pstmt.executeQuery();
						while (rs.next()) {// 轮询查询出的结果
							String device_compress = rs.getString(2);

							for (int i = 0, j = 0; i <= device_compress
									.length()
									&& j < device_compress.length(); i++) {
								if (!device_compress.substring(j, j + 4)
										.contains("x")) {// 设备不为xxxx,则表示出现
									String day;
									if (rs.getInt(1) < 10)
										day = "0" + rs.getInt(1);
									else
										day = "" + rs.getInt(1);
									String record_time = DateUtil
											.addDateWithMinute(
													table.substring(1, 5)
															+ "-"
															+ table.substring(6)
															+ "-" + day
															+ " 00:00:00",
													i + 1);
									String deviceId =device_compress
											.substring(j,j + 4);// 获取设备号
									String device_mac = name_mac.get(deviceId);//获取设备mac
									Address ad = device_addr.get(device_mac);
									UsrMacInfo u = new UsrMacInfo(usr_mac,
											record_time, device_mac,
											ad.getArea(), ad.getAddr(),
											ad.getLongtitude(),
											ad.getLatitude());
									l.add(u);
									j += 4;
								} else
									j += 4;
							}
						}// 查询出的结果计算
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						DBManager.close(rs);
						DBManager.close(pstmt);
						DBManager.close(con);
					}
				}
			}// for 循环list

		}
		return l;
	}

	public static String getJYString(String time_compress) {// 解压缩时间串
		String s = "";
		for (int i = 0; i < time_compress.length(); i++) {
			if (time_compress.charAt(i) == '0')
				s += "0000";
			else if (time_compress.charAt(i) == '1')
				s += "0001";
			else if (time_compress.charAt(i) == '2')
				s += "0010";
			else if (time_compress.charAt(i) == '3')
				s += "0011";
			else if (time_compress.charAt(i) == '4')
				s += "0100";
			else if (time_compress.charAt(i) == '5')
				s += "0101";
			else if (time_compress.charAt(i) == '6')
				s += "0110";
			else if (time_compress.charAt(i) == '7')
				s += "0111";
			else if (time_compress.charAt(i) == '8')
				s += "1000";
			else if (time_compress.charAt(i) == '9')
				s += "1001";
			else if (time_compress.charAt(i) == 'a')
				s += "1010";
			else if (time_compress.charAt(i) == 'b')
				s += "1011";
			else if (time_compress.charAt(i) == 'c')
				s += "1100";
			else if (time_compress.charAt(i) == 'd')
				s += "1101";
			else if (time_compress.charAt(i) == 'e')
				s += "1110";
			else if (time_compress.charAt(i) == 'f')
				s += "1111";
		}
		return s;
	}

	public static int getDeviceIdByYS(String device_id) {// 将设备id转换为十进制
		int id = 0;
		for (int i = 0; i < device_id.length(); i++) {
			int k;
			if (device_id.charAt(i) == 'a')
				k = 10;
			else if (device_id.charAt(i) == 'b')
				k = 11;
			else if (device_id.charAt(i) == 'c')
				k = 12;
			else if (device_id.charAt(i) == 'd')
				k = 13;
			else if (device_id.charAt(i) == 'e')
				k = 14;
			else if (device_id.charAt(i) == 'f')
				k = 15;
			else
				k = Integer.parseInt(String.valueOf(device_id.charAt(i)));// 0-9
			id += k * ((int) Math.pow(16, 3 - i));
		}
		return id;
	}

	/**
	 * 
	 * @param group
	 * @param start_time
	 * @param end_time
	 * @return 返回的是该群组mac的符合条件的时间串与设备串
	 */
	public static List<UserTimeDevice> getData1(String group,
			String start_time, String end_time) {
		List<UserTimeDevice> lutd = new ArrayList<UserTimeDevice>();
		List<String> lm = new GroupMemberDaoImpl().getMemeberByGroup(group);// 获得该群组的用户mac

		int sYear = Integer.parseInt(start_time.substring(0, 4));// 获取开始的年份
		int sMonth = Integer.parseInt(start_time.substring(5, 7));// 获取开始的月份
		int sDay = Integer.parseInt(start_time.substring(8, 10));// 获取开始的天数
		String sTime = start_time.trim().substring(11);// 开始时间

		int eYear = Integer.parseInt(end_time.substring(0, 4));// 获取结束的年份
		int eMonth = Integer.parseInt(end_time.substring(5, 7));// 获取结束的月份
		int eDay = Integer.parseInt(end_time.substring(8, 10));// 获取结束的天数
		String eTime = end_time.trim().substring(11);// 结束时间

		System.out.println(sMonth + "结束月份" + eMonth);
		UsrMacDaoImpl um = new UsrMacDaoImpl();
		deviceDaoImpl dd = new deviceDaoImpl();

		// 找出需要进行数据查询的表
		List<String> list = new ArrayList<String>();// 存放每一个需要进行查询的表名
		if (eYear > sYear) {// 不在同一年
			String month;
			String day;
			if (sDay < 10)
				day = "0" + sDay;
			else
				day = String.valueOf(sDay);
			for (int i = sMonth; i <= 12; i++) { // 添加第一年
				if (i < 10)
					month = "0" + i;
				else
					month = String.valueOf(i);
				list.add("_" + sYear + "_" + month);
			}
			for (int i = sYear + 1; i <= eYear - 1; i++) {// 添加中间的表
				for (int j = 1; j <= 12; j++) {
					if (j < 10)
						month = "0" + j;
					else
						month = String.valueOf(j);
					list.add("_" + i + "_" + month);
				}
			}
			for (int i = 1; i <= eMonth; i++) { // 添加最后一年
				if (i < 10)
					month = "0" + i;
				else
					month = String.valueOf(i);
				list.add("_" + eYear + "_" + month);
			}
		}
		if (eYear == sYear) {// 在同一年,直接添加月份
			String month;
			for (int i = sMonth; i <= eMonth; i++) {
				if (i < 10)
					month = "0" + i;
				else
					month = String.valueOf(i);
				list.add("_" + sYear + "_" + month);
			}
		}// 所有需要查找的表添加完毕
		System.out.println("list的大小为" + list.size());

		return lutd;
	}
	
	
	public static String ClobToString(Reader is)  {  
        String reString = "";      
        try {
			
			BufferedReader br = new BufferedReader(is);  
			String s = br.readLine();  
			StringBuffer sb = new StringBuffer();  
			while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING  
			    sb.append(s);  
			    s = br.readLine();  
			}  
			reString = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return reString;//返回转换完的字符串  
    }  
}
