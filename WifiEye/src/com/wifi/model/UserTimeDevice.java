package com.wifi.model;

public class UserTimeDevice {

	private String usr_mac;
	private String time;
	private String device;
	
	public UserTimeDevice(String usr_mac, String time, String device) {
//		super();
		this.usr_mac = usr_mac;
		this.time = time;
		this.device = device;
	}
	
	public String getUsr_mac() {
		return usr_mac;
	}
	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	
}
