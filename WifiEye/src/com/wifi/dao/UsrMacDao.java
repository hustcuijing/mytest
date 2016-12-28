package com.wifi.dao;

import com.wifi.model.CompressUsrMac;
import com.wifi.model.T_UserMac;

public interface UsrMacDao {

	public boolean hasData(String usr_mac);
	public int insert(CompressUsrMac c);
	public int update(T_UserMac c);
	public T_UserMac getRecordByUsrmac(String usr_mac);
}
