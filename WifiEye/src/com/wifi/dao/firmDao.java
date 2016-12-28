package com.wifi.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.wifi.model.Address;
import com.wifi.util.DBManager;

public class firmDao {
	private static Map<String, String> map = new HashMap<String, String>();
	private static Map<String, Address> addressMap = new HashMap<String, Address>();
	public static String is_Matched(String Mac) {
		String mac = Mac.replace(':', '-');
		BufferedReader br = null;
		FileReader f = null;
		if (map.containsKey(mac.substring(0, 8))) {
			return map.get(mac.substring(0, 8));
		}
		try {
			String path = null;
			try {
				path = firmDao.class.getResource("/").toURI().getPath()
						+ "firm.txt";
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			f = new FileReader(path);
			br = new BufferedReader(f);
			String oneline = "";
			while ((oneline = br.readLine()) != null) {
				if (mac.substring(0, 8).equalsIgnoreCase(
						(oneline).substring(0, 8))) {
					map.put(mac.substring(0, 8),
							oneline.substring(9, oneline.length()));
					return oneline.substring(9, oneline.length());
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";

	}
	public static Address getDeviceAddress(String device_mac) {
		if(addressMap.size() != 0){
			return addressMap.get(device_mac);
		}
		else{
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
					addressMap.put(rs.getString(3), add);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBManager.close(rs);
				DBManager.close(psmt);
				DBManager.close(con);
			}
			return addressMap.get(device_mac);
		}
	}
}
