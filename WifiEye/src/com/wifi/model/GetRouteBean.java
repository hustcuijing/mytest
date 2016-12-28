package com.wifi.model;

public class GetRouteBean implements Comparable<GetRouteBean>{

	/**
	 * 处理后的route 格式
	 */

	private String usr_mac;
	private String start_time;
	private String end_time;
	private String device_mac;
	private String area;
	private String addr;
	private double latitude;
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	private double longtitude;
	private String total;//保存总条数

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getUsr_mac() {
		return usr_mac;
	}

	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getDevice_mac() {
		return device_mac;
	}

	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}


	public GetRouteBean(String usr_mac, String start_time, String end_time,
			String device_mac, String area, String addr, double latitude,
			double longtitude) {
//		super();
		this.usr_mac = usr_mac;
		this.start_time = start_time;
		this.end_time = end_time;
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}
	public GetRouteBean(String usr_mac, String start_time, String end_time,
			String device_mac, String area, String addr) {
//		super();
		this.usr_mac = usr_mac;
		this.start_time = start_time;
		this.end_time = end_time;
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
	
	}

	public int compareTo(GetRouteBean grb) {
		// TODO Auto-generated method stub
		return start_time.compareTo(grb.getStart_time());
	}


}
