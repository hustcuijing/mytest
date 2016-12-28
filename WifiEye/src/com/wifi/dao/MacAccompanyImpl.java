package com.wifi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wifi.model.Address;
import com.wifi.model.GetRouteBean;
import com.wifi.model.UsrMacInfo;
import com.wifi.model.Visit;
import com.wifi.util.DBManager;
import com.wifi.util.DataUtil;

public class MacAccompanyImpl implements MacAccompany {
         
	public List<Visit> getAccompanyMac(String usr_mac, String start_time,
			String end_time, String timewave) {//只返回了伴随mac及其出现次数
		// TODO Auto-generated method stub
		
		List<UsrMacInfo> list = DataUtil.getInfos2( start_time, end_time,usr_mac);
		if(list == null || list.size() == 0)
			return null;
        System.out.println("list的大小为" + list.size());
		Map<String,List<Visit>> map = new HashMap<String,List<Visit>>();
		List<Visit> listv = new ArrayList<Visit>();
		List<String> dmacs = new ArrayList<String>();//存放所有出现过的设备mac
		RetainallMac rm = new RetainallMacImpl();//取交集
		//得到所有出现过的设备mac（进行压缩）,按时间出现排序
		for(UsrMacInfo rrb:list){
			    if(dmacs.size() == 0)//列表为空，则添加
			    	dmacs.add(rrb.getDevice_mac());
			    else if(!dmacs.contains(rrb.getDevice_mac()))//存放不同的设备mac
				      dmacs.add(rrb.getDevice_mac());
			    	  
		}

		for(String dm:dmacs)
			System.out.println("设备mac" + dm);
		
		Connection con = DBManager.getCon();
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2= null;
		ResultSet rs2 = null;
		String sql1 = "";
		String sql2 = "";
		String st = "";//查询处理后的出现开始时间
		String et = "";//查询处理后的出现结束时间
		System.out.println(start_time + "  " + end_time);
		for(int i = 0;i < dmacs.size();i++){
			listv = new ArrayList<Visit>();//新开始设备将list值为空值
			sql1 = "select first_time ,last_time from " + "t_visit_info_" + dmacs.get(i).replace(':', '_') + 
					" where usr_mac = ? and " +
					"((first_time >= ? and first_time <= ?) or (last_time >= ? and last_time <= ?))";//从设备表中找出该mac的开始时间,结束时间
			System.out.println(sql1);
			pstmt1 = DBManager.prepare(con, sql1);
			try {
				pstmt1.setString(1, usr_mac);
				pstmt1.setString(2, start_time);
				pstmt1.setString(3, end_time);
				pstmt1.setString(4, start_time);
				pstmt1.setString(5, end_time);
				rs1= pstmt1.executeQuery();
				while(rs1.next()){
					if(rs1.getString(1).compareTo(start_time)  < 0)//查出的时间大于给定的开始时间
						st = start_time;
					else
						st = rs1.getString(1);
					if(rs1.getString(2).compareTo(end_time)  > 0)//查出的时间大于给定的开始时间
						et = end_time;
					else
						et = rs1.getString(2);	
					
					//得出开始时间结束时间，则计算此时间段内的其他mac
					st = format_Time(st, -Integer.parseInt(timewave));
					et = format_Time(et, Integer.parseInt(timewave));
				    System.out.println("此时时间" + st + ":" + et);
					sql2 = "select usr_mac,first_time,last_time,device_mac from " + "t_visit_info_" + dmacs.get(i).replace(':', '_') + " where (first_time >= ? and first_time <= ?) " +
							" or (last_time >= ? and last_time <= ?) ";
					System.out.println("开始");
			        pstmt2 = DBManager.prepare(con, sql2);
			        pstmt2.setString(1, st);
			        pstmt2.setString(2, et);
			        pstmt2.setString(3, st);
			        pstmt2.setString(4, et);
			        System.out.println("开始查询");
			        rs2 = pstmt2.executeQuery();
			        System.out.println("查询完毕");
			        System.out.println(dmacs.get(i) + "开始查询伴随mac");
			        while(rs2.next()){//处理从此设备中查询出的所有符合条件的用户mac
			        	if(!usr_mac.equals(rs2.getString(1)) ){
			        	Visit v = new Visit();
			        	v.setUsr_mac(rs2.getString(1));
			        	v.setFirst_time(rs2.getString(2));
			        	v.setLast_time(rs2.getString(3));
			        	v.setDevice_mac(rs2.getString(4));
			        	if(!map.containsKey(dmacs.get(i))){
			        		listv.add(v);
			        		map.put(dmacs.get(i), listv);
			        	}
			        	else{
			        		map.get(dmacs.get(i)).add(v);
			        	}	
			        	}
			        }
			        System.out.println(dmacs.get(i) + "所对应的记录寻找完毕");
			        DBManager.close(rs2);
					DBManager.close(pstmt2); 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				DBManager.close(rs1);
				DBManager.close(pstmt1);
			}
		}
		DBManager.close(con);
		
		System.out.println("查询执行完毕");
		System.out.println("map的大小" + map.size());
		//处理交集
	    List<String> macs = new ArrayList<String>();//存放map中的所有设备mac
	     for(String mac : map.keySet()){
	    	 macs.add(mac);
	     }
	     System.out.println("macs的大小" + macs.size());
	     //记录每个伴随mac出现了多少次
	     List<Visit> showTimeMac = new ArrayList<Visit>();
	     List<String> usr_macs = new ArrayList<String>();//存放所有可能的伴随mac地址
	     List<Visit> list2 = new ArrayList<Visit>();
	     Map<String,GetRouteBean> map2 = new HashMap<String,GetRouteBean>();
	     String lastdevice_mac = null;
			String laststarttime = null;
	     if(macs != null && map.size() != 0){
	    	 for(String mdmac:map.keySet()){
	    		 List<Visit> lv = map.get(mdmac);
	    	     if(lv  != null){
	    	    	 for(Visit vv:lv){
	    	    		 if(!usr_macs.contains(vv.getUsr_mac()))
	    	    			 usr_macs.add(vv.getUsr_mac());//获取所有mac，不重复的存放
	    	    	 }
	    	     }
	    	 }
	    	 System.out.println("usr_macs的大小" + usr_macs.size()); 
	     //获得所有出现过得mac后，对每一个mac进行计算次数
			if (usr_macs != null) {
				for (String um : usr_macs) {
					showTimeMac = new ArrayList<Visit>();// 每次新开始处理一个伴随mac，则初始化对应的记录集合
					map2 = new HashMap<String, GetRouteBean>();// 初始化对应的map
					for (String mdmac : map.keySet()) {
						List<Visit> lv = map.get(mdmac);
						if (lv != null) {
							for (Visit vv : lv) {
								if (um.equals(vv.getUsr_mac()))
									showTimeMac.add(vv);// 获取此时的usr_mac所对应的所有对应记录
							}
						}
					}
						System.out.println(um + "对应的记录数为" + showTimeMac.size());
						Collections.sort(showTimeMac);// 按时间出现排序，从小到大，此时当中只有一个伴随mac的信息
						System.out.println("对所有记录进行的排序执行完毕");
						System.out.println("计算出现次数前" + map2.size());
						for (Visit r : showTimeMac) {// 处理输入，根据出现的时间不同，设别不同进行排序，在时间的基础上对设备进行排序
							if (!map2.isEmpty()) {
								System.out.println("执行if" + map2.size());
 
								if (r.getDevice_mac().equals(lastdevice_mac)) {// 如果出现的设备mac不变，则更新结束时
									System.out.println("出现的设备不变");
									map2.get(laststarttime).setEnd_time(
											r.getLast_time());
								}

								else {// 设备mac变化
									System.out.println("出现的设备变化");
									map2.put(
											r.getFirst_time(),
											new GetRouteBean(r.getUsr_mac(), r
													.getFirst_time(), r
													.getLast_time(), r
													.getDevice_mac(), r
													.getArea(), r.getAddr(), r
													.getLatitude(), r
													.getLongtitude()));
									lastdevice_mac = r.getDevice_mac();
									laststarttime = r.getFirst_time();
								}

							} else {
								// 首次添加map
								System.out.println("执行else");
								map2.put(
										r.getFirst_time(),
										new GetRouteBean(r.getUsr_mac(), r
												.getFirst_time(), r
												.getLast_time(), r
												.getDevice_mac(), r.getArea(),
												r.getAddr(), r.getLatitude(), r
														.getLongtitude()));
								lastdevice_mac = r.getDevice_mac();
								laststarttime = r.getFirst_time();
								System.out.println("首次添加" + lastdevice_mac + laststarttime);

							}//else
					}//for
					System.out.println(um + "所对应的记录数为:" + map2.size());
					
					list2.add(new Visit(um, map2.size()));//处理完一个伴随mac,将他加入集合，mac,出现次数
	    	    }//for
	    	 }//if
	     }//if
	     if(list2 != null && list.size() != 0)
	    	 Collections.sort(list2, new Com());
		return list2;
	}

	public static String format_Time(String time,int x){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date == null)
			return "";
	    Calendar cal = Calendar.getInstance();   
		cal.setTime(date);
		cal.add(Calendar.MINUTE, x);// 24小时制
		date = cal.getTime();
//		System.out.println("after:" + sdf.format(date)); // 显示更新后的日期
		cal = null;
		return sdf.format(date);
	}
	
