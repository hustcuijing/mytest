package com.wifi.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.wifi.dao.allMacDao;
import com.wifi.dao.allMacDaoImpl;
import com.wifi.model.PageBean;
import com.wifi.model.VisitBean;
import com.wifi.util.DBManager;
/**
 * 导出监控记录的控制器
 * @author Administrator
 *
 */
public class ImportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ImportServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String exportFileName = "E:\\信息导出_" + System.currentTimeMillis() + ".xls";// 导出文件名

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("监控记录");
		// wb.setSheetName(0, "会议记录清单");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String usr_mac = request.getParameter("usr_mac");
		String area = "";
		String addr = "";
		String device_name = "";
		// String device_mac = request.getParameter("device_mac");
		if (request.getParameter("area") != null
				&& !request.getParameter("area").equals("")) {
			area = new String(request.getParameter("area").getBytes(
					"ISO-8859-1"), "UTF-8");
		}
		if (request.getParameter("addr") != null
				&& !request.getParameter("addr").equals("")) {
			addr = new String(request.getParameter("addr").getBytes(
					"ISO-8859-1"), "UTF-8");
		}
		if (request.getParameter("device_name") != null
				&& !request.getParameter("device_name").equals("")) {
			device_name = new String(request.getParameter("device_name")
					.getBytes("ISO-8859-1"), "UTF-8");
		}
		String ap_mac = request.getParameter("ap_mac");
		allMacDao amd = new allMacDaoImpl();

		PageBean pages = new PageBean(0, 0);
		Connection con = null;
		List<VisitBean> all = null;
		try {
			con = DBManager.getCon();
			all = amd.selectMac(start_time, end_time, usr_mac, area, addr,
					ap_mac, device_name, pages);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBManager.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String[] columnNames = { "监控设备", "监控地址", "时间", "用户地址", "AP地址", "传输速率",
				"信号强度", "应用类型", "应用信息", "信道", "厂商" };
		HSSFRow row = sheet.createRow(0); // 创建第一行 title
		HSSFCell cell = null;
		for (int i = 0; i < columnNames.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
		}
		for (int i = 0; i < all.size(); i++) {
			row = sheet.createRow(i + 1);//
			cell = row.createCell(0);
			cell.setCellValue(all.get(i).getDevice_name());
			cell = row.createCell(1);
			cell.setCellValue(all.get(i).getAddress());
			cell = row.createCell(2);
			cell.setCellValue(all.get(i).getRecordtime());
			cell = row.createCell(3);
			cell.setCellValue(all.get(i).getUsr_mac());
			cell = row.createCell(4);
			cell.setCellValue(all.get(i).getAp_mac());
			cell = row.createCell(5);
			cell.setCellValue(all.get(i).getData_rate());
			cell = row.createCell(6);
			cell.setCellValue(all.get(i).getRssi_signal());
			cell = row.createCell(7);
			cell.setCellValue(all.get(i).getApp_type());
			cell = row.createCell(8);
			cell.setCellValue(all.get(i).getApp_info());
			cell = row.createCell(9);
			cell.setCellValue(all.get(i).getChannel_id());
			cell = row.createCell(10);
			cell.setCellValue(all.get(i).getFirm());

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
