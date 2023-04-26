package com.xuan.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
@RequestMapping("/ma")
public class MaController {

	@Autowired
	private StkService stkService;
	SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
	String TodayStr = SDF1.format(new Date());

    @GetMapping("/one/{stkCode}")
    public ModelAndView one(Map<String, Object> model, @PathVariable String stkCode) {
    	JSONArray array = stkService.find(stkCode);
		int yesterdayEndJsonIdx = 0;
		for (int i = array.length() - 1; i > 0; i--) {
			JSONObject json = array.getJSONObject(i);
			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
				JSONObject lastJson = array.getJSONObject(array.length()  - 1);
				yesterdayEndJsonIdx = i;
				break;
			}
		}
		JSONObject json = new JSONObject("{ma5:{t:[], c:[], m:[], d:[]}}");
		List<Double> vals = new ArrayList<Double>();
		for (int i = yesterdayEndJsonIdx - 4; i < array.length(); i++) {
			vals.add(array.getJSONObject(i).getDouble("c"));
			if (vals.size() > 5) {
				vals.remove(0);
			}
			if (vals.size() == 5) {
				double avg = vals.stream().mapToDouble(a -> a).average().getAsDouble();
				json.getJSONObject("ma5").getJSONArray("t").put(array.getJSONObject(i).getLong("t"));
				json.getJSONObject("ma5").getJSONArray("c").put(array.getJSONObject(i).getDouble("c"));
				json.getJSONObject("ma5").getJSONArray("m").put(avg);
				json.getJSONObject("ma5").getJSONArray("d").put(avg - array.getJSONObject(i).getDouble("c"));
			}
		}
		model.put("result", json.toString());
        return new ModelAndView("maone");    	

    }
}
