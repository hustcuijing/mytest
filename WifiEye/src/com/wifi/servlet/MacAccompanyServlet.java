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

import com.wifi.dao.MacAccompany;
import com.wifi.dao.MacAccompanyImpl;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;

public class MacAccompanyServlet extends HttpServlet {
	
	private MacAccompany ma = new MacAccompanyImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String usr_mac = request.getParameter("usr_mac");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String timewave = request.getParameter("timewave");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
 		String action = request.getParameter("action");
 		
 		System.out.println("传到servlet中的信息" + usr_mac + " " + start_time + " " + end_time);
 		
 		if(usr_mac == null || start_time == null || end_time == null)
 			return;//空值返回
// 		PageBean pages = new PageBean(Integer.parseInt(page),
//				Integer.parseInt(rows));
		List<Visit> counts = new ArrayList<Visit>();
		System.out.println("开始寻找伴随mac");
		counts = ma.getAccompanyMac(usr_mac,start_time,end_time,timewave);
		if(counts == null)
             return ;
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
