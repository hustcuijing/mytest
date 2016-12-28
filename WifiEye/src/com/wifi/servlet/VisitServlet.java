package com.wifi.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sun.crypto.provider.RSACipher;
import com.wifi.dao.AddrDao;
import com.wifi.dao.VisitDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.model.Visit;
import com.wifi.model.VisitInfoBean;
import com.wifi.util.JsonUtil;
import com.wifi.util.NumberUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class VisitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static int total = 0;
	private String page = "";
	private String rows = "";
	private  static String start_time = "";
	private  static String end_time = "";
	private  static String area = "";
	private  static String addr = "";
	private static String usr_mac = "";
	private static String ap_mac = "";
	private static String device_name= "";
	VisitDaoImpl vd = new VisitDaoImpl();

	private static String action = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		action = request.getParameter("action");
		if ("search".equals(action))
			list(request, response);
		else {
			list2(request, response);
		}

	}

	private void list2(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		// TODO Auto-generated method stub
        
		if (StringUtil.isEmpty(page) || StringUtil.isEmpty(rows)) {
			return;
		} else {
			page = request.getParameter("page");
			rows = request.getParameter("rows");
			PageBean pages = new PageBean(Integer.parseInt(page),
					Integer.parseInt(rows));
			List<VisitInfoBean> counts;
			if (StringUtil.isNotEmpty(area) && StringUtil.isNotEmpty(addr)){
				counts = vd.getAllVisits(start_time, end_time, usr_mac, ap_mac,
						area, addr, "", pages);
			}
			else//如果没有给区域或地址
				{
				System.out.println("无设备区域和地址");
				counts = vd.getAllVisitsByNoAddrORArea(start_time, end_time, area, addr, pages);
				}
			try {
				JSONObject result = new JSONObject();
				JSONArray jsonArray = JsonUtil.formatListToJsonArray(counts);
				result.put("rows", jsonArray);
				result.put("total", total);
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取所有的mac数据
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		page = request.getParameter("page");
		rows = request.getParameter("rows");
		start_time = request.getParameter("start_time");
		end_time = request.getParameter("end_time");

		area = request.getParameter("area");
		addr = request.getParameter("addr");
		
		System.out.println(start_time + "  " + end_time);
		System.out.println(area + "  :" + addr);
		if (StringUtil.isNotEmpty(area)) {
			area = URLDecoder.decode(area, "UTF-8");
		}
		if (StringUtil.isNotEmpty(addr)) {
			addr = URLDecoder.decode(addr, "UTF-8");
		}
 
		PageBean pages = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		List<VisitInfoBean> counts = new ArrayList<VisitInfoBean>();
		if (StringUtil.isNotEmpty(area) && StringUtil.isNotEmpty(addr)){
			counts = vd.getAllVisits(start_time, end_time, usr_mac, ap_mac,
					area, addr, "", pages);
			total = vd.getVisitCount(start_time, end_time, usr_mac, ap_mac,
					area, addr, device_name);	
		}
		else//如果没有给区域或地址
			{
			counts = vd.getAllVisitsByNoAddrORArea(start_time, end_time, area, addr, pages);
			total = NumberUtil.getNumber(start_time, end_time);
			}
		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(counts);
			if (counts.size() == 0)
				total = 0;
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
