package com.wifi.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.RaspStatusDaoImpl;
import com.wifi.model.DeviceBean;
import com.wifi.model.PageBean;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
public class RaspStatusServlet  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RaspStatusDaoImpl rsd = new RaspStatusDaoImpl();
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
		String action = request.getParameter("action");
		if("updateStatus".equals(action)){
			updateStatus(request,response);
		}else if ("list".equals(action)) {
			list(request,response);
		}

	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String area = "";
		String addr = "";
		String device_name = "";
		String ip = request.getParameter("ip");
		int status = 0;
		if (request.getParameter("area") != null
				&& !request.getParameter("area").equals("")) {
			area = URLDecoder.decode(request.getParameter("area"), "UTF-8");
		}
		if (request.getParameter("addr") != null
				&& !request.getParameter("addr").equals("")) {
			addr = URLDecoder.decode(request.getParameter("addr"), "UTF-8");
		}
		if (request.getParameter("device_name") != null
				&& !request.getParameter("device_name").equals(""))
			device_name = URLDecoder.decode(request.getParameter("device_name"), "UTF-8");
		if (request.getParameter("status") != null
				&& !request.getParameter("status").equals(""))
			status = Integer.parseInt(request.getParameter("status"));
		PageBean pages = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		List<DeviceBean> devices = rsd.getDeviceInfo(area, addr, device_name, pages, ip,status-1);
		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(devices);
			int total = rsd.selectDevcieCount(area, addr, device_name,ip,status-1);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//更新设备状态，主要用于更新失效的状态无离线
	private void updateStatus(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Integer> updatesMap=new HashMap<String,Integer>();
		List<DeviceBean> devices=rsd.getDeviceInfo("", "", "",null,"", -1);
		for(DeviceBean d:devices){
			String status_time=d.getStatus_time();
			int cur_status=d.getStatus();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//检测更新时间以判断状态是否有效
			try {
				if(null==status_time||new Date().getTime()-sdf.parse(status_time).getTime()>3600000){//时间间隔大于1小时则按异常处理
					if(cur_status!=0){
						updatesMap.put(d.getIp(),0);
					}
				}
			} catch (ParseException e) {
				if(cur_status!=0){
					updatesMap.put(d.getIp(),0);
				}
			}			
		}
		rsd.updateStatus(updatesMap);
		JSONObject result=new JSONObject();
		result.put("success",true);
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
