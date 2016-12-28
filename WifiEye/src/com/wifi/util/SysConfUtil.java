package com.wifi.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SysConfUtil {

	private static String wpaLiveTime;
	private static String timeServer;
	private static String url;
	private static String username;
	private static String password;
	private static String database;
	private static String[] channelStayTimes = new String[13];

	public static String getWpaLiveTime() {
		return wpaLiveTime;
	}

	public static String getTimeServer() {
		return timeServer;
	}

	public static String getUrl() {
		return url;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static String getDatabase() {
		return database;
	}

	public static String[] getChannelStayTimes() {
		return channelStayTimes;
	}

	public static void getConf() {
		setWpaLiveTime();
		setTimeServer();
		setStayTimes();
		setDbConf();
	}

	public static void setWpaLiveTime() {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/WpaLiveTime.properties");
		try {
			prop.load(in);
			wpaLiveTime = prop.getProperty("wpaLiveTime");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void setDbConf() {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/DataBase.properties");
		try {
			prop.load(in);
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			database = prop.getProperty("database");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void setTimeServer() {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/TimeServer.properties");
		try {
			prop.load(in);
			timeServer = prop.getProperty("timeServer");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void setStayTimes() {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/ChannelStayTime.properties");
		try {
			prop.load(in);
			int i = 0;
			for (; i < 13; i++) {
				String key = String.valueOf(i + 1);
				channelStayTimes[i] = prop.getProperty(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static int setAll(String wpaLiveTime2, String url2, String username2,
			String password2, String database2, String timeServerIp,
			String[] stayTime) {
		int result = 0;
		try {
			setWpaLiveTime(wpaLiveTime2);
			setDbConf(url2, username2, password2, database2);
			setStayTimes(stayTime);
			setTimeServer(timeServerIp);
		} catch (IOException e) {
			result = -1;
		}
		return result;
	}

	public static void setWpaLiveTime(String wpaLiveTime2) throws IOException {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/WpaLiveTime.properties");
		prop.load(in);
		in.close();
		prop.setProperty("wpaLiveTime", wpaLiveTime2);
		String path = SysConfUtil.class.getResource("/WpaLiveTime.properties").getPath();
		FileOutputStream fos = new FileOutputStream(path);
		prop.store(fos, "update");
		fos.close();
		fos.flush();
		// TODO Auto-generated catch block
	}

	public static void setDbConf(String url2, String username2, String password2,
			String database2) throws IOException {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/DataBase.properties");
		prop.load(in);
		in.close();
		prop.setProperty("url", url2);
		prop.setProperty("username", username2);
		prop.setProperty("password", password2);
		prop.setProperty("database", database2);
		String path = SysConfUtil.class.getResource("/DataBase.properties").getPath();
		FileOutputStream fos = new FileOutputStream(path);
		prop.store(fos, "update");
		fos.close();
		fos.flush();
		// TODO Auto-generated catch block
	}

	public static void setTimeServer(String timeServerIp) throws IOException {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/TimeServer.properties");
		prop.load(in);
		in.close();
		prop.setProperty("timeServer", timeServerIp);
		String path = SysConfUtil.class.getResource("/TimeServer.properties").getPath();
		FileOutputStream fos = new FileOutputStream(path);
		prop.store(fos, "update");
		fos.close();
		fos.flush();
	}

	public static void setStayTimes(String[] stayTime) throws IOException {
		Properties prop = new Properties();
		InputStream in = SysConfUtil.class
				.getResourceAsStream("/ChannelStayTime.properties");
		prop.load(in);
		in.close();
		int i = 0;
		for (; i < 13; i++) {
			String key = String.valueOf(i + 1);
			String value = stayTime[i];
			prop.setProperty(key, value);
		}
		String path = SysConfUtil.class.getResource("/ChannelStayTime.properties").getPath();
		FileOutputStream fos = new FileOutputStream(path);
		prop.store(fos, "update");
		fos.close();
		fos.flush();
	}
}
