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

import com.wifi.dao.locationDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;

/**
 * 场所类型控制器
 */

public class LocationTypeServlet extends HttpServlet {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	locationDaoImpl ldi = new locationDaoImpl();

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
		if ("list".equals(action))
			list(request, response);
		else if ("save".equals(action))
			save(request, response);
		else if ("delete".equals(action))
			delete(request, response);
		else if ("modify".equals(action))
			modify(request, response);
		else if ("listbox".equals(action))
			listbox(request, response);

	}

	/**
	 * 修改场所类型信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void modify(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("typeid");
		String name = request.getParameter("name");
		int type_id = -1;
		if (StringUtil.isNotEmpty(name)) {
			name = URLDecoder.decode(name, "UTF-8");
		}
		if (StringUtil.isNotEmpty(id)) {
			type_id = Integer.parseInt(id);
		}
		Connection con = null;
		try {
			JSONObject result = new JSONObject();
			con = DBManager.getCon();
			int saveNums = 0;
			if (type_id != -1) {
				if (ldi.existTypeWithName(con, name)) {
					saveNums = -1;
				} else {
					saveNums = ldi.typeUpdate(con, type_id, name);
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
		} finally {
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除场所类型信息
	 * 
	 * @param request
	 * @param response
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String delIds = request.getParameter("delIds");
		Connection con = null;
		try {
			con = DBManager.getCon();
			JSONObject result = new JSONObject();
			int delNums = ldi.typeDelete(con, delIds);
			if (delNums > 0) {
				result.put("success", true);
				result.put("delNums", delNums);
			} else {
				result.put("errorMsg", "删除失败");
			}
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存场所类型信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void save(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		if (StringUtil.isNotEmpty(name)) {
			name = URLDecoder.decode(name, "UTF-8");
		}
		Connection con = null;
		try {
			JSONObject result = new JSONObject();
			con = DBManager.getCon();
			int saveNums = 0;
			if (ldi.existTypeWithName(con, name)) {
				saveNums = -1;
			} else {
				saveNums = ldi.typeAdd(con, name);
			}
			if (saveNums == -1) {
				result.put("success", true);
				result.put("errorMsg", "该场所类型已经存在");
			} else {
				result.put("success", true);
			}
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * combobox自动加载的场所类型信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void listbox(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		try {
			con = DBManager.getCon();
			JSONArray jsonArray = new JSONArray();
			JSONObject result = new JSONObject();
			result.put("name", "");
			jsonArray.add(result);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(ldi.nameList(con)));
			ResponseUtil.write(response, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取所有的场所类型信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String name = request.getParameter("name");
		PageBean pageBean = new PageBean(0, 0);
		if (StringUtil.isNotEmpty(name)) {
			name = URLDecoder.decode(name, "UTF-8");
		}
		if (StringUtil.isNotEmpty(page) && StringUtil.isNotEmpty(rows)) {
			pageBean = new PageBean(Integer.parseInt(page),
					Integer.parseInt(rows));
		}

		Connection con = null;
		try {
			con = DBManager.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(ldi.getTypeList(
					name, pageBean));
			int total = ldi.getTypeCount(con,name);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
