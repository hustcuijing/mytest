package com.wifi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.GetMacCollipse;
import com.wifi.dao.GetMacCollipseImpl;
import com.wifi.model.CollipseBean;
import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.util.JsonUtil;
import com.wifi.util.NumberUtil;
import com.wifi.util.ResponseUtil;

public class MacCollipse extends HttpServlet {

	/**
	 * 
	 */
	GetMacCollipse gmc = new GetMacCollipseImpl();
	private static final long serialVersionUID = 1L;
	private static String area = "";
	private static String addr = "";
	private static String start_time = "";
	private static String end_time = "";
	private static int total = 0;
	private  String page = "";
	private  String rows = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
          area = request.getParameter("area");
         addr = request.getParameter("addr");
         start_time = request.getParameter("start_time");
         end_time = request.getParameter("end_time");
         page = request.getParameter("page");
 		rows = request.getParameter("rows");
 		String action = request.getParameter("action");
 		if(start_time == null || start_time.length() == 0)
 			return ;
// 		System.out.println(area);
// 		System.out.println(addr);
 		
 		
// 		PageBean pages = new PageBean(Integer.parseInt(page),
//				Integer.parseInt(rows));
		List<GetRouteBean> counts = new ArrayList<GetRouteBean>();
		counts = gmc.getcollipseNew2(start_time, end_time,area, addr, new PageBean(0, 0));
		
		if(counts == null){
			counts = new ArrayList<GetRouteBean>();
            counts.add(new GetRouteBean("", "", "没有符合条件的数据", "", "", ""));
			}
		else
			System.out.println("有" + counts.size());

//		for(Visit v : counts){
//			System.out.println(v.getUsr_mac());
//		}
		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(counts);
			//total = vd.getVisitCount(start_time, end_time, usr_mac, ap_mac, area, addr, device_name);
//			total = NumberUtil.getNumber(start_time,end_time);
			result.put("rows", jsonArray);
			result.put("total", counts.size());
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
 		
	}

}
