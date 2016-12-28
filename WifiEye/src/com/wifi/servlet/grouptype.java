package com.wifi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.GroupDao;
import com.wifi.dao.GroupDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

public class grouptype extends HttpServlet {
    static GroupDao gd = new GroupDaoImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if(action.equals("listbox"))
			nameList(request, response);
		else if(action.equals("list"))
			list(request,response);
		else if(action.equals("save"))
			save(request, response);
		else if ("modify".equals(action))
			modify(request, response);
		else if ("delete".equals(action))
			delete(request, response);
			

	}
	public static void  nameList(HttpServletRequest request, HttpServletResponse response){

			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject result = new JSONObject();
				result.put("name", "");
				jsonArray.add(result);
				jsonArray.addAll(JsonUtil.formatListToJsonArray(gd.getNameList()));
				ResponseUtil.write(response, jsonArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
    public static void list(HttpServletRequest request, HttpServletResponse response){
    	String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String name = request.getParameter("name");
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
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(gd.getNameListMain(name, pageBean));
			int total = gd.listCount(name);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public static void save(HttpServletRequest request, HttpServletResponse response){
    	String name = request.getParameter("name");
		try {
			if (StringUtil.isNotEmpty(name)) {
				name = URLDecoder.decode(name, "UTF-8");
			}
			
				JSONObject result = new JSONObject();
				int saveNums = 0;
				if (gd.isExitsGroupName(name)) {
					saveNums = -1;
				} else {
					gd.addGroupName(name);
				}
				if (saveNums == -1) {
					result.put("success", true);
					result.put("errorMsg", "该场所类型已经存在");
				} else {
					result.put("success", true);
				}
				ResponseUtil.write(response, result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }
    public static void modify(HttpServletRequest request, HttpServletResponse response){
    	String id = request.getParameter("typeid");
		String name = request.getParameter("name");
		int type_id = -1;
		
		try {
			if (StringUtil.isNotEmpty(name)) {
				name = URLDecoder.decode(name, "UTF-8");
			}
			if (StringUtil.isNotEmpty(id)) {
				type_id = Integer.parseInt(id);
			}
			JSONObject result = new JSONObject();

			int saveNums = 0;
			if (type_id != -1) {
				if (gd.isExitsGroupName(name)) {
					saveNums = -1;
				} else {
					gd.updateGroupName(type_id, name);
				}
			} else {
				saveNums = -2;
			}
			if (saveNums == -1) {
				result.put("success", true);
				result.put("errorMsg", "该场所类型已经存在");
			} else if (saveNums == -2) {
				result.put("success", true);
				result.put("errorMsg", "数据异常");
			} else {
				result.put("success", true);
			}
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    public static void delete(HttpServletRequest request, HttpServletResponse response){
    	String delIds = request.getParameter("delIds");
		try {
			JSONObject result = new JSONObject();
			int delNums = gd.deleteGroupName(delIds);
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
