package com.wifi.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBManager {

	
	private static DataSource ds = null;
    //在静态代码块中创建数据库连接池
    static{
        try{
             //初始化JNDI
            Context initCtx = new InitialContext();
             //得到JNDI容器
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
             //从JNDI容器中检索name为jdbc/datasource的数据源
            ds = (DataSource)envCtx.lookup("jdbc/datasource");
        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

	public static Connection getCon() {

		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param con
	 *            需要关闭的数据库连接
	 * @throws Exception
	 *             抛出的异常
	 */
	public static void closeCon(Connection con) throws Exception {
		if (con != null) {
			con.close();
		}
	}
	/**
	 * 获得语句执行表达式
	 * @param conn 数据库连接
	 * @param sql 查询语句
	 * @return
	 */
	public static PreparedStatement prepare(Connection conn,String sql){
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	/**
	 * 关闭连接
	 * @param conn 数据库连接
	 */
	public static void close(Connection conn){
		if(conn == null) return;
		try {
			conn.close();
			conn=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 关闭数据库表达式
	 * @param stmt 
	 */
	public static void close(Statement stmt){
		if(stmt == null) return;
		try {
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 关闭结果集
	 * @param rs 结果集
	 */
	public static void close(ResultSet rs){
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



//	public static void main(String[] args) {
//		
//		Connection con = DBManager.getCon();
//		if(con != null)
//			System.out.println("连接成功");
//		else
//			System.out.println("连接失败");
//	}












}
