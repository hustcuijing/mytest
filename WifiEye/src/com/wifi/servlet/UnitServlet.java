package com.wifi.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.UnitDaoImpl;
import com.wifi.dao.locationDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;
/**
 * 单位管理控制器
 * @author lkm
 *
 */
public class UnitServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	locationDaoImpl ldi = new locationDaoImpl();
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
		else if("save".equals(action))
			save(request,response);
		else if("delete".equals(action))
			delete(request,response);
		else if("modify".equals(action))
			modify(request,response);

	}
	/**
	 * 修改单位信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void modify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String town = request.getParameter("town");
		String s_location_id = request.getParameter("location_id");
		String area = province + " " + city + " " + town;
		String addr = request.getParameter("addr");
		String s_type_id = request.getParameter("location_type");
		String s_latitude = request.getParameter("latitude");
		String s_longtitude = request.getParameter("longtitude");
		int type_id = -1;
		double latitude = -1;
		double longtitude = -1;
		int location_id = -1;
		if(StringUtil.isNotEmpty(s_type_id)){
			try {
				type_id = Integer.parseInt(s_type_id);
			} catch (Exception e) {
				// TODO: handle exception
				type_id = ldi.getIdByName(s_type_id);
			}
		}
		if(StringUtil.isNotEmpty(s_latitude)){
			latitude = Double.parseDouble(s_latitude);
		}
		if(StringUtil.isNotEmpty(s_longtitude)){
			longtitude = Double.parseDouble(s_longtitude);
		}
		if(StringUtil.isNotEmpty(s_location_id)){
			location_id = Integer.parseInt(s_location_id);
		}
		Connection con=null;
		try{
			JSONObject result=new JSONObject();
			con=DBManager.getCon();
			int saveNums=0;
			if(udi.existLocationWithAddr(con,area,addr,location_id)){
					saveNums=-1;
			}else{
					saveNums=udi.locationUpdate(con,area,addr,type_id,latitude,longtitude,location_id);					
			}
			if(saveNums==-1){
				result.put("success", true);
				result.put("errorMsg", "该单位地址已经存在");
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
	 * 删除单位信息
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
			int delNums=udi.locationDelete(con, delIds);
			if(delNums>0){
				result.put("success", true);
				result.put("delNums", delNums);
			}else{
				result.put("errorMsg", "删除失败");
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
	 * 保存单位信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String town = request.getParameter("town");
		
		String area = province + " " + city + " " + town;
		String addr = request.getParameter("addr");
		String s_type_id = request.getParameter("location_type");
		String s_latitude = request.getParameter("latitude");
		String s_longtitude = request.getParameter("longtitude");
		int type_id = -1;
		double latitude = -1;
		double longtitude = -1;
		if(StringUtil.isNotEmpty(s_type_id)){
			try {
				type_id = Integer.parseInt(s_type_id);
			} catch (Exception e) {
				// TODO: handle exception
				type_id = ldi.getIdByName(s_type_id);
			}
		}
		if(StringUtil.isNotEmpty(s_latitude)){
			latitude = Double.parseDouble(s_latitude);
		}
		if(StringUtil.isNotEmpty(s_longtitude)){
			longtitude = Double.parseDouble(s_longtitude);
		}
		Connection con=null;
		try{
			JSONObject result=new JSONObject();
			con=DBManager.getCon();
			int saveNums=0;
			if(udi.existLocationWithAddr(con,area,addr)){
					saveNums=-1;
			}else{
					saveNums=udi.locationAdd(con,area,addr,type_id,latitude,longtitude);					
			}
			if(saveNums==-1){
				result.put("success", true);
				result.put("errorMsg", "该单位地址已经存在");
			}else{
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
	 * 获取所有的单位信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String area = request.getParameter("area");
		String addr = request.getParameter("addr");
		String location_type = request.getParameter("location_type");
		String s_latitude = request.getParameter("latitude");
		String s_longtitude = request.getParameter("longtitude");
		int type_id = -1;
		double latitude = -1;
		double longtitude = -1;
		if(StringUtil.isNotEmpty(location_type)){
			type_id = Integer.parseInt(location_type);
		}
		if(StringUtil.isNotEmpty(s_latitude)){
			latitude = Double.parseDouble(s_latitude);
		}
		if(StringUtil.isNotEmpty(s_longtitude)){
			longtitude = Double.parseDouble(s_longtitude);
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONObject result=new JSONObject();
			JSONArray jsonArray=JsonUtil.formatListToJsonArray(udi.getAllLocationInfo(con,area, addr, type_id, latitude, longtitude, pageBean));
			int total=udi.getUnitCount(con,area, addr, type_id, latitude, longtitude);
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

