package com.xuan.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuan.service.kdj.KdjService;
import com.xuan.service.stk.StkService;

@RestController
@RequestMapping("/rsi")
public class RsiController {

	@Autowired
	private StkService stkService;

	@Autowired
	private KdjService kdjService;
	
    DecimalFormat DF = new DecimalFormat("###,##0.0");
    @GetMapping("/show")
    public String show() {

    	StringBuilder sb = new StringBuilder();
    	for (String stkCode: Constants.StkCodeList) {
        	JSONArray array = stkService.find(stkCode, "d");
        	int size = 0;
        	List<Double> hights = new ArrayList();
        	List<Double> lows = new ArrayList();
        	List<Double> closes = new ArrayList();
        	for (int i = array.length() - 1; i > -1 && size++ < 120; i--) {
        		JSONObject json = array.getJSONObject(i);
        		hights.add(0, json.getDouble("h"));
        		lows.add(0, json.getDouble("l"));
        		closes.add(0, json.getDouble("c"));
        	}
        	double[] res = kdjService.rsi(5, closes);
        	size = res.length;
        	if (res[size - 1] > 30) {
        		continue;
        	}
    		sb.append("<p>" + stkCode + "===================</p>");
    		List<String> strs = new ArrayList();
    		Arrays.stream(res).forEach(d -> {
    			strs.add(DF.format(d));
    		});
    		sb.append("<p>" + String.join(", ", strs) + "</p>");
    	}

    	return sb.toString();
    
    }
}
