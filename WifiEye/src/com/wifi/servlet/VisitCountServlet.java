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

import com.wifi.dao.AddrDao;
import com.wifi.dao.VisitCountDaoImpl;
import com.wifi.dao.firmDao;
import com.wifi.model.Address;
import com.wifi.model.PageBean;
import com.wifi.model.VisitCount;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class VisitCountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		System.out.println("action:" + action);
		if("list".equals(action)){
			list(request,response);
		}else if("unique".equals(action)){
			unique(request,response);
		}
		
	}

	private void unique(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String device_mac = request.getParameter("device_mac");
		
		JSONObject result = new JSONObject();
		VisitCountDaoImpl vd = new VisitCountDaoImpl();
		try {
			int unique = vd.getUniqueMacs(start_time, end_time, device_mac);
			result.put("unique", unique);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		
		String area = request.getParameter("area");
		String addr = request.getParameter("addr");
		String device_name = request.getParameter("device_name");
		if (StringUtil.isNotEmpty(area)) {
			area = URLDecoder.decode(area, "UTF-8");
		}
		if (StringUtil.isNotEmpty(addr)) {
			addr = URLDecoder.decode(addr, "UTF-8");
		}
		if (StringUtil.isNotEmpty(device_name)) {
			device_name = URLDecoder.decode(device_name, "UTF-8");
		}
		VisitCountDaoImpl vc = new VisitCountDaoImpl();
		AddrDao ad = new AddrDao();
		PageBean pages = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		JSONObject result = new JSONObject();
		JSONArray jsonArray;
		
		
			
		List<VisitCount> counts = new ArrayList<VisitCount>();
		int total = 0;
			
		
		StringBuilder device_mac_strs = new StringBuilder("(");
		if(device_name != null && !device_name.equals("")){
			String tmp = "'"+device_name + "')";
			device_mac_strs.append(tmp);
			System.out.println(device_name);
			total = 1;
		}
		else{
			List<String> device_macs = ad.deviceList(area, addr);
		int i = 0;
		for(;i < device_macs.size() - 1;i++){
			String tmp = "'"+device_macs.get(i) + "',";
			device_mac_strs.append(tmp);
			
		}
		String tmp = "'" + device_macs.get(i) + "')";
		device_mac_strs.append(tmp);
		total = device_macs.size();
		}
		counts = vc.getAllVisitCounts2(start_time, end_time, device_mac_strs.toString(), pages);
		for(VisitCount vcCount : counts){
			Address address = firmDao.getDeviceAddress(vcCount.getDevice_name());
			if(address != null){
				vcCount.setAddr(address.getAddr());
				vcCount.setArea(address.getArea());
			}
		}
		try {
			jsonArray = JsonUtil.formatListToJsonArray(counts);
			
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

