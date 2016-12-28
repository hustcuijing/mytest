package com.wifi.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.MacCount;
import com.wifi.dao.MacCountImpl;
import com.wifi.model.PageBean;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

/**
 * mac统计器的控制器
 * @author lkm
 *
 */
public class MacCountServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		if(StringUtil.isEmpty(page) || StringUtil.isEmpty(rows)){
			return;
		}
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String usr_mac = request.getParameter("usr_mac");
		
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
		MacCount mc = new MacCountImpl();
		PageBean pages = new PageBean(Integer.parseInt(page),
				Integer.parseInt(rows));
		JSONObject result = new JSONObject();
		JSONArray jsonArray;
		try {
			jsonArray = JsonUtil.formatListToJsonArray(mc.countAllUniqueMac(
					start_time, end_time, usr_mac, area, addr, device_name,
					pages));
			int total = mc.countDevice(start_time, end_time, usr_mac, area,
					addr, device_name);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
