package com.xuan.controller;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.xuan.service.log.LogService;
import com.xuan.service.stk.StkService;


@RestController
@RequestMapping("/stk")
public class StkController {
	
	@Autowired
	private LogService logService;
	@Autowired
	private StkService stkService;
	SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
    DecimalFormat DF = new DecimalFormat("###,##0.0000");
    DecimalFormat DF1 = new DecimalFormat("###,##0.00");
    NumberFormat NF = NumberFormat.getPercentInstance();
	String TodayStr = SDF1.format(new Date());
	String TodayTimeStr = SDF.format(new Date());
	String JsonText = "";

	@PostConstruct
	public void init() {
		//TodayStr = "20230407";
		try {
			File file = ResourceUtils.getFile("classpath:stk50.json");
	        InputStream in = new FileInputStream(file);
            JsonText = IOUtils.toString(in, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @GetMapping("/chart")
    public ModelAndView chart(Map<String, Object> model) {
    	JSONObject stkcodes = new JSONObject(JsonText);
    	for (String groupName : stkcodes.keySet()) {
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
    			JSONObject yesterdayEndJson = null;
	    		for (int i = array.length() - 1; i > 0; i--) {
	    			JSONObject json = array.getJSONObject(i);
	    			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
	    				JSONObject lastJson = array.getJSONObject(array.length()  - 1);
	    				yesterdayEndJson = array.getJSONObject(i);
	    				break;
	    			}
	    		}
	    		
    			int count = 12;
            	JSONArray rowArray = new JSONArray();
            	rowArray.put(stkCode + "-" + stkName);
	    		for (int i = array.length() - 1; i > 0 && count > 0; i--) {
	    			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
	    				break;
	    			}
	    			count--;
	    			JSONObject json = array.getJSONObject(i);
	    			String c = "" + json.getDouble("c");
	    			double d = (json.getDouble("c") / yesterdayEndJson.getDouble("c")) - 1;
	    			d *= 100;
	    			d = Math.round(d * 10.0) / 10.0;
	    			rowArray.put(d);
	    		}
	    		while (rowArray.length() < 13) {
	    			rowArray.put("");
	    		}
	    		stkJson.put("d", rowArray);
    		}
    	}
        model.put("result", stkcodes.toString());
        return new ModelAndView("chart");
    }
    @GetMapping("/table")
    public ModelAndView table(Map<String, Object> model) {
    	JSONObject stkcodes = new JSONObject(JsonText);
    	for (String groupName : stkcodes.keySet()) {
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
	    		for (int i = array.length() - 1; i > 0; i--) {
	    			JSONObject json = array.getJSONObject(i);
	    			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
	    				JSONObject lastJson = array.getJSONObject(array.length()  - 1);
	    				JSONObject yesterdayEndJson = array.getJSONObject(i);
	    				double change = lastJson.getDouble("c") / yesterdayEndJson.getDouble("c") - 1;
	    				stkJson.put("c",  NF.format(change));
	    				break;
	    			}
	    		}
	    		
    			int count = 12;
            	JSONArray rowArray = new JSONArray();
            	rowArray.put(stkCode + "-" + stkName);
	    		for (int i = array.length() - 1; i > 0 && count > 0; i--) {
	    			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
	    				break;
	    			}
	    			if (array.getJSONObject(i).getLong("t") > 202304120955L) {
	    				//continue;
	    			}
	    			count--;
	    			JSONObject json = array.getJSONObject(i);
	    			String c = "" + json.getDouble("c");
	    			rowArray.put("" + json.getDouble("c") + "(" + json.getDouble("v") + ")");
	    		}

	    		while (rowArray.length() < 13) {
	    			rowArray.put("");
	    		}
	    		stkJson.put("d", rowArray);
    		}
    	}
        model.put("result", stkcodes.toString());
        return new ModelAndView("stk");
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
	    			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
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
		sb.append("<script>setTimeout(() => location.reload(), 20 * 1000);</script>");
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
