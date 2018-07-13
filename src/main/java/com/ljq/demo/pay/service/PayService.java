package com.ljq.demo.pay.service;

import com.ljq.demo.pay.common.api.ApiResult;

/**
 * @Description: 支付业务
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
public interface PayService {

    /**
     * 创建支付订单
     *
     * @param params json 格式参数
     * @return
     */
    ApiResult createPayOrder(String params);





}
