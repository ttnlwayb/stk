package com.xuan.service.kdj;

import java.util.List;

public interface KdjService {

	List<double[]> kdj(List<Double> hights, List<Double> lows, List<Double> closes);
	double[] rsi(int value, List<Double> list);
}
