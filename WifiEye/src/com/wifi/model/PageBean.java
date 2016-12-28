package com.wifi.model;

public class PageBean {

	private int page; //页数
	private int rows; //每一页的行数
	@SuppressWarnings("unused")
	private int start;  //页面起始的数据行
	
	
	public PageBean(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getStart() {
		return (page-1)*rows;
	}
	
	
}
