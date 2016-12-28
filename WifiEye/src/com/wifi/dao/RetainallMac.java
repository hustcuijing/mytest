package com.wifi.dao;

import java.util.List;
import java.util.Map;

import com.wifi.model.Visit;

public interface RetainallMac {
       public List<Visit> retainMac(List<Visit> list1, List<Visit> list2);//返回list1与list2的交集
       public void deleteMac(Map<String, List<Visit>> m,List<Visit> list);//删除非碰撞mac
}
