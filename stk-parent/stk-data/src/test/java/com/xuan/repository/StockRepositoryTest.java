package com.xuan.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.xuan.en.Stock;
import com.xuan.Application;
import com.xuan.repository.StockRepository;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = Application.class)
public class StockRepositoryTest {

	@Autowired
	private StockRepository stockRepository;

	String JsonText = "";

	public void init() {
		//TodayStr = "20230407";
		try {
			File file = ResourceUtils.getFile("classpath:Stk50.json");
	        InputStream in = new FileInputStream(file);
            JsonText = IOUtils.toString(in, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	String jsonStr = "{'元大50': \n"
			+ "	[['1101', '台泥', '水泥'], \n"
			+ "	['2330', '台積電', '半導體'], \n"
			+ "	['2454', '聯發科', '半導體'], \n"
			+ "	['2303', '聯電', '半導體'], \n"
			+ "	['3711', '日月光投控', '半導體'], \n"
			+ "	['3034', '聯詠', '半導體'], \n"
			+ "	['2379', '瑞昱', '半導體'], \n"
			+ "	['6415', '矽力*-KY', '半導體'], \n"
			+ "	['2408', '南亞科', '半導體'], \n"
			+ "	['3008', '大立光', '光電'], \n"
			+ "	['2207', '和泰車', '汽車'], \n"
			+ "	['5871', '中租-KY', '其他'], \n"
			+ "	['9910', '豐泰', '其他'], \n"
			+ "	['2317', '鴻海', '其他電子'], \n"
			+ "	['6505', '台塑化', '油電燃氣'], \n"
			+ "	['2881', '富邦金', '金融業'], \n"
			+ "	['2891', '中信金', '金融業'], \n"
			+ "	['2882', '國泰金', '金融業'], \n"
			+ "	['2886', '兆豐金', '金融業'], \n"
			+ "	['2884', '玉山金', '金融業'], \n"
			+ "	['2892', '第一金', '金融業'], \n"
			+ "	['5880', '合庫金', '金融業'], \n"
			+ "	['2885', '元大金', '金融業'], \n"
			+ "	['2880', '華南金', '金融業'], \n"
			+ "	['2883', '開發金', '金融業'], \n"
			+ "	['2887', '台新金', '金融業'], \n"
			+ "	['2890', '永豐金', '金融業'], \n"
			+ "	['5876', '上海商銀', '金融業'], \n"
			+ "	['2801', '彰銀', '金融業'], \n"
			+ "	['1216', '統一', '食品'], \n"
			+ "	['1402', '遠東新', '紡織'], \n"
			+ "	['2603', '長榮', '航運'], \n"
			+ "	['2609', '陽明', '航運'], \n"
			+ "	['2615', '萬海', '航運'], \n"
			+ "	['2412', '中華電', '通訊網路'], \n"
			+ "	['3045', '台灣大', '通訊網路'], \n"
			+ "	['4904', '遠傳', '通訊網路'], \n"
			+ "	['2912', '統一超', '貿易百貨'], \n"
			+ "	['1303', '南亞', '塑膠'], \n"
			+ "	['1301', '台塑', '塑膠'], \n"
			+ "	['1326', '台化', '塑膠'], \n"
			+ "	['2308', '台達電', '電子零組件'], \n"
			+ "	['2327', '國巨', '電子零組件'], \n"
			+ "	['3037', '欣興', '電子零組件'], \n"
			+ "	['2382', '廣達', '電腦週邊'], \n"
			+ "	['2357', '華碩', '電腦週邊'], \n"
			+ "	['2395', '研華', '電腦週邊'], \n"
			+ "	['1605', '華新', '電器電纜'], \n"
			+ "	['1590', '亞德客-KY', '電機'], \n"
			+ "	['2002', '中鋼', '鋼鐵'] \n"
			+ "]}";
	@Test
	public void initTest() throws Exception {
		init();
    	JSONObject stkcodes = new JSONObject(jsonStr);
    	Iterator<String> keys = stkcodes.keys();
    	while (keys.hasNext()) {
    	    String groupName = keys.next();
    		JSONArray stkArray = stkcodes.getJSONArray(groupName);
    		for (int stkIdx = 0; stkIdx < stkArray.length(); stkIdx++) {
    			JSONArray stk = stkArray.getJSONArray(stkIdx);
    			String stkCode = stk.getString(0);
    			String stkName = stk.getString(1);
    			String type = stk.getString(2);
    			Stock stock = new Stock();
    			stock.setStkCode(stkCode);
    			stock.setName(stkName);
    			stock.setStkgroup(groupName);
    			stock.setType(type);
    			stockRepository.save(stock);
    		}
    	}
    	List<Stock> list = stockRepository.findAll();
    	System.out.println("StockRepositoryTest initTest: " + list.get(0));
		assertNotNull(list);

	}
}
