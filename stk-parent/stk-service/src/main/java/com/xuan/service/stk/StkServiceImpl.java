package com.xuan.service.stk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuan.en.MaxCountRecord;
import com.xuan.en.MinCountRecord;
import com.xuan.en.Stock;
import com.xuan.repository.MaxCountRecordRepository;
import com.xuan.repository.MinCountRecordRepository;
import com.xuan.repository.StockRepository;
import com.xuan.service.async.AsyncService;
import com.xuan.service.reptile.Reptile;

@Service
public class StkServiceImpl implements StkService {

	@Autowired
	private Reptile reptile;

	@Autowired
	private AsyncService asyncService;

	//stkUrl = "http://127.0.0.1/stk/1.html?" + System.currentTimeMillis();
	final String stkUrl = "https://tw.quote.finance.yahoo.net/quote/q?type=ta&perd=5m&mkt=10&v=1&sym=";
	final String stkPerdUrl = "https://tw.quote.finance.yahoo.net/quote/q?type=ta&mkt=10&v=1&sym=";
	
	final String wtxUrl = "https://tw.screener.finance.yahoo.net/future/q?type=ta&perd=5m&mkt=01&sym=";
	final String wtxPerUrl = "https://tw.screener.finance.yahoo.net/future/q?type=ta&mkt=01&sym=";
	ConcurrentHashMap<String, Long> CacheTime = new ConcurrentHashMap();
	ConcurrentHashMap<String, JSONArray> CacheJSONArray = new ConcurrentHashMap();
	long _19s = 19 * 1000;
	long _1day = 60 * 60 * 24 * 1000;
	SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	SimpleDateFormat SDF1 = new SimpleDateFormat ("yyyyMMdd");
	String TodayStr = SDF1.format(new Date());
	//String JsonText = "";
	@Autowired
	MinCountRecordRepository minCountRecordRepository;

	@Autowired
	MaxCountRecordRepository maxCountRecordRepository;
	
	@Autowired
	StockRepository stockRepository;
	private ConcurrentHashMap<Long, int[]> CacheMinCounts = new ConcurrentHashMap();
	private ConcurrentHashMap<Long, int[]> CacheMaxCounts = new ConcurrentHashMap();
	private List<Stock> Stocks = null;
	@PostConstruct
	public void init() {
		/*
		try {
			File file = ResourceUtils.getFile("classpath:stk50.json");
	        InputStream in = new FileInputStream(file);
            JsonText = IOUtils.toString(in, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		Stocks = stockRepository.find0050();
		long todayLong = System.currentTimeMillis() / _1day * _1day;
		List<MinCountRecord> minCountRecords = minCountRecordRepository.findByInserttime("" + todayLong);
		for (MinCountRecord minCountRecord : minCountRecords) {
			long time = Long.parseLong(minCountRecord.getInserttime());
			int[] mincounts = new int[] {
				minCountRecord.getCount1(),
				minCountRecord.getCount2(),
				minCountRecord.getCount3(),
				minCountRecord.getCount4(),
				minCountRecord.getCount5(),
				minCountRecord.getCount6(),
				minCountRecord.getCount7(),
				minCountRecord.getCount8(),
				minCountRecord.getCount9(),
				minCountRecord.getCount10(),
				minCountRecord.getCount11(),
				minCountRecord.getCount12()
			};
			CacheMinCounts.put(time, mincounts);
		}
		List<MaxCountRecord> maxCountRecords = maxCountRecordRepository.findByInserttime("" + todayLong);
		for (MaxCountRecord maxCountRecord : maxCountRecords) {
			long time = Long.parseLong(maxCountRecord.getInserttime());
			int[] maxcounts = new int[] {
				maxCountRecord.getCount1(),
				maxCountRecord.getCount2(),
				maxCountRecord.getCount3(),
				maxCountRecord.getCount4(),
				maxCountRecord.getCount5(),
				maxCountRecord.getCount6(),
				maxCountRecord.getCount7(),
				maxCountRecord.getCount8(),
				maxCountRecord.getCount9(),
				maxCountRecord.getCount10(),
				maxCountRecord.getCount11(),
				maxCountRecord.getCount12()
			};
			CacheMaxCounts.put(time, maxcounts);
		}
	}
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


	
	@Override
	public int[][] calcMinMaxCount() {
    	int[] mins = new int[12]; 
    	int[] maxs = new int[12]; 
		for (Stock stock : Stocks) {
			String stkCode = stock.getStkCode();
			String stkName = stock.getName();
			JSONArray array = null;
			try {
				array = find(stkCode);
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
    	saveCountRecord(mins, maxs);
    	return new int[][] {mins, maxs};
	}
	
	private void saveCountRecord(int[] mins, int[] maxs) {
    	CacheMinCounts.put(System.currentTimeMillis(), mins);
    	CacheMaxCounts.put(System.currentTimeMillis(), maxs);
    	asyncService.saveMinCountRecord(mins);
    	asyncService.saveMaxCountRecord(maxs);
	}

	@Override
	public ConcurrentHashMap<Long, int[]> getCacheMinCounts() {
		return CacheMinCounts;
	}
	@Override
	public ConcurrentHashMap<Long, int[]> getCacheMaxCounts() {
		return CacheMaxCounts;
	}
	public List<Stock> getStocks() {
		return Stocks;
	}
	public void setStocks(List<Stock> stocks) {
		Stocks = stocks;
	}


}
