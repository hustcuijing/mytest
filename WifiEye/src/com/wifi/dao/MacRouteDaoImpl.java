package com.wifi.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wifi.model.GetRouteBean;
import com.wifi.model.PageBean;
import com.wifi.model.RawRouteBean;
import com.wifi.model.UsrMacInfo;
import com.wifi.util.DataUtil;


public class MacRouteDaoImpl implements MacRouteDao {

	public List<GetRouteBean> getMacRoute(String start_time, String end_time,
			String usr_mac) {
		//获取mac路径
		//以开始时间为为key,
		Map<String,GetRouteBean> map = new HashMap<String,GetRouteBean>();
		String lastdevice_mac = null;
		String laststarttime = null;

		List<UsrMacInfo> list = DataUtil.getInfos2( start_time, end_time,usr_mac);
		System.out.println("route原始大小" + list.size());
        
		System.out.println("-------------------");
		for(UsrMacInfo u:list){
			System.out.println(u.getRecord_time());
		}
		System.out.println("-------------------");
		
		List<GetRouteBean> list2 = new ArrayList<GetRouteBean>();

		Collections.sort(list);//根据录入时间排序
		
		for(UsrMacInfo r:list){//处理输入
			    if(!map.isEmpty()){
			    	
			    	if(r.getDevice_mac().equals(lastdevice_mac)){//如果出现的设备mac不变，则更新结束时
			    		map.get(laststarttime).setEnd_time(r.getRecord_time());
			    	}
			    	
			    	else{//设备mac变化
			    		map.put(r.getRecord_time(), new GetRouteBean(r.getUsr_mac(), r.getRecord_time(), r.getRecord_time(), r.getDevice_mac(), r.getArea(), r.getAddr(),r.getLatitude(),r.getLongtitude()));
			    		lastdevice_mac = r.getDevice_mac();
			    		laststarttime = r.getRecord_time();
			    	}
			    	
			    }
			    else{
			    	//首次添加map
			    	map.put(r.getRecord_time(),new GetRouteBean(r.getUsr_mac(), r.getRecord_time(), r.getRecord_time(), r.getDevice_mac(), r.getArea(), r.getAddr(),r.getLatitude(),r.getLongtitude()));
			    	lastdevice_mac = r.getDevice_mac();
			    	laststarttime = r.getRecord_time();
			  
			    }    
		}
		for(String dmac:map.keySet()){
			list2.add(map.get(dmac));
		}
		
		Collections.sort(list2);

		return list2;
	}

}
