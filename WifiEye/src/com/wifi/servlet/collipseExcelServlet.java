package com.wifi.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.wifi.dao.GetMacCollipse;
import com.wifi.dao.GetMacCollipseImpl;
import com.wifi.dao.MacRouteDao;
import com.wifi.dao.MacRouteDaoImpl;
import com.wifi.model.CollipseBean;
import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;


public class collipseExcelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("碰撞分析");
		
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String addr = request.getParameter("addr");
		String area = request.getParameter("area");
		
		addr = new String(addr.getBytes("ISO-8859-1"),"utf-8");
		area = new String(area.getBytes("ISO-8859-1"),"utf-8");
		String exportFileName = "碰撞" + System.currentTimeMillis() + ".xls";// 导出文件名
        GetMacCollipse gcl = new GetMacCollipseImpl();
		
        List<GetRouteBean> all = null;
			
		all = gcl.getcollipse(start_time, end_time, area, addr, new PageBean(0,0));
	
		String[] columnNames = { "用户mac", "上线时间 " , "下线时间","设备mac"  ,"设备区域" , "设备地址" };
		HSSFRow row = sheet.createRow(0); // 创建第一行 title
		HSSFCell cell = null;
		for (int i = 0; i < columnNames.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
		}
		for (int i = 0; i < all.size(); i++) {
			row = sheet.createRow(i + 1);//
			cell = row.createCell(0);
			cell.setCellValue(all.get(i).getUsr_mac());
			cell = row.createCell(1);
			cell.setCellValue(all.get(i).getStart_time());
			cell = row.createCell(2);
			cell.setCellValue(all.get(i).getEnd_time());
			cell = row.createCell(3);
			cell.setCellValue(all.get(i).getDevice_mac());
			cell = row.createCell(4);
			cell.setCellValue(all.get(i).getArea());
			cell = row.createCell(5);
			cell.setCellValue(all.get(i).getAddr());
		
		}
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String((exportFileName).getBytes(), "ISO8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
	}
}
