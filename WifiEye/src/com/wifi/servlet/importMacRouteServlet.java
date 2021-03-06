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

import com.wifi.dao.MacRouteDao;
import com.wifi.dao.MacRouteDaoImpl;
import com.wifi.model.GetRouteBean;


public class importMacRouteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		String exportFileName = "E:\\_" + System.currentTimeMillis() + ".xls";// 导出文件名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("mac轨迹");
		
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String usr_mac = request.getParameter("usr_mac");
		String exportFileName = usr_mac+"轨迹" + System.currentTimeMillis() + ".xls";// 导出文件名
//		allMacDao amd = new allMacDaoImpl();
        MacRouteDao mrd = new MacRouteDaoImpl();
		
		List<GetRouteBean> all = null;
			
		all = mrd.getMacRoute(start_time, end_time, usr_mac);
	
		String[] columnNames = { "用户地址", "进入时间", "离开时间", "监控设备", "监控区域", "单位地址",
				"经度", "纬度" };
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
			cell = row.createCell(6);
			cell.setCellValue(all.get(i).getLongtitude());
			cell = row.createCell(7);
			cell.setCellValue(all.get(i).getLatitude());	
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
