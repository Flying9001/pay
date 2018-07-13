package com.ljq.demo.pay.common.util;

import java.util.UUID;

/**
 * @Description: UUID生成工具类
 * @Author: junqiang.lu
 * @Date: 2018/7/12
 */
public final class UUIDUtil {

    private UUIDUtil(){}

    /**
     * 生成UUID字符串
     * @return string UUID
     * */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉"-"符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }

}
