package com.wifi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wifi.model.Visit;

public class RetainallMacImpl implements RetainallMac{

	public List<Visit> retainMac(List<Visit> list1, List<Visit> list2) {
		// TODO Auto-generated method stub
		int i ;
		int j ;
		String usr_mac;
		List<Visit> list3 = new ArrayList<Visit>();
		List<String> mac = new ArrayList<String>();
		
		for(i = 0; i < list1.size();i++){
			usr_mac = list1.get(i).getUsr_mac();//当前用户mac
			for(j = 0;j < list2.size();j++){
				if(list2.get(j).getUsr_mac().equals(usr_mac) && !mac.contains(usr_mac) ){
					list3.add(list1.get(i));
					mac.add(usr_mac);
				}
			}
		}	
		return list3;
	}

	public void deleteMac(Map<String, List<Visit>> m, List<Visit> list) {
		// TODO Auto-generated method stub
		//把value的list除去非list中的对象
		for(String address:m.keySet()){
			for(int i = 0;i < m.get(address).size();i++){//获取value中的visit
				Visit v1 = m.get(address).get(i);
				Boolean status = false;//标识此点是否被删除
				for(Visit v2:list){//获取碰撞后的visit
					if(v1.getUsr_mac().equals(v2.getUsr_mac()))//只要相等，则说明是碰撞到的mac
						status = true;
				}
				if(status == false)//表示在list中没有该，即表示不是被碰撞的mac
				{
					m.get(address).remove(i);
					i--;
				}	
			}
		}
	}

}
