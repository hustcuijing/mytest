package com.wifi.model;

public class Visit implements Comparable<Visit>{

	private int id;

	private String device_mac;//设备mac
	public Visit(String device_mac, String area, String addr, String usr_mac,
			String first_time, String last_time, int online_times) {
//		super();
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
		this.usr_mac = usr_mac;
		this.first_time = first_time;
		this.last_time = last_time;
		this.online_times = online_times;
	}
	private String area;
	public Visit(String usr_mac, int showTime) {
		super();
		this.usr_mac = usr_mac;
		this.showTime = showTime;
	}
	private String addr;
	public Visit(String device_mac, String area, String addr, double latitude,
			double longtitude, String usr_mac, String first_time,
			String last_time, String arads_timee_time) {
		super();
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.usr_mac = usr_mac;
		this.first_time = first_time;
		this.last_time = last_time;
		this.arads_timee_time = arads_timee_time;
	}
	private double latitude;
	private double longtitude;
	
	private String usr_mac;//手机终端mac
	private String first_time;//上线时间
	private String last_time;//下线时间
	private int online_times;//上线次数
	
	private String ap_mac;//ap终端mac
	private float data_rate;
	private int rssi_signal;
	private int channel_id;
	private String arads_timee_time;
	public String getArads_timee_time() {
		return arads_timee_time;
	}
	public void setArads_timee_time(String arads_timee_time) {
		this.arads_timee_time = arads_timee_time;
	}
	public int getShowTime() {
		return showTime;
	}
	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}
	private int showTime;//在计算伴随mac时，若此mac是伴随mac,则记录此mac伴随了几次
	
	public int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Visit(){
		
	}
	public Visit(Visit v){
		device_mac = v.getDevice_mac();
		usr_mac = v.getUsr_mac();
		ap_mac = v.getAp_mac();
		first_time = v.getFirst_time();
		last_time = v.getLast_time();
		online_times = v.getOnline_times();
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
	public int getOnline_times() {
		return online_times;
	}
	public void setOnline_times(int online_times) {
		this.online_times = online_times;
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
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public int compareTo(Visit o) {
		// TODO Auto-generated method stub
		if(this.first_time.compareTo(o.first_time) < 0)//根据开始时间将排序
			return -1;
		else if(this.first_time.compareTo(o.first_time) > 0)
		    return 1;
		else
			return 0;
	}

	
}
