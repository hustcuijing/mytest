package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wifi.model.AppInfo;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.util.DBManager;
import com.wifi.util.StringUtil;

public class AppInfoDaoImpl implements AppInfoDao {

	public int getAllCount(String start_time, String end_time,String area, String addr, int app_type) {
		// TODO Auto-generated method stub
		System.out.println("开始计算条数");
	    System.out.println("计算条数时的时间" + start_time);
	    System.out.println(app_type+"应用类型");
		AddrDao ad = new AddrDao();
		List<String> device_macs = new ArrayList<String>();// 存放此地之中的地址中的所有设备mac
		device_macs = ad.deviceList(area, addr);// 获取此地址中的所有设备mac
		if(device_macs.size() == 0)
			return 0;
		int m = 0;
		deviceDao dd = new deviceDaoImpl();
		for(;m < device_macs.size();m++){
			if(dd.isTableExits("t_app_info_"+device_macs.get(m).replace(':', '_')))//判断表是否存在
				break;
		}
		if(m >= device_macs.size())//判断是否越界，即表不存在
			return 0;
			
		StringBuilder sql = new StringBuilder("select max(id),min(id) from  t_app_info_"
				+ device_macs.get(m).replace(':', '_') + "  where id >= 1");
		if (StringUtil.isNotEmpty(start_time)) {
			sql.append(" and recordtime >= ?");
		}
		if (StringUtil.isNotEmpty(end_time)) {
			sql.append(" and recordtime <= ?");
		}
        if(app_type != -1){
        	sql.append(" and app_type = ?");
        }
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int result = 0;
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
			if(app_type != -1){
				psmt.setInt(i++,app_type);
	        }
			rs = psmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1) - rs.getInt(2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		System.out.println("记录数为" + result);
		return result;
	}

	public List<AppInfo> getAllAppInfos(String start_time, String end_time,
			String area, String addr, int app_type, PageBean page) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			if (start_time != null && start_time.length() != 0)
				sdf.parse(start_time);
			if (end_time != null && end_time.length() != 0)
				sdf.parse(end_time);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<AppInfo> list = new ArrayList<AppInfo>();
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		AddrDao ad = new AddrDao();
		List<String> device_macs = new ArrayList<String>();// 存放此地之中的地址中的所有设备mac
		device_macs = ad.deviceList(area, addr);// 获取此地址中的所有设备mac
		Random rand = new Random();
		int deviceNum = device_macs.size();//获取的设备数量
		if(deviceNum <= 0)
			return null;
		for(int i = 0;i < page.getRows();){//获取page.getRows()行数据
			
			int m = rand.nextInt(deviceNum);
			if(!new deviceDaoImpl().isTableExits("t_app_info_"+device_macs.get(m).replace(':', '_')))//若此表不存在，继续寻找下一个表
				continue;
			
			String string = "select app_type,app_info,recordtime, device.device_mac,area,addr," +
					"usr_mac,ap_mac,data_rate,rssi_signal,channel_id from device,location,t_app_info_"
					+ device_macs.get(m).replace(':', '_') + " where  device.location_id" +
							"=location.location_id and device.device_mac = ? ";
			StringBuilder sql = new StringBuilder(string);
			if (StringUtil.isNotEmpty(start_time)) {
				sql.append(" and recordtime >= ? ");
			}
			if (StringUtil.isNotEmpty(end_time)) {
				sql.append(" and recordtime <= ? ");
			}
			if (app_type != -1) {
				sql.append(" and app_type = ? ");
			}
			//每张表随机获取获取一条记录
			sql.append(" order by recordtime desc limit " + page.getStart() + rand.nextInt(500) + ",1");

			con = DBManager.getCon();
			System.out.println("连接成功过");
			pstmt = DBManager.prepare(con, sql.toString());
			int j = 2;
			try {
				pstmt.setString(1, device_macs.get(m));
				if (StringUtil.isNotEmpty(start_time)) {
					pstmt.setString(j++, start_time);
				}
				if (StringUtil.isNotEmpty(end_time)) {
					pstmt.setString(j++, end_time);
				}
				if (app_type != -1) {
					pstmt.setInt(j++, app_type);//应用类型
				}
				System.out.println("查寻开始");
				rs = pstmt.executeQuery();
				System.out.println("查寻完毕");
				while (rs.next()) {
					AppInfo info = new AppInfo();
					info.setApp_info(rs.getString(2));
					info.setRecordtime(rs.getString(3));
					info.setDevice_mac(rs.getString(4));
					info.setArea(rs.getString(5));
					info.setAddr(rs.getString(6));
					info.setUsr_mac(rs.getString(7));
					info.setAp_mac(rs.getString(8));
					info.setData_rate(rs.getFloat(9));
					info.setRssi_signal(rs.getInt(10));
					info.setChannel_id(rs.getInt(11));
					list.add(info);
					i++;//新增一条记录，则可以查询下一个表
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(pstmt);
				DBManager.close(con);
			}
			
		}
		System.out.println("list的大小" + list.size());
		return list;
	}

}
