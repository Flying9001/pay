package com.ljq.demo.pay.common.util;

import java.security.MessageDigest;

/**
 * @Description: md5+salt 加密工具类
 * @Author: junqiang.lu
 * @Date: 2018/4/24
 */
public final class MD5Util {

    private MD5Util(){}

    /**
     * 普通 md5 编码
     * @param url
     * @return
     */
    public static String md5(String url) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String t = Integer.toHexString(0xFF & messageDigest[i]);
                if (t.length() == 1) {
                    hexString.append("0" + t);
                } else {
                    hexString.append(t);
                }
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
