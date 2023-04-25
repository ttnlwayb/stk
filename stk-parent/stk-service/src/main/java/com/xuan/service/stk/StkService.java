package com.xuan.service.stk;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;

public interface StkService {

	JSONArray find(String stkCode);

	JSONArray find(String stkCode, String perd);
	
	int[][] calcMinMaxCount();
	
	ConcurrentHashMap<Long, int[]> getCacheMinCounts();
	ConcurrentHashMap<Long, int[]> getCacheMaxCounts();

	ConcurrentHashMap<Long, int[]> get6CacheMinCounts();
	ConcurrentHashMap<Long, int[]> get6CacheMaxCounts();

}
