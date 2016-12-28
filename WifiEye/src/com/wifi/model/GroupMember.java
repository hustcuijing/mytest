package com.wifi.model;

public class GroupMember {

	private String name;//所在群组的名称
	private String usr_mac;//用户mac
	
	public GroupMember(String name, String usr_mac) {
//		super();
		this.name = name;
		this.usr_mac = usr_mac;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsr_mac() {
		return usr_mac;
	}
	public void setUsr_mac(String usr_mac) {
		this.usr_mac = usr_mac;
	}
	
	
}
