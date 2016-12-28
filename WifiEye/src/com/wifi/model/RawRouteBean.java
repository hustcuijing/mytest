package com.wifi.model;



public class RawRouteBean implements Comparable<RawRouteBean>{

	/**
	 * 获取的route格式，压缩表获取的数据格式
	 */

	private String usr_mac;

	private String recordtime;
	private String device_mac;
	private String area;
	private String addr;

	public String getUsr_mac() {
		return usr_mac;
	}

	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}

	public String getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
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

	public int compareTo(RawRouteBean rrb) {
		// TODO Auto-generated method stub
		return recordtime.compareTo(rrb.getRecordtime());
	}
	
	public RawRouteBean(String recordtime) {
//		super();
		this.recordtime = recordtime;
	}
	
	public RawRouteBean(String usr_mac, String recordtime, String device_mac,
			String area, String addr) {
//		super();
		this.usr_mac = usr_mac;
		this.recordtime = recordtime;
		this.device_mac = device_mac;
		this.area = area;
		this.addr = addr;
	}


}
