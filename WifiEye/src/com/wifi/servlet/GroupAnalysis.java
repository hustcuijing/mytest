package com.wifi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class GroupAnalysis extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String group = request.getParameter("group");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String rows = request.getParameter("rows");
		if (StringUtil.isNotEmpty(group)) {
			try {
				group = URLDecoder.decode(group, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return ;
		}
		System.out.println(group + "  " + start_time + "  " + end_time);
		com.wifi.dao.GroupAnalysis ga = new com.wifi.dao.GroupAnalysis();
		try {
			JSONObject result = new JSONObject();
			JSONArray array = JsonUtil.formatListToJsonArray(ga.getAnalysis(group, start_time, end_time));
			result.put("rows", array);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
