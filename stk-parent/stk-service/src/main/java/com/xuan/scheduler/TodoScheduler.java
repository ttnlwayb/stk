package com.xuan.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
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
	SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
    DecimalFormat DF = new DecimalFormat("###,##0.0000");
    DecimalFormat DF1 = new DecimalFormat("###,##0.00");
    NumberFormat NF = NumberFormat.getPercentInstance();
	String TodayStr = SDF1.format(new Date());
	String TodayTimeStr = SDF.format(new Date());

	@Autowired
	private LogService logService;
	@Autowired
	private StkService stkService;

    @Scheduled(initialDelay = 1000, fixedRate = 29 * 1000)
    public void fixedRate() throws Exception{
    	Date startDate = new Date();
    	startDate.setHours(9);
    	startDate.setMinutes(0);
    	startDate.setSeconds(0);
    	if (startDate.after(new Date())) {
    		System.out.println(SDF.format(new Date()) + " too early");
    		return;
    	}
    	Date endDate = new Date();
    	endDate.setHours(13);
    	endDate.setMinutes(30);
    	endDate.setSeconds(0);
    	if (endDate.before(new Date())) {
    		System.out.println(SDF.format(new Date()) + " too late");
    		return;
    	}
		int[][] data = stkService.calcMinMaxCount();
		int[] mins = data[0];
		int[] maxs = data[1];
    	int minSum = Arrays.stream(mins).sum();
    	int maxSum = Arrays.stream(maxs).sum();
		logService.info("" + minSum + "[mins]: " + Arrays.toString(mins));
		logService.warn("" + maxSum + "[maxs]: " + Arrays.toString(maxs));
    }
}
