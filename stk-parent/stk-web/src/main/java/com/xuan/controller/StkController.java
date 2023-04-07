package com.xuan.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuan.service.stk.StkService;

@RestController
@RequestMapping("/stk")
public class StkController {

	@Autowired
	private StkService stkService;
	SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
    DecimalFormat DF = new DecimalFormat("###,##0.0000");
    DecimalFormat DF1 = new DecimalFormat("###,##0.00");
	String TodayStr = SDF1.format(new Date());
	String YesterDayStr = "";
	{
		Date date = new Date();
		date.setTime(date.getTime() - (60 *60 * 24 * 1000));
		YesterDayStr = SDF1.format(date) + "1330";
		//YesterDayStr = "202303311330";
	}

    @GetMapping("/show")
    public String show() {
    	JSONObject stkcodes = new JSONObject(Constants.StkCodes);
    	StringBuilder sb = new StringBuilder();
    	sb.append("<p>" + SDF.format(new Date()) + "</p>");
    	for (String groupName : stkcodes.keySet()) {
        	sb.append("<p>" + groupName + "------------------------</p>");
    		JSONArray stkArray = stkcodes.getJSONArray(groupName);
    		for (int stkIdx = 0; stkIdx < stkArray.length(); stkIdx++) {
    			JSONObject stkJson = stkArray.getJSONObject(stkIdx);
    			String stkCode = stkJson.getString("i");
    			String stkName = stkJson.getString("n");
				JSONArray array = null;

    			try {
    				array = stkService.find(stkCode);
    			} catch (Exception e) {
    				continue;
    			}
	        	StringBuilder row = new StringBuilder();
	        	HashMap<String, String> map = createMA(stkCode);
	        	row.append( "<pre><p style='white-space: nowrap;font-size:8px'><b> ");
	        	row.append("[" + stkCode + "] " + "[" + stkName + "] ");
	        	row.append(map.toString() + "</p><p>");
	        	JSONObject yesterdayEndJson = null;
	        	int minI = array.length() - 1;
	        	double minL = 9999;
	    		for (int i = array.length() - 1; i > 0; i--) {
	    			JSONObject json = array.getJSONObject(i);
	    			if ((array.length() - i < 7) && json.getDouble("l") < minL) {
	    				minI = i;
	    				minL = json.getDouble("l");
	    			}
	    			if (YesterDayStr.equals("" + array.getJSONObject(i).getLong("t"))) {
	    				yesterdayEndJson = array.getJSONObject(i);
	    				break;
	    			}
	    		}
	    		array.getJSONObject(minI).put("minL", "true");
        		for (int i = array.length() - 1; i > 0; i--) {
    	        	JSONObject pre = array.getJSONObject(i - 1);
        			JSONObject json = array.getJSONObject(i);
        			if (!("" + json.getLong("t")).startsWith(TodayStr)) {
        				continue;
        			}
        			try {
            			row.append(pack(yesterdayEndJson, pre, json) + "	");
        			} catch (Exception e) {
        				continue;
        			}
        		}
	        	row.append("</b></p></pre>");
	    		sb.append(row.toString());
    		}
    	}
		sb.append("<script>setTimeout(() => location.reload(), 10 * 1000);</script>");
        return sb.toString();
    }
    
    String pack(JSONObject yesterdayEndJson, JSONObject pre, JSONObject json) {
		/*
		 * {"c":160,"t":202303231055,"v":68,"h":160.5,"l":160,"o":160.5} 
		 * */
    	
		String t = ("" + json.getLong("t")).substring(8);
		String c = "" + json.getDouble("c");
		boolean isChanged = false;
    	double yesterdayEndO = yesterdayEndJson.getDouble("o");
	    double diff = (json.getDouble("c") - yesterdayEndO) / yesterdayEndO;
	    String diffStr = DF.format(diff);
	    String changedVal = " / " + DF.format(diff);
		if ( json.getDouble("c") > yesterdayEndO) {
			c = "<font color='red'>"  + c + "("+ diffStr + ")</font>";
		} else if ( json.getDouble("c") < yesterdayEndO) {
			c = "<font color='#00DD00'>"  + c + "("+ diffStr + ")</font>";
		}
		String v = "" + json.getDouble("v");
		if (json.getDouble("c") < pre.getDouble("c")) {
			if (json.has("minL")) {
				v = "<font size='5px' color='#00DD00'>"  + v + "</font>";
			} else {
				v = "<font color='#00DD00'>"  + v + "</font>";
			}
		} else if (json.getDouble("v") > 1000.0 ) {
			v = "<font color='#BB5500'>"  + v + "</font>";
		}

    	return  "(" + t + ": " + c + " / " + v + ")";
    }
    
    HashMap<String, String> createMA(String stkCode) {
    	HashMap<String, String> map = new HashMap<String, String>();
    	JSONArray array = stkService.find(stkCode, "d");
    	List<Double> nums = new ArrayList();
    	int count = 0;
    	double num = 0.0;
    	for (int i = array.length() -2; i > -1; i--) {
    		count++;
    		nums.add(array.getJSONObject(i).getDouble("c"));
    		switch (count) {
    		case 5:
    			num = nums.stream()
    			.mapToDouble(e -> Double.valueOf(e))
    			.average()
    		    .orElse(Double.NaN);
    			map.put("MA5", DF1.format(num));
    			break;
    		case 20:
    			num = nums.stream()
    			.mapToDouble(e -> Double.valueOf(e))
    			.average()
    		    .orElse(Double.NaN);
    			map.put("MA20", DF1.format(num));

    			break;
    		case 60:
    			num = nums.stream()
    			.mapToDouble(e -> Double.valueOf(e))
    			.average()
    		    .orElse(Double.NaN);
    			map.put("MA60", DF1.format(num));
    			break;
    		}
    	}
    	return map;
    }
}
