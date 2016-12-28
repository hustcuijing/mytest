package com.wifi.model;

public class UsrMacInfo implements Comparable<UsrMacInfo>{

	private String usr_mac;//用户MAC地址
	private String record_time;//用户出现的时间
	private String device_mac;//设备MAC地址
	private String area;//设备地址
	private String addr;
	private double latitude;
	private double longtitude;
	
	public UsrMacInfo(String usr_mac, String record_time, String device_mac,
			String area, String addr) {
//		super();
		this.usr_mac = usr_mac;
		this.record_time = record_time;
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
//		this.latitude = latitude;
//		this.longtitude = longtitude;
	}
	
	
	
	
	public UsrMacInfo(String usr_mac, String record_time, String device_mac,
			String area, String addr, double latitude, double longtitude) {
		super();
		this.usr_mac = usr_mac;
		this.record_time = record_time;
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}
	
	public UsrMacInfo(String usr_mac,String record_time,String device_mac,Address address){
		this.usr_mac = usr_mac;
		this.record_time = record_time;
		this.device_mac = device_mac;
		this.area = address.getArea();
		this.addr = address.getAddr();
		this.latitude = address.getLatitude();
		this.longtitude = address.getLongtitude();
	}
	public String getUsr_mac() {
		return usr_mac;
	}
	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	
	@Override
	public String toString(){
		return usr_mac + " " + record_time + " " + device_mac + " " + area + " " + addr;
		
	}

	public int compareTo(UsrMacInfo o) {
		// TODO Auto-generated method stub
		return record_time.compareTo(o.getRecord_time());
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
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	
}
