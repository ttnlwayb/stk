package com.xuan.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xuan.Application;
import com.xuan.service.reptile.Reptile;
import com.xuan.service.stk.StkService;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = Application.class)
public class serviceTest {

	@Autowired
	private Reptile reptile;
	
	@Autowired
	private StkService stkService;

	@Test
	public void doReptileTest() {
		//String res = reptile.doReptile("http://127.0.0.1/stk/1.html");
		//assertNotNull(res);
	}
	@Test
	public void stkTest() {
		JSONArray obj = stkService.find("2603");
		assertNotNull(obj);
	}
	
	@Test
	public void stkTest1() {
		SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
		String TodayStr = SDF1.format(new Date());
		JSONArray array = stkService.find("WTX00", "5m");
		for (int i = 0; i < array.length(); i++) {
			if (!("" + array.getJSONObject(i).getLong("t")).startsWith(TodayStr)) {
				continue;
			}
			System.out.println("" + array.getJSONObject(i).getLong("t"));
		}
		assertNotNull(array);
	}
}
