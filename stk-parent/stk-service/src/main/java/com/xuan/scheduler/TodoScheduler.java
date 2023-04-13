package com.xuan.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.xuan.service.log.LogService;
import com.xuan.service.stk.StkService;

@Component
public class TodoScheduler {
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
		try {
			File file = ResourceUtils.getFile("classpath:stk50.json");
	        InputStream in = new FileInputStream(file);
            JsonText = IOUtils.toString(in, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Autowired
	private LogService logService;
	@Autowired
	private StkService stkService;

    @Scheduled(initialDelay = 1000, fixedRate = 19 * 1000)
    public void fixedRate(){
    	int[] mins = new int[12]; 
    	int[] maxs = new int[12]; 
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
    			int count = 12;
            	List<Double> doubleList = new ArrayList();
	    		for (int i = array.length() - 1; i > 0 && count > 0; i--) {
	    			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
	    				break;
	    			}
	    			count--;
	    			JSONObject json = array.getJSONObject(i);
	    			doubleList.add(json.getDouble("c"));
	    		}
	    		int minIdx = 0;
	    		int maxIdx = 0;
	    		for (int i = 1; i < doubleList.size(); i++) {
	    			if (doubleList.get(minIdx) > doubleList.get(i)) {
	    				minIdx = i;
	    			}
	    			if (doubleList.get(maxIdx) < doubleList.get(i)) {
	    				maxIdx = i;
	    			}
	    		}
	    		mins[minIdx]++;
	    		maxs[maxIdx]++;
    		}
    	}
    	int minSum = Arrays.stream(mins).sum();
    	int maxSum = Arrays.stream(maxs).sum();
		logService.info("" + minSum + "[mins]: " + Arrays.toString(mins));
		logService.warn("" + maxSum + "[maxs]: " + Arrays.toString(maxs));
    }
}
