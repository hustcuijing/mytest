package com.wifi.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hdf.extractor.data.DOP;

import com.wifi.dao.GetMacCollipse;
import com.wifi.dao.GetMacCollipseImpl;

public class StopQuery extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
         System.out.println("传入servlet!!!");
         GetMacCollipse gc = new GetMacCollipseImpl();
         gc.stopQuery();
	}

}
