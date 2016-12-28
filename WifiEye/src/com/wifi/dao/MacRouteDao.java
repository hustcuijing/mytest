package com.wifi.dao;

import java.util.List;

import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;


public interface MacRouteDao {

	//获取mac轨迹
	public List<GetRouteBean> getMacRoute(String start_time, String end_time,String usr_mac);
}
