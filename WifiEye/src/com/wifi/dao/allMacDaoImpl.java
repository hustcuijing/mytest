package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import com.wifi.model.PageBean;
import com.wifi.model.VisitBean;
import com.wifi.util.DBManager;


public class allMacDaoImpl implements allMacDao{

	public int selectMacCount(String start_time, String end_time,String usr_mac,
			String area, String addr, String ap_mac,String device_name){
		int count = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
			 try {
				if(start_time != null && start_time.length() != 0)
					 sdf.parse(start_time);
				 if(end_time != null && end_time.length() != 0)
					 sdf.parse(end_time);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 1;//ռλ��Ĭ�ϳ�ʼ���
		StringBuilder sql = new StringBuilder("select count(*) from interface,device,location where id >= 1 ");
		
		if(start_time != null && start_time.length() != 0 && !start_time.equals(" ")){
			sql.append("and recordtime >= ?");
		}
		if(end_time != null && end_time.length() != 0 && !end_time.equals(" ")){
			sql.append("and recordtime <= ?");
		}
		if(usr_mac != null && usr_mac.length() != 0 && !usr_mac.equals(" ")){
			sql.append("and usr_mac = ?");
		}
		if(area != null && area.length() != 0 && !area.equals(" ")){
			sql.append("and location.area =?");
		}
		if(addr != null && addr.length() != 0 && !addr.equals(" ")){
			sql.append("and location.addr =?");
		}
		if(ap_mac != null && ap_mac.length() != 0 && !ap_mac.equals(" ")){
			sql.append("and ap_mac =?");
		}
		if(device_name!= null && device_name.length() != 0 && !device_name.equals(" ")){
			sql.append("and device.device_name =?");
		}
		sql.append(" and  device.device_mac = interface.device_mac and device.location_id = location.location_id ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			if(start_time != null && start_time.length() != 0 && !start_time.equals(" ")){
				pstmt.setString(i++, start_time);
			}
			if(end_time != null && end_time.length() != 0 && !end_time.equals(" ")){
				pstmt.setString(i++, end_time);
			}
			if(usr_mac != null && usr_mac.length() != 0 && !usr_mac.equals(" ")){
				pstmt.setString(i++, usr_mac);
			}
			if(area != null && area.length() != 0 && !area.equals(" ")){
				pstmt.setString(i++,area );
			}
			if(addr != null && addr.length() != 0 && !addr.equals(" ")){
				pstmt.setString(i++,addr );
			}
			if(ap_mac != null && ap_mac.length() != 0 && !ap_mac.equals(" ")){
				pstmt.setString(i++, ap_mac );
			}
			if(device_name != null && device_name.length() != 0 && !device_name.equals(" ")){
				pstmt.setString(i++, device_name );
			}
			
			//System.out.println(sql.toString());
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

	
	public List<VisitBean> selectMac(String start_time, String end_time,
			String usr_mac, String area, String addr, String ap_mac,
			String device_name, PageBean page) {
		List<VisitBean> list = new ArrayList<VisitBean>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		
			 try {
				if(start_time != null && start_time.length() != 0)
					 sdf.parse(start_time);
				 if(end_time != null && end_time.length() != 0)
					 sdf.parse(end_time);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

		Connection con = DBManager.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 1;//ռλ��Ĭ�ϳ�ʼ���
		StringBuilder sql = new StringBuilder("select device.device_name,location.area," +
				"location.addr,interface.recordtime,interface.usr_mac," +
				"interface.ap_mac,interface.data_rate, interface.rssi_signal," +
				"interface.channel_id,interface.app_type,interface.app_info,device.device_mac from interface,location,device where device.device_mac= interface.device_mac and device.location_id = location.location_id ");
		StringBuilder idQuery = new StringBuilder("select id from interface,location,device where device.device_mac= interface.device_mac and device.location_id = location.location_id ");
		if(start_time != null && start_time.length() != 0 && !start_time.equals(" ")){
			//sql.append("and interface.recordtime >= ?" + start_time);
			idQuery.append("and interface.recordtime >= ? ");
		}
		if(end_time != null && end_time.length() != 0 && !end_time.equals(" ")){
			//sql.append("and interface.recordtime <= ?");
			idQuery.append("and interface.recordtime <= ? ");
		}
		if(usr_mac != null && usr_mac.length() != 0 && !usr_mac.equals(" ")){
			//sql.append("and interface.usr_mac = ?");
			idQuery.append("and interface.usr_mac = ? ");
		}
		if(area != null && area.length() != 0 && !area.equals(" ")){
			//sql.append("and location.area = ?");
			idQuery.append("and location.area = ? ");
		}
		if(addr != null && addr.length() != 0 && !addr.equals(" ")){
			//sql.append("and location.addr =?");
			idQuery.append("and location.addr = ? ");
		}
		if(ap_mac != null && ap_mac.length() != 0 && !ap_mac.equals(" ")){
			//sql.append("and interface.ap_mac =?");
			idQuery.append("and interface.ap_mac = ? ");
		}
		if(device_name!= null && device_name.length() != 0 && !device_name.equals(" ")){
			//sql.append("and device.device_name =?");
			idQuery.append("and device.device_name = ? ");
		}
		if(page.getStart() >= 0)
			sql.append(" and id >= (" + idQuery + " limit " + page.getStart()+",1) limit "+page.getRows());
		try {
			pstmt = con.prepareStatement(sql.toString());
			if(start_time != null && start_time.length() != 0 && !start_time.equals(" ")){
				pstmt.setString(i++, start_time);
			}
			if(end_time != null && end_time.length() != 0 && !end_time.equals(" ")){
				pstmt.setString(i++, end_time);
			}
			if(usr_mac != null && usr_mac.length() != 0 && !usr_mac.equals(" ")){
				pstmt.setString(i++, usr_mac);
			}
			if(area != null && area.length() != 0 && !area.equals(" ")){
				pstmt.setString(i++, area );
			}
			if(addr != null && addr .length() != 0 && !addr .equals(" ")){
				pstmt.setString(i++, addr  );
			}
			if(ap_mac != null && ap_mac.length() != 0 && !ap_mac.equals(" ")){
				pstmt.setString(i++, ap_mac );
			}
			if(device_name != null && device_name.length() != 0 && !device_name.equals(" ")){
				pstmt.setString(i++, device_name );
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				VisitBean vb = new VisitBean();
				vb.setDevice_name(rs.getString(1));
				vb.setAddress(rs.getString(2) + rs.getString(3));
				vb.setRecordtime(rs.getString(4));
				vb.setUsr_mac(rs.getString(5));
				vb.setAp_mac(rs.getString(6));
				vb.setData_rate(rs.getFloat(7));
				vb.setRssi_signal(rs.getInt(8));
				vb.setChannel_id(rs.getInt(9));
				vb.setApp_type(rs.getInt(10));
				vb.setApp_info(rs.getString(11));
				vb.setFirm(firmDao.is_Matched(rs.getString(5)));//设置厂商
				list.add(vb);
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


	public ResultSet getAllMacs(String first_time, String last_time,
			String usr_mac, String area, String addr, String ap_mac,
			String device_name, PageBean page) {
		// TODO Auto-generated method stub
		return null;
	}
}
