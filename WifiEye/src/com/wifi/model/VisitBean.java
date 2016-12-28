package com.wifi.model;

public class VisitBean {
	private int ID;//����ID
	private String device_name;//�豸mac
	private String address;//��ַ
	private String recordtime;
	private String usr_mac;//�ֻ�mac
	private String ap_mac;// �����mac
	private float data_rate;// ��������    
	private int rssi_signal;//�ź�ǿ��
	private int app_type;// 
	private String app_info;//
	private int channel_id;//ͣ��ʱ��
	private String firm;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
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
	public int getApp_type() {
		return app_type;
	}
	public void setApp_type(int app_type) {
		this.app_type = app_type;
	}
	public String getApp_info() {
		return app_info;
	}
	public void setApp_info(String app_info) {
		this.app_info = app_info;
	}
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public String getFirm() {
		return firm;
	}
	public void setFirm(String firm) {
		this.firm = firm;
	}

	
	

}
