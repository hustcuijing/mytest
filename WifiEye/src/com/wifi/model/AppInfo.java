package com.wifi.model;

public class AppInfo {

	
	private int id;

	private String device_mac;//设备mac
	private String area;
	private String addr;
	private double latitude;
	private double longtitude;
	
	private String usr_mac;//手机终端mac
	
	private String recordtime;//记录时间
	
	private String ap_mac;//ap终端mac
	private float data_rate;
	private int rssi_signal;
	private int channel_id;
	private String app_type;
	private String app_info;
	
	public AppInfo(){
		
	}
	public AppInfo(int id, String device_mac, String area, String addr,
			double latitude, double longtitude, String usr_mac,
			String recordtime,
			String ap_mac, float data_rate, int rssi_signal, int channel_id,
			String app_type, String app_info) {
//		super();
		this.id = id;
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.usr_mac = usr_mac;
		this.recordtime = recordtime;
		this.ap_mac = ap_mac;
		this.data_rate = data_rate;
		this.rssi_signal = rssi_signal;
		this.channel_id = channel_id;
		this.app_type = app_type;
		this.app_info = app_info;
	}
	
	public AppInfo(AppInfo v){
		device_mac = v.getDevice_mac();
		usr_mac = v.getUsr_mac();
		ap_mac = v.getAp_mac();
		recordtime = v.getRecordtime();
	}
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
	public String getAp_mac() {
		return ap_mac;
	}
	public void setAp_mac(String ap_mac) {
		this.ap_mac = ap_mac;
	}
	
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getData_rate() {
		return data_rate;
	}
	public void setData_rate(float data_rate) {
		this.data_rate = data_rate;
	}
	public int getRssi_signal() {
		return rssi_signal;
	}
	public void setRssi_signal(int rssi_signal) {
		this.rssi_signal = rssi_signal;
	}
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getApp_type() {
		return app_type;
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	public String getApp_info() {
		return app_info;
	}
	public void setApp_info(String app_info) {
		this.app_info = app_info;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	
	
}
