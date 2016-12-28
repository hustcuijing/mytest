package com.wifi.dao;

import java.util.List;

import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;

public interface MacAccompany {
	public List<Visit> getAccompanyMac(String usr_mac,String start_time, String end_time,String timewave);//获取伴随mac
	public List<GetRouteBean> getAccompanyMacInfo(String usr_mac,String acusr_mac,String start_time, String end_time,String timewave);//获取伴随mac的详细信息
}
