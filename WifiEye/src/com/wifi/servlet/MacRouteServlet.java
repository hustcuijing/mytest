package com.wifi.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.wifi.dao.MacRouteDao;
import com.wifi.dao.MacRouteDaoImpl;
import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;
import com.wifi.model.UsrMacInfo;
import com.wifi.model.Visit;
import com.wifi.model.VisitBean;
import com.wifi.util.DataUtil;
import com.wifi.util.JsonUtil;
import com.wifi.util.NumberUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class MacRouteServlet extends HttpServlet {

	/**
	 * 
	 */
	MacRouteDao  mrd = new MacRouteDaoImpl();
	private static final long serialVersionUID = 1L;
	private static int total = 0;
	private  String page = "";
	private  String rows = "";
	private static String  start_time = "";
	private static String  end_time = "";
	private static String usr_mac = "";
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
		//System.out.println(action);
//		if("search".equals(action))
			list(request,response);
         }
	private void list2(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		if(StringUtil.isEmpty(page) || StringUtil.isEmpty(rows)){
			return;
		}
		else{
			page = request.getParameter("page");
			rows = request.getParameter("rows");
			PageBean pages = new PageBean(Integer.parseInt(page),
					Integer.parseInt(rows));
			List<GetRouteBean> counts = mrd.getMacRoute(start_time, end_time, usr_mac);
			try {
				JSONObject result = new JSONObject();
				JSONArray jsonArray = JsonUtil.formatListToJsonArray(counts);
				result.put("rows", jsonArray);
				if(counts.size() != 0)
				result.put("total", counts.size());
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
		
		page = request.getParameter("page");
		rows = request.getParameter("rows");
		//System.out.println(page);
		//System.out.println(rows);
		start_time = request.getParameter("start_time");
		end_time = request.getParameter("end_time");
		usr_mac = request.getParameter("usr_mac");

		if(usr_mac == null)
			return;

//		PageBean pages = new PageBean(Integer.parseInt(page),
//				Integer.parseInt(rows));
		List<GetRouteBean> counts = new ArrayList<GetRouteBean>();
//		counts = DataUtil.getInfos(usr_mac, start_time, end_time);
		MacRouteDao mrd = new MacRouteDaoImpl();
		counts = mrd.getMacRoute(start_time, end_time, usr_mac);
		if(counts == null || counts.size() == 0)
			counts.add(new GetRouteBean("", "", "没有符合条件的数据","", "", ""));
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

