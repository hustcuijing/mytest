package com.wifi.dao;

import java.util.Comparator;

import com.wifi.model.Visit;

public class Com implements Comparator<Visit> {

	public int compare(Visit o1, Visit o2) {
		// TODO Auto-generated method stub
		if (o1.getShowTime() > o2.getShowTime())
			return -1;
		else if (o1.getShowTime() < o2.getShowTime())
			return 1;
		else
			return 0;
	}

}
