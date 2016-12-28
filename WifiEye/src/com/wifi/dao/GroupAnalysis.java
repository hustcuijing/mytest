package com.wifi.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.wifi.model.GetRouteBean;

public class GroupAnalysis {
	
	public static List<GetRouteBean> getRetain(List<GetRouteBean> l1,List<GetRouteBean> l2){
		List<GetRouteBean> list = new ArrayList<GetRouteBean>();
		for(int i = 0;i < l1.size();i++){
			for(int j = 0;j < l2.size();j++){
				//在地址相同的情况下比较时间
				if((l1.get(i).getArea() + l1.get(i).getAddr()).equals(l2.get(j).getArea() + l2.get(j).getAddr())){
					if((l1.get(i).getStart_time().compareTo(l2.get(j).getStart_time()) <= 0 && l1.get(i).getEnd_time().compareTo(l2.get(j).getEnd_time()) >= 0)
							  || (l1.get(i).getStart_time().compareTo(l2.get(j).getStart_time()) > 0 && l1.get(i).getStart_time().compareTo(l2.get(j).getEnd_time()) <= 0)
							  || (l1.get(i).getEnd_time().compareTo(l2.get(j).getStart_time()) > 0 && l1.get(i).getEnd_time().compareTo(l2.get(j).getEnd_time()) <= 0)){
						list.add(l1.get(i));
					}
				}
			}
		}
		//返回相交得到的routebean
		return list;
	}
	public static GetRouteBean getRetain(GetRouteBean g1,List<GetRouteBean> l2){
			for(int j = 0;j < l2.size();j++){
				//在地址相同的情况下比较时间
				if((g1.getArea() + g1.getAddr()).equals(l2.get(j).getArea() + l2.get(j).getAddr())){
					if((g1.getStart_time().compareTo(l2.get(j).getStart_time()) <= 0 && g1.getEnd_time().compareTo(l2.get(j).getEnd_time()) >= 0)
							  || (g1.getStart_time().compareTo(l2.get(j).getStart_time()) > 0 && g1.getStart_time().compareTo(l2.get(j).getEnd_time()) <= 0)
							  || (g1.getEnd_time().compareTo(l2.get(j).getStart_time()) > 0 && g1.getEnd_time().compareTo(l2.get(j).getEnd_time()) <= 0)){
						return l2.get(j);
					}
				}
			}
		return null;
	}
	public List<GetRouteBean> getAnalysis(String group, String start_time,
			String end_time) {
		List<String> userList = new GroupMemberDaoImpl()
				.getMemeberByGroup(group);// 获得该群组的所有用户mac
		if (userList == null || userList.size() == 0)
			return null;

		// 以用户mac为key,以该mac的轨迹为value
		Map<String, List<GetRouteBean>> routeMap = new HashMap<String, List<GetRouteBean>>();
		MacRouteDao mrd = new MacRouteDaoImpl();

		for (String usr_mac : userList) {// 查询所有mac的轨迹
			List<GetRouteBean> routeBeanList = new ArrayList<GetRouteBean>();
			routeBeanList = mrd.getMacRoute(start_time, end_time, usr_mac);
			if (routeBeanList != null || routeBeanList.size() != 0)
				routeMap.put(usr_mac, routeBeanList);
		}
        userList = null;// 释放一部分空间
		userList = new ArrayList<String>();
		if (routeMap == null || routeMap.size() == 0)
			return null;
		for(String u:routeMap.keySet()){
			userList.add(u);// 得到初步用户mac
		}
	

		// 开始计算共同出现的个数，从第一个开始轮询，逐个与后面的做相交运算，若相交为空，则继续向后，若不为空，则相交结果作为新的起点
		// 从前往后依次轮询，找到相交的用户mac最多的一组
		List<GetRouteBean> retainList1 = new ArrayList<GetRouteBean>();
		List<GetRouteBean> retainList2 = new ArrayList<GetRouteBean>();
		int maxsize = 0;
		int cursize = 0;
		int start = -1;//记录满足条件的用户mac初始序号

		for (int i = 0; i < userList.size(); i++) {
			retainList1 = routeMap.get(userList.get(i));
			
			for (int j = i + 1; j < userList.size(); j++) {
                retainList2 = getRetain(retainList1,routeMap.get(userList.get(j)));
                if(retainList2 != null && retainList2.size() != 0)//若相交的结果集不为0
                {
                	retainList1 = retainList2;//更新为结果集
                	cursize = retainList1.size();
                }  
			}
			
			if(cursize > maxsize){
				start = i;//记录开始序号
				maxsize = cursize;
			}
		}
		
		retainList1 =  null;//释放空间
		if(start == -1 || maxsize == 0)//没有在一起出现过
			return null;
		
		List<GetRouteBean> result = new ArrayList<GetRouteBean>();
		for(int i = start;i < userList.size();i++){
			GetRouteBean grb = getRetain(retainList2.get(0), routeMap.get(userList.get(i)));
			if(grb != null)
				result.add(grb);
		}
		return result;
	}
}
