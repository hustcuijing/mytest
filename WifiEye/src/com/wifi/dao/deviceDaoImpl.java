package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.wifi.util.DBManager;
import com.wifi.model.Address;
import com.wifi.model.DeviceBean;
import com.wifi.model.PageBean;
import com.wifi.util.StringUtil;


public class deviceDaoImpl implements deviceDao {

	
	
	public void setLocationNull(String device_name){
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		String sql = "update device set location_id = -1 where device_name = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,device_name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public List<String> getDevice_name(String area,String addr){
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		
		int i = 1;
		StringBuilder sql = new StringBuilder("select device_name from device,location where device.location_id = location.location_id ") ;
		if(area != null && area.length() != 0 && !area.equals(" ")){
			sql.append("and area = ?");
		}
		if(addr != null && addr.length() != 0 && !addr.equals(" ")){
			sql.append("and addr = ?");
		}
		
		try {
			pstmt = con.prepareStatement(sql.toString());
		    if(area != null && area.length() != 0 && !area.equals(" ")){
		    	 pstmt.setString(i++, area);
			}
		    if(addr != null && addr.length() != 0 && !addr.equals(" ")){
		    	 pstmt.setString(i++, addr);
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
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
	public int selectDevcieCount(String area, String addr, String device_name,String ip) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		int i = 1;
		StringBuilder sql = new StringBuilder("select count(*) from device,location where device.location_id = location.location_id ") ;
		if(StringUtil.isNotEmpty(area)){
			sql.append("and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sql.append("and addr = ?");
		}
		if(StringUtil.isNotEmpty(device_name)){
			sql.append("and device_name = ?");
		}
		if(StringUtil.isNotEmpty(ip)){
			sql.append(" and ip = ?");
		}
		try {
			pstmt = con.prepareStatement(sql.toString());
		    if(StringUtil.isNotEmpty(area)){
		    	 pstmt.setString(i++, area);
			}
		    if(StringUtil.isNotEmpty(addr)){
		    	 pstmt.setString(i++, addr);
			}
			if(StringUtil.isNotEmpty(device_name)){
				 pstmt.setString(i++, device_name);
			}
			if(StringUtil.isNotEmpty(ip)){
				pstmt.setString(i++, ip);
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
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
		
		return count;
	}


	public List<DeviceBean> getDeviceInfo(String area, String addr,
			String device_name, PageBean page,String ip) {
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<DeviceBean> list = new ArrayList<DeviceBean>();
		
		int i = 1;
		StringBuilder sql = new StringBuilder("select device_mac,device_name,ip,area,addr,status,status_time from device,location where device.location_id = location.location_id ") ;
		if(StringUtil.isNotEmpty(area)){
			sql.append("and area = ?");
		}
		if(StringUtil.isNotEmpty(addr)){
			sql.append("and addr = ?");
		}
		if(StringUtil.isNotEmpty(device_name)){
			sql.append("and device_name = ?");
		}
		if(StringUtil.isNotEmpty(ip)){
			sql.append(" and ip = ?");
		}
		sql.append(" limit " + page.getStart() + "," + page.getRows() );
		try {
			pstmt = con.prepareStatement(sql.toString());
		    if(StringUtil.isNotEmpty(area)){
		    	 pstmt.setString(i++, area);
			}
		    if(StringUtil.isNotEmpty(addr)){
		    	 pstmt.setString(i++, addr);
			}
			if(StringUtil.isNotEmpty(device_name)){
				 pstmt.setString(i++, device_name);
			}
			if(StringUtil.isNotEmpty(ip)){
				pstmt.setString(i++, ip);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				DeviceBean db = new DeviceBean();
				db.setDevice_mac(rs.getString(1));
				db.setDevice_name(rs.getString(2));
				db.setIp(rs.getString(3));
				db.setArea(rs.getString(4));
				db.setAddr(rs.getString(5));
				String status_time=rs.getString(7);//获取状态的时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try {
					if(null==status_time||new Date().getTime()-sdf.parse(status_time).getTime()>3600000){//时间间隔大于1小时则按异常处理
						db.setStatus(0);
					}else{
						db.setStatus(rs.getInt(6));//获取最新状态
					}
				} catch (ParseException e) {
					db.setStatus(0);
				}
				list.add(db);
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

	public int deviceAdd(String device_name, String ip, int location_id) {
		String sql = "update device set ip = ?,location_id = ? where device_name = ?";
		Connection con = null;
		PreparedStatement psmt = null;
		int rs = 0;
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, ip);
			psmt.setInt(2, location_id);
			psmt.setString(3, device_name);
			rs = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		
		return rs;
	}

	public boolean deviceExistWithIp(String ip,String id) {
		String sql = "select location_id from device where ip = ? and device_name != ?";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		boolean result = false;
		
		con = DBManager.getCon();
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, ip);
			psmt.setString(2, id);
			rs = psmt.executeQuery();
			if(rs.next())
				result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		
		return result;
	}

	public int deviceDelete(String delIds) {
		String[] strs = delIds.split(",");
		Connection con = null;
		PreparedStatement psmt = null;
		int result = 0;
		con = DBManager.getCon();
		try {
			con.setAutoCommit(false);
			String sql = "update device set location_id = -1,ip = null where device_mac = ?";
			for(String str : strs){
				psmt = con.prepareStatement(sql);
				psmt.setString(1, str);
				psmt.executeUpdate();
				result++;
			}
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				con.rollback();
				result = -1;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBManager.close(psmt);
			DBManager.close(con);
		}
		
		return result;
	}
	
	public List<String> getAllDeviceMacs() {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		String sql = "select device_mac from device";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while (rs.next()) {
				result.add(StringUtil.replaceColonWithUnderline(rs.getString(1)));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}

	public Map<String, Integer> getDeviceIndex() {
		// TODO Auto-generated method stub
		Map<String, Integer> maps = new HashMap<String, Integer>();
		String sql = "select id,device_mac from device_number";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while (rs.next()) {
				maps.put(rs.getString(2),rs.getInt(1));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return maps;
	}

	public Map<Integer, String> getDeviceByIndex() {
		Map<Integer, String> maps = new HashMap<Integer, String>();
		String sql = "select id,device_mac from device_number";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while (rs.next()) {
				maps.put(rs.getInt(1),rs.getString(2));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return maps;
	}
	public Map<String, String> getDeviceByName() {
		Map<String, String> maps = new HashMap<String, String>();
		String sql = "select device_name,device_mac from device";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while (rs.next()) {
				maps.put(rs.getString(1).substring(6),rs.getString(2));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return maps;
	}

	public Map<String,Address> getDeviceAddress() {
		Map<String, Address> result = new HashMap<String, Address>();
		String sql = "select area,addr,device_mac,location.latitude,location.longtitude from device inner join location on device.location_id = location.location_id";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			while (rs.next()) {
				Address add = new Address();
				add.setArea(rs.getString(1));
				add.setAddr(rs.getString(2));
				add.setLatitude(rs.getDouble(4));
				add.setLongtitude(rs.getDouble(5));
				result.put(rs.getString(3), add);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return result;
	}

	public String minFirstTime(){
		String sql = "select min(first_time) from t_visit_info_b8_27_eb_b7_1f_1f";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return "";
	}
	public String maxFirstTime(){
		String sql = "select max(first_time) from t_visit_info_b8_27_eb_b7_1f_1f";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}
		return "";
	}

	public boolean isTableExits(String tableName) {
		// TODO Auto-generated method stub
		String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name = ? ";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			psmt.setString(1, tableName);
			rs = psmt.executeQuery();
			if(rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}

		return false;
	}

	public int getTableCount() {
		// TODO Auto-generated method stub
		String sql = "SELECT count(*) FROM information_schema.TABLES WHERE table_name like 't_visit_info%' ";
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		con = DBManager.getCon();
		psmt = DBManager.prepare(con, sql);
		try {
			rs = psmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBManager.close(rs);
			DBManager.close(psmt);
			DBManager.close(con);
		}

		return 0;
	}
}
