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

import com.wifi.dao.UnitDaoImpl;
import com.wifi.dao.deviceDao;
import com.wifi.dao.deviceDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
/**
 * 设备管理控制器
 * @author lkm
 *
 */
public class DeviceServlet extends HttpServlet {


	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	deviceDao dd = new deviceDaoImpl();
	UnitDaoImpl udi = new UnitDaoImpl();
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
		if("list".equals(action))
			list(request, response);
		else if("save".equals(action)){
			save(request,response);
		}else if("modify".equals(action)){
			modify(request,response);
		}else if("delete".equals(action)){
			delete(request,response);
		}

	}
	/**
	 * 删除设备
	 * @param request
	 * @param response
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		String delIds = request.getParameter("delIds");
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONObject result=new JSONObject();
			int delNums=dd.deviceDelete(delIds);
			if(delNums > 0){
				result.put("success", true);
				result.put("delNums", delNums);
			}else if(delNums == -1)
			{
				result.put("success", true);
				result.put("errorMsg", "删除失败");
			}else {
				result.put("errorMsg", "数据异常");
			}
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 修改设备信息
	 * @param request
	 * @param response
	 */
	private void modify(HttpServletRequest request, HttpServletResponse response) {
		String ip = request.getParameter("ip");
		String device_name = request.getParameter("device_name");
		String area = request.getParameter("area");
		String addr = request.getParameter("addr");
		String id = request.getParameter("id");
		int location_id = udi.getIdWithAreaAddr(area, addr);
		try{
			JSONObject result=new JSONObject();
			int saveNums=0;
			if(dd.deviceExistWithIp(ip,id)){
				saveNums = -1;
			}else {
				saveNums=dd.deviceAdd(device_name,ip,location_id);
			}
								
			if(saveNums == 0){
				result.put("success", true);
				result.put("errorMsg", "数据异常");
			}else if(saveNums == -1){
				result.put("success", true);
				result.put("errorMsg", "该IP已经被使用");
			}
			else{
				result.put("success", true);
			}
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 保存设备信息
	 * @param request
	 * @param response
	 */
	private void save(HttpServletRequest request, HttpServletResponse response) {
		String ip = request.getParameter("ip");
		String device_name = request.getParameter("device_name");
		String area = request.getParameter("area");
		String addr = request.getParameter("addr");
		String id = request.getParameter("id");
		int location_id = udi.getIdWithAreaAddr(area, addr);
		try{
			JSONObject result=new JSONObject();
			int saveNums=0;
			if(dd.deviceExistWithIp(ip,id)){
				saveNums = -1;
			}else {
				saveNums=dd.deviceAdd(device_name,ip,location_id);
			}
								
			if(saveNums == 0){
				result.put("success", true);
				result.put("errorMsg", "数据异常");
			}else if(saveNums == -1){
				result.put("success", true);
				result.put("errorMsg", "该IP已经被使用");
			}
			else{
				result.put("success", true);
			}
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有设备的具体信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String area = "";
		String addr = "";
		String device_name = "";
		String ip = request.getParameter("ip");
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
		PageBean pages = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		try {
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatListToJsonArray(dd.getDeviceInfo(area, addr, device_name, pages,ip));
			int total = dd.selectDevcieCount(area, addr, device_name,ip);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
