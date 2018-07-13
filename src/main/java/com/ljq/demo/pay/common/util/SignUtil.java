package com.ljq.demo.pay.common.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 签名工具类
 * @Author: junqiang.lu
 * @Date: 2018/6/29
 */
public final class SignUtil {

    private SignUtil(){}


    /**
     * 生成 md5 签名.将 map 数据按照 key 的升序进行排列
     * 使用URL键值对的格式(即key1=value1&key2=value2…)
     * 传送的sign参数不参与签名
     *
     * @param data 待签名数据
     * @param key 密钥
     * @param fieldSign 签名字段(sign)
     * @return 签名
     */
    public static String getMD5Sign(final Map<String, String> data, String key,String fieldSign) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(fieldSign)) {
                continue;
            }
            if (data.get(k) != null && data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5Util.md5(sb.toString()).toUpperCase();
    }

    /**
     * 校验签名
     *
     * @param data 待校验数据
     * @param key 密钥
     * @param fieldSign 签名字段(sign)
     * @return
     */
    public static boolean signValidate(Map<String,String> data, String key, String fieldSign) {
        if (!data.containsKey(fieldSign)) {
            return false;
        }
        String sign = data.get(fieldSign);
        return getMD5Sign(data, key, fieldSign).equalsIgnoreCase(sign);
    }

}
