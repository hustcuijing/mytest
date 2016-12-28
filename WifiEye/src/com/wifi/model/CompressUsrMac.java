package com.wifi.model;

import com.wifi.util.StringUtil;

public class CompressUsrMac {

	private String usr_mac;//用户mac
	private String day;//时间 以天为单位
	private String timeString;//还没有压缩的时间字符串
	private String device_macString;//没压缩的设备字符串
	public CompressUsrMac(String usr_mac,String day){
		this.usr_mac = usr_mac;
		this.day = day;
		timeString = StringUtil.createInitData(1440);
	}
	public String getUsr_mac() {
		return usr_mac;
	}
	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	public String getDevice_macString() {
		return device_macString;
	}
	public void setDevice_macString(String device_macString) {
		this.device_macString = device_macString;
	}
}
