package com.wifi.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wifi.dao.RoleDao;
import com.wifi.dao.UserDao;
import com.wifi.model.PageBean;
import com.wifi.model.Role;
import com.wifi.util.DBManager;
import com.wifi.util.JsonUtil;
import com.wifi.util.ResponseUtil;
import com.wifi.util.StringUtil;
/**
 * 角色控制器
 * @author Administrator
 *
 */
public class RoleServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RoleDao roleDao=new RoleDao();
	UserDao userDao=new UserDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action=request.getParameter("action");
		if("comBoList".equals(action)){
			comBoList(request, response);			
		}else if("list".equals(action)){
			roleList(request, response);		
		}else if("delete".equals(action)){
			roleDelete(request, response);		
		}else if("save".equals(action)){
			roleSave(request, response);		
		}else if("auth".equals(action)){
			auth(request, response);		
		}
	}
	/**
	 * combobox绑定的角色信息数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void comBoList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("roleId", "");
			jsonObject.put("roleName", "全部");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(roleDao.roleList(con,null,new Role())));
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
	 * 获取所有的角色list
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void roleList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		Role role=new Role();
		String s_roleName=request.getParameter("s_roleName");
		if(StringUtil.isNotEmpty(s_roleName)){
			role.setRoleName(s_roleName);
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONObject result=new JSONObject();
			JSONArray jsonArray=JsonUtil.formatRsToJsonArray(roleDao.roleList(con, pageBean,role));
			int total=roleDao.roleCount(con,role);
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
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void roleDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String delIds=request.getParameter("delIds");
		Connection con=null;
		try{
			con=DBManager.getCon();
			JSONObject result=new JSONObject();
			String str[]=delIds.split(",");
			for(int i=0;i<str.length;i++){
				boolean f=userDao.existUserWithRoleId(con, str[i]);
				if(f){
					result.put("errorIndex", i);
					result.put("errorMsg", "用户不存在该权限");
					ResponseUtil.write(response, result);
					return;
				}
			}
			int delNums=roleDao.roleDelete(con, delIds);
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
	 * 保存角色
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	
	private void roleSave(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String roleName=request.getParameter("roleName");
		String roleDescription=request.getParameter("roleDescription");
		String roleId=request.getParameter("roleId");
		Role role=new Role(roleName, roleDescription);
		if(StringUtil.isNotEmpty(roleId)){
			role.setRoleId(Integer.parseInt(roleId));
		}
		Connection con=null;
		try{
			JSONObject result=new JSONObject();
			con=DBManager.getCon();
			int saveNums=0;
			if(StringUtil.isNotEmpty(roleId)){
				saveNums=roleDao.roleUpdate(con, role);
			}else{
				saveNums=roleDao.roleAdd(con, role);					
			}
            if(saveNums>0){
            	result.put("success", true);
			}else{
				result.put("success", true);
				result.put("errorMsg", "添加失败");
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
	 * 根据roleId获取权限
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void auth(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String roleId=request.getParameter("roleId");
		String authIds=request.getParameter("authIds");
		Role role=new Role(Integer.parseInt(roleId), authIds);
		Connection con=null;
		try{
			JSONObject result=new JSONObject();
			con=DBManager.getCon();
			int updateNums=roleDao.roleAuthIdsUpdate(con, role);
            if(updateNums>0){
            	result.put("success", true);
			}else{
				result.put("errorMsg", "赋权失败");
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
}
