package com.wifi.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.AddrDao;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;
/**
 * 地址控制器
 * @author lkm
 *
 */
public class AddrServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AddrDao addrDao = new AddrDao();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
          doPost(request, response);
		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String action=request.getParameter("action");
		if("area".equals(action)){
			areaList(request, response);			
		}else if("addr".equals(action)){
			addrList(request, response);
		}else if("device".equals(action)){
			deviceList(request, response);
		}else if("notused".equals(action)){
			deviceNotUsesList(request,response);
		}else if("ip".equals(action)){
			ipList(request,response);
		}
		
	}

	/**
	 * 获取所有的ip数据
	 * @param request
	 * @param response
	 */
	private void ipList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("ip", "");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(addrDao.ipList(con)));
			ResponseUtil.write(response, jsonArray);
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
	 * 获取没有分配地址的设备数据
	 * @param request
	 * @param response
	 */
	private void deviceNotUsesList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("device_name", "");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(addrDao.deviceList(con)));
			ResponseUtil.write(response, jsonArray);
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
	 * 获取所有设备信息
	 * @param request
	 * @param response
	 */
	private void deviceList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String area = request.getParameter("area");
		String addr = request.getParameter("addr");
		try {
			if(StringUtil.isNotEmpty(area))
				area = URLDecoder.decode(area,"UTF-8");
			if(StringUtil.isNotEmpty(addr))
				addr = URLDecoder.decode(addr,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("device_name", "");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(addrDao.deviceList(con,null,area,addr)));
			ResponseUtil.write(response, jsonArray);
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
	 * 获取详细地址信息
	 * @param request
	 * @param response
	 */
	private void addrList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String area = request.getParameter("area");
		try {
			if(StringUtil.isNotEmpty(area))
				area = URLDecoder.decode(area,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("addr", "");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(addrDao.addrList(con,null,area)));
			ResponseUtil.write(response, jsonArray);
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
	 * 获取地址区域信息
	 * @param request
	 * @param response
	 */
	private void areaList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("area", "");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(addrDao.areaList(con,null,null)));
			ResponseUtil.write(response, jsonArray);
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
