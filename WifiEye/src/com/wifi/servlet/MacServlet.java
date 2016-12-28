package com.wifi.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.allMacDao;
import com.wifi.dao.allMacDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
/**
 * 所有mac信息的控制器
 * @author lkm
 *
 */
public class MacServlet extends HttpServlet {

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

		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String usr_mac = request.getParameter("usr_mac");
		String area = "";
		String addr = "";
		String device_name = "";
		// String device_mac = request.getParameter("device_mac");
		if (request.getParameter("area") != null
				&& !request.getParameter("area").equals("")) {
			area = URLDecoder.decode(request.getParameter("area"), "UTF-8");
		}
		if (request.getParameter("addr") != null
				&& !request.getParameter("addr").equals("")) {
			addr = URLDecoder.decode(request.getParameter("addr"), "UTF-8");
		}
		if (request.getParameter("device_name") != null
				&& !request.getParameter("device_name").equals("")) {
			device_name = URLDecoder.decode(request.getParameter("device_name"), "UTF-8");
		}
		String ap_mac = request.getParameter("ap_mac");
		allMacDao amd = new allMacDaoImpl();

		PageBean pages = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(amd.selectMac(
					start_time, end_time, usr_mac, area, addr, ap_mac,
					device_name, pages));
			int total = amd.selectMacCount(start_time, end_time, usr_mac, area,
					addr, ap_mac, device_name);
			result.put("rows", jsonArray);
			System.out.println("执行次servlet");
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
