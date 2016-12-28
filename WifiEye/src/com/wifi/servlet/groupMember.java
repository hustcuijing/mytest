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

import com.wifi.dao.GroupMemberDao;
import com.wifi.dao.GroupMemberDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class groupMember extends HttpServlet {

	public static GroupMemberDao gmd = new GroupMemberDaoImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if (action.equals("list"))
			list(request, response);
		else if (action.equals("save"))
			save(request, response);
		else if ("modify".equals(action))
			modify(request, response);
		else if ("delete".equals(action))
			delete(request, response);
	}

	public static void list(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		if (name == null || name.equals(""))
			return;
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		PageBean pageBean = new PageBean(0, 0);
		if (StringUtil.isNotEmpty(name)) {
			try {
				name = URLDecoder.decode(name, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(page) && StringUtil.isNotEmpty(rows)) {
			pageBean = new PageBean(Integer.parseInt(page),
					Integer.parseInt(rows));
		}

		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(gmd.getMember(
					name, pageBean));
			int total = gmd.getMemeberCount(name);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void save(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		System.out.println(name);
		String usr_mac = request.getParameter("usr_mac");
		JSONObject result = new JSONObject();
		if (StringUtil.isNotEmpty(name)) {

			try {
				name = URLDecoder.decode(name, "UTF-8");
				System.out.println(name);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			
			result.put("errorMsg", "请选择群组类型");
			try {
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int saveNums = 0;
		if (gmd.isExitsMember(usr_mac, name)) {
			saveNums = -1;
		} else {
			gmd.addMember(usr_mac, name);
		}
		if (saveNums == -1) {
			result.put("success", true);
			result.put("errorMsg", "群组中该成员已经存在");
		} else {
			result.put("success", true);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void modify(HttpServletRequest request,
			HttpServletResponse response) {
		
	}

	public static void delete(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("delNames");
		String macs = request.getParameter("delMacs");
		System.out.println(name + "@@" + macs);
		if (StringUtil.isNotEmpty(name)) {
			try {
				name = URLDecoder.decode(name, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			JSONObject result = new JSONObject();
			int delNums = gmd.deleteMember(name, macs);
			if (delNums > 0) {
				result.put("success", true);
				result.put("delNums", delNums);
			} else {
				result.put("errorMsg", "删除失败");
			}
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
