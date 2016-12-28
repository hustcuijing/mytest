package com.wifi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.MacAccompany;
import com.wifi.dao.MacAccompanyImpl;
import com.wifi.model.GetRouteBean;
import com.wifi.model.Visit;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;

public class MacAccompanyInfo extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
           doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	    String usr_mac = request.getParameter("usr_mac");
	    String acusr_mac = request.getParameter("acusr_mac");
	    String start_time = request.getParameter("start_time");
	    String end_time = request.getParameter("end_time");
	    String timewave = request.getParameter("timewave");
	    String rows = request.getParameter("rows");
	    if(acusr_mac == null || acusr_mac.length() == 0)
	    	return ;
	    MacAccompany ma = new MacAccompanyImpl();
	    List<GetRouteBean> list = ma.getAccompanyMacInfo(usr_mac, acusr_mac, start_time, end_time, timewave);
	    JSONObject result = new JSONObject();
		try {
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(list);
			result.put("rows", jsonArray);
			result.put("total", list.size());
			ResponseUtil.write(response, result);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
