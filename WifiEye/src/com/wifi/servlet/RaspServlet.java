package com.wifi.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.RaspDaoImpl;
import com.wifi.dao.locationDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;
/**
 * 设备录入的控制器
 * @author lkm
 *
 */
public class RaspServlet  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	locationDaoImpl ldi = new locationDaoImpl();
	RaspDaoImpl rdi = new RaspDaoImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * 
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
		else if("save".equals(action))
			save(request,response);
		else if("delete".equals(action))
			delete(request,response);
		else if("modify".equals(action))
			modify(request,response);
		else if("name".equals(action))
			getAllName(request,response);

	}
	public void getAllName(HttpServletRequest request, HttpServletResponse response){
		try {
			JSONArray jsonArray= new JSONArray();
			JSONObject ob = new JSONObject();
			ob.put("device_name", "");
			jsonArray.add(ob);
			jsonArray.addAll(JsonUtil.formatListToJsonArray(rdi.getAllRasp()));
			ResponseUtil.write(response, jsonArray);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 修改设备
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void modify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String device_name = request.getParameter("device_name");
		String device_mac = request.getParameter("device_mac");
		String device_id = request.getParameter("id");
		if(StringUtil.isNotEmpty(device_id)){
			device_id = URLDecoder.decode(device_id, "UTF-8");
		}
		Connection con=null;
		try{
			JSONObject result=new JSONObject();
			con=DBManager.getCon();                            
			int saveNums=0;
			if(rdi.raspExistUpdate(device_mac,device_name,device_id)){
					saveNums=-1;
			}else{
					saveNums=rdi.raspUpdate(device_mac,device_name,device_id);					
			}
			if(saveNums==-1){
				result.put("success", true);
				result.put("errorMsg", "该场所类型已经存在");
			}else if(saveNums == 0){
				result.put("success", true);
				result.put("errorMsg", "数据异常");
			}
			else{
				result.put("success", true);
			}
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 删除设备
	 * @param request
	 * @param response
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String delIds = request.getParameter("delIds");
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONObject result=new JSONObject();
			int delNums=rdi.raspDelete(delIds);
			if(delNums == 1){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存设备信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String device_name = request.getParameter("device_name");
		String device_mac = request.getParameter("device_mac");
		Connection con=null;
		try{
			JSONObject result=new JSONObject();
			con=DBManager.getCon();
			int saveNums=0;
			if(rdi.raspExist(device_name,device_mac)){
					saveNums=-1;
			}else{
					saveNums=rdi.raspAdd(device_mac,device_name);					
			}
			if(saveNums==-1){
				result.put("success", true);
				result.put("errorMsg", "该场所类型已经存在");
			}else if(saveNums == 0){
				result.put("success", true);
				result.put("errorMsg", "数据异常");
			}
			else{
				result.put("success", true);
			}
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取所有的设备信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String device_name = request.getParameter("device_name");
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		System.out.println("device_name" + device_name);
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONObject result=new JSONObject();
			JSONArray jsonArray=JsonUtil.formatRsToJsonArray(rdi.getRaspList(con,device_name,pageBean));
			int total=rdi.getRaspCount(con,device_name);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(response, result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
