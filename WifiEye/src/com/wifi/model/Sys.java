package com.wifi.model;

public class Sys {

	public Sys(String wpaLiveTime, String timeServer, String url,
			String username, String password, String database,String[] stay) {
		this.wpaLiveTime = wpaLiveTime;
		this.timeServer = timeServer;
		this.url = url;
		this.username = username;
		this.password = password;
		this.database = database;
		int i = 0;
		one = stay[i++];
		two = stay[i++];
		three = stay[i++];
		four = stay[i++];
		five = stay[i++];
		six = stay[i++];
		seven = stay[i++];
		eight = stay[i++];
		nine = stay[i++];
		ten = stay[i++];
		eleven = stay[i++];
		twelve = stay[i++];
		thirteen = stay[i++];
	}
	private String wpaLiveTime;

	private String timeServer;
	private String url;
	private String username;
	private String password;
	private String database;
	private String one;
	private String two;
	private String three;
	private String four;
	private String five;
	private String six;
	private String seven;
	private String eight;
	private String nine;
	private String ten;
	private String eleven;
	private String twelve;
	private String thirteen;
	public String getWpaLiveTime() {
		return wpaLiveTime;
	}
	public void setWpaLiveTime(String wpaLiveTime) {
		this.wpaLiveTime = wpaLiveTime;
	}
	public String getTimeServer() {
		return timeServer;
	}
	public void setTimeServer(String timeServer) {
		this.timeServer = timeServer;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getOne() {
		return one;
	}
	public void setOne(String one) {
		this.one = one;
	}
	public String getTwo() {
		return two;
	}
	public void setTwo(String two) {
		this.two = two;
	}
	public String getThree() {
		return three;
	}
	public void setThree(String three) {
		this.three = three;
	}
	public String getFour() {
		return four;
	}
	public void setFour(String four) {
		this.four = four;
	}
	public String getFive() {
		return five;
	}
	public void setFive(String five) {
		this.five = five;
	}
	public String getSix() {
		return six;
	}
	public void setSix(String six) {
		this.six = six;
	}
	public String getSeven() {
		return seven;
	}
	public void setSeven(String seven) {
		this.seven = seven;
	}
	public String getEight() {
		return eight;
	}
	public void setEight(String eight) {
		this.eight = eight;
	}
	public String getNine() {
		return nine;
	}
	public void setNine(String nine) {
		this.nine = nine;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getEleven() {
		return eleven;
	}
	public void setEleven(String eleven) {
		this.eleven = eleven;
	}
	public String getTwelve() {
		return twelve;
	}
	public void setTwelve(String twelve) {
		this.twelve = twelve;
	}
	public String getThirteen() {
		return thirteen;
	}
	public void setThirteen(String thirteen) {
		this.thirteen = thirteen;
	}
	
}
