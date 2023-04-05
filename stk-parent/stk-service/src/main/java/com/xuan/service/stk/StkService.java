package com.xuan.service.stk;

import org.json.JSONArray;

public interface StkService {

	JSONArray find(String stkCode);

	JSONArray find(String stkCode, String perd);
}
