package com.wifi.dao;

import java.sql.ResultSet;
import java.util.List;

import com.wifi.model.GroupTypeBean;
import com.wifi.model.PageBean;

public interface GroupDao {

	public List<GroupTypeBean> getNameList();
	public List<GroupTypeBean> getNameListMain(String name,PageBean pageBean);
	public int listCount(String name);
	public boolean isExitsGroupName(String name);
	public void addGroupName(String name);
	public void updateGroupName(int groupType_id,String name);
	public int deleteGroupName(String groupType_ids);
	public int getIdByname(String name);
}
