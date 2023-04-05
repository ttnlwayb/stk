package com.xuan.service.kdj;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class KdjServiceImpl implements KdjService {



    /**
     * 获取ma
     *
     * @param value ma的参数(5，10,15,20等)
     * @return
     */
    public double[] ma(int value, List<Double> list) {
        if (value <= 0) {
            throw new RuntimeException("天数不能小于0");
        }
        // 收盘价
        double[] inClose = new double[list.size()];
        // 返回的数据最后value + 1没有值
        double[] output = new double[list.size() - value + 1];

        for (int i = 0; i < list.size(); i++) {
            inClose[i] = list.get(i);
        }

        Core core = new Core();

        // ma 需要四舍五入
        RetCode code = core.sma(0, inClose.length - 1, inClose, value, new MInteger(), new MInteger(), output);

        if (code == RetCode.Success) {
            output = Arrays.stream(output)
                    .map(e -> new BigDecimal(e)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
                    .toArray();
        }
        return output;
    }

    /**
     * 获取rsi
     * @param value rsi的参数(6,12,24等)
     * @return
     */
    public double[] rsi(int value, List<Double> list) {
        // 收盘价
        double[] inClose = new double[list.size()];
        // 返回的数据最后value没有值
        double[] output = new double[list.size() - value];

        for (int i = 0; i < list.size(); i++) {
            inClose[i] = list.get(i);
        }

        Core core = new Core();

        // rsi 直接截取
        RetCode code = core.rsi(0, inClose.length - 1, inClose, value, new MInteger(), new MInteger(), output);

        if (code == RetCode.Success) {
            output = Arrays.stream(output)
                    .map(e -> new BigDecimal(e)
                            .setScale(2, BigDecimal.ROUND_DOWN).doubleValue())
                    .toArray();
        }
        return output;
    }

    /**
     * 获取kdj(使用默认的9,3,3)
     *
     * @return
     */
    public List<double[]> kdj(List<Double> hights, List<Double> lows, List<Double> closes) {
        // 最高
        double[] inHigh = new double[hights.size()];
        // 收盘
        double[] inClose = new double[hights.size()];
        // 最低
        double[] inLow = new double[hights.size()];

        // 输出的k（最后16位没有值）
        double[] k = new double[hights.size() - 16];
        // 输出的d（最后16位没有值）
        double[] d = new double[hights.size() - 16];
        // 手动计算j值(3*k - 2*d)
        double[] j = new double[hights.size() - 16];

        for (int i = 0; i < hights.size(); i++) {
            inHigh[i] = hights.get(i);
            inClose[i] = closes.get(i);
            inLow[i] = lows.get(i);
        }

        List<double[]> kdjList = null;

        Core core = new Core();

        // kd 直接截取
        RetCode code = core.stoch(0, inHigh.length - 1, inHigh, inLow, inClose, 9, 5, MAType.Ema, 5, MAType.Ema, new MInteger(), new MInteger(), k, d);

        if (code == RetCode.Success) {
            // 计算j值(保留2位)
            for (int i = 0; i < d.length; i++) {
                BigDecimal b1 = new BigDecimal(k[i]).multiply(new BigDecimal(3));
                BigDecimal b2 = new BigDecimal(d[i]).multiply(new BigDecimal(2));
                j[i] = b1.subtract(b2).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            }

            // 计算k值(保留2位)
            k = Arrays.stream(k)
                    .map(e -> new BigDecimal(e)
                            .setScale(2, BigDecimal.ROUND_DOWN).doubleValue())
                    .toArray();

            // 计算d值(保留2位)
            d = Arrays.stream(d)
                    .map(e -> new BigDecimal(e)
                            .setScale(2, BigDecimal.ROUND_DOWN).doubleValue())
                    .toArray();

            kdjList = new ArrayList<double[]>();
            kdjList.add(k);
            kdjList.add(d);
            kdjList.add(j);
        }
        return kdjList == null ? Collections.emptyList() : kdjList;
    }
}