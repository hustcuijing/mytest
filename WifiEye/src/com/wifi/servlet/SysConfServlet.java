package com.wifi.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.hwpf.model.SavedByEntry;

import com.wifi.model.Sys;
import com.wifi.util.ResponseUtil;
import com.wifi.util.SysConfUtil;


public class SysConfServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		if("list".equals(action)){
			list(request,response);
		}else if("save".equals(action)){
			save(request,response);
		}
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String wpaLiveTime;
		String timeServer;
		String url;
		String username;
		String password;
		String database;
		String[] channelStayTimes = new String[13];
		String[] strs = {"one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve","thirteen"};
		wpaLiveTime = request.getParameter("wpaLiveTime");
		timeServer = request.getParameter("timeServer");
		url = request.getParameter("url");
		username = request.getParameter("username");
		password = request.getParameter("password");
		database = request.getParameter("database");
		for(int i = 0;i < strs.length;i++){
			channelStayTimes[i] = request.getParameter(strs[i]);
		}
		JSONObject result=new JSONObject();
		int res = SysConfUtil.setAll(wpaLiveTime, url, username, password, database, timeServer, channelStayTimes);
		if(res == -1)
			result.put("errorMsg","保存失败");
		else 
			result.put("success", true);
		try {
			ResponseUtil.write(response,result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void list(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String wpaLiveTime;
		String timeServer;
		String url;
		String username;
		String password;
		String database;
		String[] channelStayTimes = new String[13];
		SysConfUtil.getConf();
		wpaLiveTime = SysConfUtil.getWpaLiveTime();
		timeServer = SysConfUtil.getTimeServer();
		url = SysConfUtil.getUrl();
		username = SysConfUtil.getUsername();
		password = SysConfUtil.getPassword();
		database = SysConfUtil.getDatabase();
		channelStayTimes = SysConfUtil.getChannelStayTimes();
		JSONObject result=new JSONObject();
		Sys sys = new Sys(wpaLiveTime, timeServer, url, username, password, database, channelStayTimes);
		result.put("data",sys);
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
