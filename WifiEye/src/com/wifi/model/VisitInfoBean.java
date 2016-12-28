package com.wifi.model;

public class VisitInfoBean {

	private String device_mac;
	private String usr_mac;
	private String first_time;
	private String last_time;
	private int visit_times;
	private String area;
	private String addr;
	private double latitude;
	private double longitude;
	
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getUsr_mac() {
		return usr_mac;
	}
	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}
	public String getFirst_time() {
		return first_time;
	}
	public void setFirst_time(String first_time) {
		this.first_time = first_time;
	}
	public String getLast_time() {
		return last_time;
	}
	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public int getVisit_times() {
		return visit_times;
	}
	public void setVisit_times(int visit_times) {
		this.visit_times = visit_times;
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
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public VisitInfoBean(String device_mac, String usr_mac, String first_time,
			String last_time, int visit_times, String area, String addr,
			double latitude, double longitude) {
		super();
		this.device_mac = device_mac;
		this.usr_mac = usr_mac;
		this.first_time = first_time;
		this.last_time = last_time;
		this.visit_times = visit_times;
		this.area = area;
		this.addr = addr;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	
}
