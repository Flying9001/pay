package com.ljq.demo.pay.common.util;

import java.math.BigDecimal;

/**
 * @Description: 金融计算工具类
 * @Author: junqiang.lu
 * @Date: 2019/4/17
 */
public class CalculateUtil {

    /**
     * 默认保留小数位数
     */
    private static final int SCALE = 2;

    private CalculateUtil(){}


    /**
     * 加法计算
     *
     * @param var1 参数1(加数)
     * @param var2 参数2(被加数)
     * @param scale 参数精度,并非计算结果精度(默认保留两位小数)
     * @return 两个参数的和
     */
    public static BigDecimal add(double var1, double var2, int scale){
        if(Math.abs(scale) == 0){
            scale = SCALE;
        }
        scale = Math.abs(scale);
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(var1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(var2));
        bigDecimal1 = bigDecimal1.setScale(scale,BigDecimal.ROUND_DOWN);
        bigDecimal2 = bigDecimal2.setScale(scale,BigDecimal.ROUND_DOWN);

        return bigDecimal1.add(bigDecimal2);
    }

    /**
     * 减法计算
     *
     * @param var1 参数1(减数)
     * @param var2 参数2(被减数)
     * @param scale 参数精度,并非计算结果精度(默认保留两位小数)
     * @return 两个参数的差
     */
    public static BigDecimal subtract(double var1, double var2, int scale){
        if(Math.abs(scale) == 0){
            scale = SCALE;
        }
        scale = Math.abs(scale);
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(var1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(var2));
        bigDecimal1 = bigDecimal1.setScale(scale,BigDecimal.ROUND_DOWN);
        bigDecimal2 = bigDecimal2.setScale(scale,BigDecimal.ROUND_DOWN);

        return bigDecimal1.subtract(bigDecimal2);
    }

    /**
     * 乘法计算
     *
     * @param var1 参数1(乘数)
     * @param var2 参数2(被乘数)
     * @param scale 参数精度,并非结果精度(默认保留两位小数)
     * @return 两个参数的乘积
     */
    public static BigDecimal multiply(double var1, double var2, int scale){
        if(Math.abs(scale) == 0){
            scale = SCALE;
        }
        scale = Math.abs(scale);
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(var1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(var2));
        bigDecimal1 = bigDecimal1.setScale(scale,BigDecimal.ROUND_DOWN);
        bigDecimal2 = bigDecimal2.setScale(scale,BigDecimal.ROUND_DOWN);

        return bigDecimal1.multiply(bigDecimal2);
    }

    /**
     * 除法计算
     *
     * @param var1 参数1(除数)
     * @param var2 参数2(被除数)
     * @param scale 参数精度,并非结果精度(默认保留两位小数)
     * @return 两个参数的商
     */
    public static BigDecimal divide(double var1, double var2,int scale){
        if(Math.abs(scale) == 0){
            scale = SCALE;
        }
        scale = Math.abs(scale);
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(var1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(var2));
        bigDecimal1 = bigDecimal1.setScale(scale,BigDecimal.ROUND_DOWN);
        bigDecimal2 = bigDecimal2.setScale(scale,BigDecimal.ROUND_DOWN);

        return bigDecimal1.divide(bigDecimal2);
    }

}
