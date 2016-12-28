package com.wifi.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public class ResponseUtil {

	/**
	 * response的返回处理函数
	 * @param response http请求的response
	 * @param o 需要打印出的数据
	 * @throws Exception 抛出异常
	 */
	public static void write(HttpServletResponse response,Object o)throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(o.toString());
		out.flush();
		out.close();
	}
}
