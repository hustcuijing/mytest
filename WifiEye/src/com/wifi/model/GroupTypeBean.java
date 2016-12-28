package com.wifi.model;

public class GroupTypeBean {
	private int groupType_id;
    public GroupTypeBean(int groupType_id, String name) {
		super();
		this.groupType_id = groupType_id;
		this.name = name;
	}
	private String name;
    
    public int getGroupType_id() {
		return groupType_id;
	}
	public void setGroupType_id(int groupType_id) {
		this.groupType_id = groupType_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
