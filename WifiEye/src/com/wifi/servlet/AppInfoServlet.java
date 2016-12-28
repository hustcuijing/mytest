package com.wifi.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.AddrDao;
import com.wifi.dao.AppInfoDaoImpl;
import com.wifi.dao.VisitDaoImpl;
import com.wifi.dao.allMacDao;
import com.wifi.dao.allMacDaoImpl;
import com.wifi.dao.deviceDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.JsonUtil;
import com.wifi.util.NumberUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class AppInfoServlet extends HttpServlet {

	/**
	 * 
	 */
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
		
		if(action == null)
			return;
		if(action.equals("action"))
		  list(request, response);


	}
	/**
	 * 获取所有的mac数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
        System.out.println("执行list");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String area = request.getParameter("area");
		String addr = request.getParameter("addr");

		int app_type = -1;
		// String device_mac = request.getParameter("device_mac");
//		if(StringUtil.isEmpty(start_time)&&StringUtil.isEmpty(end_time)&&StringUtil.isEmpty(area)
//				&&StringUtil.isEmpty(addr))
//			return;
		if (StringUtil.isNotEmpty(area)) {
			area = URLDecoder.decode(area, "UTF-8");
		}
		if (StringUtil.isNotEmpty(addr)) {
			addr = URLDecoder.decode(addr, "UTF-8");
		}

		if(StringUtil.isNotEmpty(request.getParameter("app_info")) && request.getParameter("app_info").length() != 0)
			app_type = Integer.parseInt(request.getParameter("app_info"));
		System.out.println("应用信息类型" + request.getParameter("app_info"));
		AppInfoDaoImpl vd = new AppInfoDaoImpl();
        
		PageBean pages = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(vd.getAllAppInfos(start_time, end_time, area, addr, app_type, pages));
			int total = 0;
			total = NumberUtil.getNumber(start_time, end_time);
			   if(StringUtil.isEmpty(addr))//地址为空
			   {
					total /= new deviceDaoImpl().getTableCount();
					if(StringUtil.isNotEmpty(request.getParameter("app_info")))
						total /= 5;
			   }
			   else 
				   total = vd.getAllCount(start_time, end_time, area, addr, app_type);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}

