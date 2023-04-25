package com.xuan.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.xuan.service.stk.StkService;

@RestController
@RequestMapping("/stk0050")
public class Stk0050Controller {

	@Autowired
	private StkService stkService;
	
	SimpleDateFormat SDF = new SimpleDateFormat ("yyyyMMddHHmm");
    @GetMapping("/table")
    public ModelAndView table(Map<String, Object> model) {
    	return table(model, 12);
    }
    @GetMapping("/table/{interval}")
	public ModelAndView table(Map<String, Object> model, @PathVariable int interval) {
		ConcurrentHashMap<Long, int[]> cacheMinCounts = null;
		ConcurrentHashMap<Long, int[]> cacheMaxCounts = null;
		switch (interval) {
			case 6:
				cacheMinCounts = stkService.get6CacheMinCounts();
				cacheMaxCounts = stkService.get6CacheMaxCounts();
				break;
			default:
				cacheMinCounts = stkService.getCacheMinCounts();
				cacheMaxCounts = stkService.getCacheMaxCounts();
		}
		JSONObject json = new JSONObject("{ma:{}, min:{}, max:{}}");
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
		json.getJSONObject("max").put("t", arrl);
		json.getJSONObject("max").put("n", arrn);
		try {
			JSONArray array = stkService.find("00631L");
			JSONObject majson = json.getJSONObject("ma");
			List<Double> mas = new ArrayList<Double>();
			List<Integer> manum = new ArrayList(Arrays.asList(1, 5, 20, 60));
			for (int i = array.length() - 1; i > -1 && !manum.isEmpty(); i--) {
				JSONObject d = array.getJSONObject(i);
				mas.add(d.getDouble("c"));
				if (manum.contains(mas.size())) {
					double avg = mas.stream().mapToDouble(a -> a).average().getAsDouble();
					majson.put("ma" + mas.size(), avg);
					manum.remove(0);
				}
			}
		} catch (Exception e) {
		}
		model.put("result", json.toString());
		return new ModelAndView("stk0050table");
	}
}
