package com.wifi.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.AppInfoDaoImpl;
import com.wifi.dao.AppTypeDao;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class AppTypeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppTypeDao atd = new AppTypeDao();
	
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
	 * 获取所有的应用类型数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

			JSONArray jsonArray = new JSONArray();
			JSONObject result = new JSONObject();
			result.put("id", "-1");
			result.put("type_name", "");
			jsonArray.add(result);
			Connection con = null;
			try {
				con = DBManager.getCon();
				jsonArray.addAll(JsonUtil.formatRsToJsonArray(atd.getAllTypes(con)));
				ResponseUtil.write(response, jsonArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				DBManager.close(con);
			}
			
		
	}

}
