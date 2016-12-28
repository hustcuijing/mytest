package com.wifi.model;

public class VisitCount {

	private String area;

	private String addr;
	private String device_name;
	private int total;
	private int unique;
	
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
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getUnique() {
		return unique;
	}
	public void setUnique(int unique) {
		this.unique = unique;
	}
	
}
