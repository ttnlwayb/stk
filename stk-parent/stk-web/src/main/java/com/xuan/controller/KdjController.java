package com.xuan.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.xuan.service.kdj.KdjService;
import com.xuan.service.stk.StkService;

@RestController
@RequestMapping("/kdj")
public class KdjController {

	@Autowired
	private StkService stkService;

	@Autowired
	private KdjService kdjService;
	
	SimpleDateFormat SDF = new SimpleDateFormat ("yyyyMMddhhmm");
	SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
	String TodayStr = SDF1.format(new Date());

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
        	List<double[]> res = kdjService.kdj(hights, lows, closes);
        	size = res.get(0).length;
        	double lastK = res.get(0)[size - 1];
        	double lastD = res.get(1)[size - 1];
        	double lastJ = res.get(2)[size - 1];
        	/*if (lastJ > 10 || lastJ <= res.get(2)[size - 2]) {
        		continue;
        	}*/
        	if (lastK > lastJ + 20) {
        		continue;
        	}
    		sb.append("<p>" + stkCode + "===================</p>");
        	for (double[] doubles : res) {
        		List<String> strs = new ArrayList();
        		Arrays.stream(doubles).forEach(d -> {
        			strs.add(DF.format(d));
        		});
        		sb.append("<p>" + String.join(", ", strs) + "</p>");
        	}
    	}

    	return sb.toString();
    }
    @GetMapping("/one/{stkCode}")
    public ModelAndView one(Map<String, Object> model, @PathVariable String stkCode) {
    	return one(model, stkCode, 999999999999L);
    }
    @GetMapping("/one/{stkCode}/{endtime}")
    public ModelAndView one(Map<String, Object> model, @PathVariable String stkCode, @PathVariable long endtime) {
    	JSONArray array = stkService.find(stkCode);
    	int size = 0;
    	List<Double> hights = new ArrayList();
    	List<Double> lows = new ArrayList();
    	List<Double> closes = new ArrayList();
    	List<Long> times = new ArrayList();
    	for (int i = array.length() - 1; i > -1 && size++ < 120; i--) {
    		JSONObject json = array.getJSONObject(i);
    		if (json.getLong("t") > endtime) {
    			continue;
    		}
    		hights.add(0, json.getDouble("h"));
    		lows.add(0, json.getDouble("l"));
    		closes.add(0, json.getDouble("c"));
    		times.add(0, json.getLong("t"));
    	}
    	List<double[]> res = kdjService.kdj(hights, lows, closes);
    	JSONArray arrk = new JSONArray();
    	JSONArray arrd = new JSONArray();
    	JSONArray arrj = new JSONArray();
    	JSONArray arrt = new JSONArray();
    	JSONObject json = new JSONObject();
    	for (int i = 0; i < res.get(0).length; i++) {
    		if (times.get(i + 16) < 202304140859L) {
    			//continue;
    		}
    		if (times.get(i + 16) > endtime) {
    			continue;
    		}
    		arrk.put(res.get(0)[i]);
    		arrd.put(res.get(1)[i]);
    		arrj.put(res.get(2)[i]);
    		arrt.put(times.get(i + 16));
    	}
    	json.put("k", arrk);
    	json.put("d", arrd);
    	json.put("j", arrj);
    	json.put("t", arrt);
    	model.put("result", json.toString());
        return new ModelAndView("kdj");    	
    }
    
    @GetMapping("/one/MinCounts/{stkCode}")
    public ModelAndView oneAndMinCounts(Map<String, Object> model, @PathVariable String stkCode) {
    	JSONArray array = stkService.find(stkCode);
    	int size = 0;
    	List<Double> hights = new ArrayList();
    	List<Double> lows = new ArrayList();
    	List<Double> closes = new ArrayList();
    	List<Long> times = new ArrayList();
    	for (int i = array.length() - 1; i > -1 && size++ < 120; i--) {
    		JSONObject json = array.getJSONObject(i);
    		hights.add(0, json.getDouble("h"));
    		lows.add(0, json.getDouble("l"));
    		closes.add(0, json.getDouble("c"));
    		times.add(0, json.getLong("t"));
    	}
    	List<double[]> res = kdjService.kdj(hights, lows, closes);
    	JSONArray arrk = new JSONArray();
    	JSONArray arrd = new JSONArray();
    	JSONArray arrj = new JSONArray();
    	JSONArray arrt = new JSONArray();
    	JSONObject json = new JSONObject("{kdj:{}, min:{}, max:{}}");
    	for (int i = 0; i < res.get(0).length; i++) {
    		if (times.get(i + 16) < 202304140859L) {
    			//continue;
    		}
    		arrk.put(res.get(0)[i]);
    		arrd.put(res.get(1)[i]);
    		arrj.put(res.get(2)[i]);
    		arrt.put(times.get(i + 16));
    	}
    	json.getJSONObject("kdj").put("k", arrk);
    	json.getJSONObject("kdj").put("d", arrd);
    	json.getJSONObject("kdj").put("j", arrj);
    	json.getJSONObject("kdj").put("t", arrt);
    	
		ConcurrentHashMap<Long, int[]> cacheMinCounts = stkService.getCacheMinCounts();
		ConcurrentHashMap<Long, int[]> cacheMaxCounts = stkService.getCacheMaxCounts();
    	JSONArray arrl = new JSONArray();
    	JSONArray arrn = new JSONArray();
    	List<Long> nkeys = new ArrayList(cacheMinCounts.keySet());
    	nkeys = nkeys.stream().sorted().collect(Collectors.toList());
		for (Long time : nkeys) {
			arrl.put(SDF.format(new Date(time)));
			arrn.put(cacheMinCounts.get(time)[0]);
		}
    	json.getJSONObject("min").put("t", arrl);
    	json.getJSONObject("min").put("n", arrn);
    	arrl = new JSONArray();
    	arrn = new JSONArray();
    	List<Long> mkeys = new ArrayList(cacheMaxCounts.keySet());
    	mkeys = mkeys.stream().sorted().collect(Collectors.toList());
		for (Long time : mkeys) {
			arrl.put(SDF.format(new Date(time)));
			arrn.put(cacheMaxCounts.get(time)[0]);
		}
		//System.out.println(arrl.toString());
    	json.getJSONObject("max").put("t", arrl);
    	json.getJSONObject("max").put("n", arrn);
    	model.put("result", json.toString());
        return new ModelAndView("kdjMinCounts");    	
    }
    @GetMapping("/calc")
    public String calc() {
    	StringBuilder sb = new StringBuilder();
    	for (String stkCode: Constants.StkCodeList) {
    		if (!"6698".equals(stkCode)) {
    			//continue;
    		}
        	JSONArray array = stkService.find(stkCode, "d");
        	int size = 0;
        	List<String> times = new ArrayList();
        	List<Double> hights = new ArrayList();
        	List<Double> lows = new ArrayList();
        	List<Double> closes = new ArrayList();
        	int arraySize = array.length();
        	for (int i = arraySize - 1; i > -1 && size++ < 120; i--) {
        		JSONObject json = array.getJSONObject(i);
        		times.add(0, "" + json.getLong("t"));
        		hights.add(0, json.getDouble("h"));
        		lows.add(0, json.getDouble("l"));
        		closes.add(0, json.getDouble("c"));
        	}
        	List<double[]> res = kdjService.kdj(hights, lows, closes);
    		double preK = res.get(0)[0];
    		double preD = res.get(1)[0];
    		double preJ = res.get(2)[0];
    		double buy = 0;
    		double sell = 0;
    		double total = 0;
    		int earn = 0;
    		int pay = 0;
    		int resSize = res.get(0).length;
        	for (int i = 1; i < resSize; i++) {
        		double K = res.get(0)[i];
        		double D = res.get(1)[i];
        		double J = res.get(2)[i];
        		int arrIdx = arraySize - (resSize - i);
        		if (buy == 0 && (preJ < preK || preJ < preD)) {
            		if (K > 30) {
            			continue;
            		}
            		if (J >= K && J >= D) {
            			buy = hights.get(arrIdx);
            			System.out.println("[" + times.get(arrIdx) + "]buy:" + buy);
            		}      			
        		} else {
            		if (buy != 0 && preJ > preK && preJ > preD) {
						if (J <= K || J <= D) {
							sell = lows.get(arrIdx);
	            			System.out.println("[" + times.get(arrIdx) + "]sell:" + sell);
	            			double diff = sell - buy;
							total += diff;
							buy = 0;
							sell = 0;
							if (diff > 0) {
								earn++;
							}
							if (diff <= 0) {
								pay++;
							}
						}      			
            		}
        		}
        		preK = K;
        		preD = D;
        		preJ = J;
        	}
			sb.append("<pre><p>" + stkCode + "	" + 
					", earn: " + earn +
					", pay: " + pay +
					(", " + total / closes.get(closes.size() - 1)) + "</p></pre>");
    		continue;
    	}
    	return sb.toString();
    
    	
    }
    @GetMapping("/last")
	public String last() {
		StringBuilder sb = new StringBuilder();
		sb.append("<pre>");
		List<String> buys = new ArrayList();
		List<String> sells = new ArrayList();
		for (String stkCode : Constants.StkCodeList) {
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
			List<double[]> res = kdjService.kdj(hights, lows, closes);
			size = res.get(0).length;
			double K = res.get(0)[size - 1];
			double D = res.get(1)[size - 1];
			double J = res.get(2)[size - 1];
			double _2ndK = res.get(0)[size - 2];
			double _2ndD = res.get(1)[size - 2];
			double _2ndJ = res.get(2)[size - 2];
			int buyCount = 0;
			int sellCount = 0;
			boolean isBuy = false;
			if (_2ndK < 35 && _2ndJ < _2ndK && _2ndJ < _2ndD) {
				if (J > D && J > K) {
					buys.add(stkCode);
				}
			}
			if (!isBuy) {
				if (_2ndK > 65 && _2ndJ > _2ndK && _2ndJ > _2ndD) {
					if (J < D && J < K) {
						sells.add(stkCode);
					}
				}
			}
		}
		sb.append("<p> buys: " + buys.size() + ", " + buys + " </p><pre>");
		sb.append("<p> sells: " + sells.size() + ", " + sells + " </p><pre>");
		return sb.toString();
	}
}
