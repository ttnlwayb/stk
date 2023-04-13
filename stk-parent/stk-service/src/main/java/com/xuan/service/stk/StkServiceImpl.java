package com.xuan.service.stk;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuan.service.reptile.Reptile;

@Service
public class StkServiceImpl implements StkService {

	@Autowired
	private Reptile reptile;

	//stkUrl = "http://127.0.0.1/stk/1.html?" + System.currentTimeMillis();
	final String stkUrl = "https://tw.quote.finance.yahoo.net/quote/q?type=ta&perd=5m&mkt=10&v=1&sym=";
	final String stkPerdUrl = "https://tw.quote.finance.yahoo.net/quote/q?type=ta&mkt=10&v=1&sym=";
	
	final String wtxUrl = "https://tw.screener.finance.yahoo.net/future/q?type=ta&perd=5m&mkt=01&sym=";
	final String wtxPerUrl = "https://tw.screener.finance.yahoo.net/future/q?type=ta&mkt=01&sym=";
	ConcurrentHashMap<String, Long> CacheTime = new ConcurrentHashMap();
	ConcurrentHashMap<String, JSONArray> CacheJSONArray = new ConcurrentHashMap();
	long _19s = 19 * 1000;
	@Override
	public JSONArray find(String stkCode) {
		String cacheKey = stkCode;
		if (_19s + CacheTime.getOrDefault(cacheKey, 0L) > System.currentTimeMillis()) {
			return CacheJSONArray.get(cacheKey);
		}
		String url = stkUrl + stkCode + "&nocache=" + System.currentTimeMillis();
		if (stkCode.startsWith("WTX")) {
			url = wtxUrl + stkCode + "&nocache=" + System.currentTimeMillis();
		}
		String data = reptile.doReptile(url);
		data = data.substring(5);
		if (stkCode.startsWith("WTX")) {
			data = data.replaceFirst("144", "144_1");
		}
		JSONObject json = new JSONObject(data);
		JSONArray array = json.getJSONArray("ta");
		CacheTime.put(cacheKey, System.currentTimeMillis());
		CacheJSONArray.put(cacheKey, array);
		return array;
	}
	
	@Override
	public JSONArray find(String stkCode, String perd) {
		String cacheKey = stkCode + perd;
		if (_19s + CacheTime.getOrDefault(cacheKey, 0L) > System.currentTimeMillis()) {
			return CacheJSONArray.get(cacheKey);
		}
		String url = stkPerdUrl + stkCode + "&perd=" + perd + "&nocache=" + System.currentTimeMillis();
		if (stkCode.startsWith("WTX")) {
			url = wtxPerUrl + stkCode  + "&perd=" + perd + "&nocache=" + System.currentTimeMillis();
		}
		String data = reptile.doReptile(url);
		data = data.substring(5);
		if (stkCode.startsWith("WTX")) {
			data = data.replaceFirst("144", "144_1");
		}
		JSONObject json = new JSONObject(data);
		JSONArray array = json.getJSONArray("ta");
		CacheTime.put(cacheKey, System.currentTimeMillis());
		CacheJSONArray.put(cacheKey, array);
		return array;
	}

}