	public List<GetRouteBean> getAccompanyMacInfo(String usr_mac, String acusr_mac,
			String start_time, String end_time, String timewave) {
		// TODO Auto-generated method stub
		List<UsrMacInfo> list = DataUtil.getInfos2( start_time, end_time,usr_mac);//后期获取输入
		Map<String,List<Visit>> map = new HashMap<String,List<Visit>>();
		List<Visit> listv = new ArrayList<Visit>();
		List<String> dmacs = new ArrayList<String>();//存放所有出现过的设备mac
		RetainallMac rm = new RetainallMacImpl();//取交集
		//得到所有出现过的设备mac（进行压缩）,按时间出现排序
		for(UsrMacInfo rrb:list){
			    if(dmacs.size() == 0)//列表为空，则添加
			    	dmacs.add(rrb.getDevice_mac());
			    else if(!dmacs.contains(rrb.getDevice_mac()))//与上一个mac不同则添加
				      dmacs.add(rrb.getDevice_mac());
			    	  
		}
//		dmacs.add("b8:27:eb:09:3e:2d");
//		dmacs.add("b8:27:eb:15:43:4b");
		for(String dm:dmacs)
			System.out.println("设备mac" + dm);
		
		Connection con = DBManager.getCon();
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2= null;
		ResultSet rs2 = null;
		String sql1 = "";
		String sql2 = "";
		String st = "";//查询处理后的出现开始时间
		String et = "";//查询处理后的出现结束时间
		Map<String,Address> mmap = new deviceDaoImpl().getDeviceAddress();
		for(int i = 0;i < dmacs.size();i++){
			listv = new ArrayList<Visit>();//新开始设别将list值为空值
			sql1 = "select first_time ,last_time from " + "t_visit_info_" + dmacs.get(i).replace(':', '_') + 
					" where usr_mac = ? and " +
					"((first_time >= ? and first_time <= ?) or (last_time >= ? and last_time <= ?) )";//从设备表中找出该mac的开始时间。结束时间
			pstmt1 = DBManager.prepare(con, sql1);
			try {
				pstmt1.setString(1, usr_mac);
				pstmt1.setString(2, start_time);
				pstmt1.setString(3, end_time);
				pstmt1.setString(4, start_time);
				pstmt1.setString(5, end_time);
				rs1= pstmt1.executeQuery();
				while(rs1.next()){
					if(rs1.getString(1).compareTo(start_time)  < 0)//查出的时间大于给定的开始时间
						st = start_time;
					else
						st = rs1.getString(1);
					if(rs1.getString(2).compareTo(end_time)  > 0)//查出的时间大于给定的开始时间
						et = end_time;
					else
						et = rs1.getString(2);	
					
					//得出开始时间结束时间，则计算此时间段内的其他mac
					st = format_Time(st, -Integer.parseInt(timewave));
					et = format_Time(et, Integer.parseInt(timewave));
				    System.out.println("此时时间" + st + ":" + et);
					sql2 = "select usr_mac,first_time,last_time,device_mac from "+ "t_visit_info_"  + dmacs.get(i).replace(':', '_') + " where ((first_time >= ? and first_time <= ?) " +
							" or (last_time >= ? and last_time <= ?)) and usr_mac = ?";
					
			        pstmt2 = DBManager.prepare(con, sql2);
			        pstmt2.setString(1, st);
			        pstmt2.setString(2, et);
			        pstmt2.setString(3, st);
			        pstmt2.setString(4, et);
			        pstmt2.setString(5, acusr_mac);//将伴随mac作为条件来查询，只查该mac的信息
			        rs2 = pstmt2.executeQuery();
			        while(rs2.next()){//处理从此设备中查询出的所有符合条件的用户mac
			        	Visit v = new Visit();
			        	v.setUsr_mac(rs2.getString(1));
			        	v.setFirst_time(rs2.getString(2));
			        	v.setLast_time(rs2.getString(3));
			        	v.setDevice_mac(rs2.getString(4));
			        	v.setArea(mmap.get(rs2.getString(4)).getArea());
			        	v.setAddr(mmap.get(rs2.getString(4)).getAddr());
			        	if(!map.containsKey(dmacs.get(i))){
			        		listv.add(v);
			        		map.put(dmacs.get(i), listv);
			        	}
			        	else{
			        		map.get(dmacs.get(i)).add(v);
			        	}	
			        }
			        DBManager.close(rs2);
					DBManager.close(pstmt2); 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				DBManager.close(rs1);
				DBManager.close(pstmt1);
			}
		}
		DBManager.close(con);
		
		System.out.println(map.size()+"map的大小");
		//处理交集
	    List<String> macs = new ArrayList<String>();//存放map中的所有设备mac，此时每个的value是需要的伴随的mac信息
	     for(String mac : map.keySet()){
	    	 macs.add(mac);
	    }
	     
	     List<Visit> showTimeMac = new ArrayList<Visit>();
	     List<GetRouteBean> list2 = new ArrayList<GetRouteBean>();
	     Map<String,GetRouteBean> map2 = new HashMap<String,GetRouteBean>();
	     String lastdevice_mac = null;
			String laststarttime = null;
	     if(macs != null && map.size() != 0){
	    	 for(String mdmac:map.keySet()){
	    		 List<Visit> lv = map.get(mdmac);
	    	     if(lv  != null){
	    	    	 for(Visit vv:lv){
	    	    		 showTimeMac.add(vv);
	    	    	 }
	    	     }
	    	 }
	    Collections.sort(showTimeMac);//按时间出现排序，从小到大
	    
	    for(Visit r:showTimeMac){//处理输入，根据出现的时间不同，设别不同进行排序，在时间的基础上对设备进行排序
		    if(!map2.isEmpty()){
		    	
		    	if(r.getDevice_mac().equals(lastdevice_mac)){//如果出现的设备mac不变，则更新结束时
		    		map2.get(laststarttime).setEnd_time(r.getLast_time());
		    	}
		    	
		    	else{//设备mac变化
		    		map2.put(r.getFirst_time(), new GetRouteBean(r.getUsr_mac(), r.getFirst_time(), r.getLast_time(), r.getDevice_mac(), r.getArea(), r.getAddr(),r.getLatitude(),r.getLongtitude()));
		    		lastdevice_mac = r.getDevice_mac();
		    		laststarttime = r.getFirst_time();
		    	}
		    	
		    }
		    else{
		    	//首次添加map
		    	map2.put(r.getFirst_time(),new GetRouteBean(r.getUsr_mac(), r.getFirst_time(), r.getLast_time(), r.getDevice_mac(), r.getArea(), r.getAddr(),r.getLatitude(),r.getLongtitude()));
		    	lastdevice_mac = r.getDevice_mac();
		    	laststarttime = r.getFirst_time();
		  
		    }    
	}
	    for(String dmac:map2.keySet()){
		list2.add(map2.get(dmac));
	    }
	
	   Collections.sort(list2);

	   
	    }
	    
	     return list2;

	}
	
	

}

