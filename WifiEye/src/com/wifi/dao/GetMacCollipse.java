package com.wifi.dao;

import java.util.List;

import com.wifi.model.CollipseBean;
import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;

public interface GetMacCollipse {

	public List<GetRouteBean> getcollipse(String start_time,String end_time,String arearaw,String addrraw,PageBean pages);
	public List<CollipseBean> getcollipse2(String start_time,String end_time,String arearaw,String addrraw,PageBean pages);
	
	//新算法实现碰撞，2016.04.25
	public List<GetRouteBean> getcollipseNew1(String start_time,String end_time,String arearaw,String addrraw,PageBean pages);
	
	//2016.05.03，改为处理区域地址相同，但时间不一样的情况
	public List<GetRouteBean> getcollipseNew2(String start_time,String end_time,String arearaw,String addrraw,PageBean pages);
	
	public void  stopQuery();
}
