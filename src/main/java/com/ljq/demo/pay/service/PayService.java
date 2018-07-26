package com.ljq.demo.pay.service;

import com.ljq.demo.pay.common.api.ApiResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 支付业务
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
public interface PayService {

    /**
     * 创建支付订单
     *
     * @param params json 订单信息(json 格式参数)
     * @return
     */
    ApiResult createPayOrder(String params);


    /**
     * (主动)获取支付结果
     *
     * @param params 订单信息(json 格式参数)
     * @return
     */
    ApiResult getPayResult(String params);


    /**
     * 微信支付结果通知
     *
     * @param request 微信支付回调请求
     * @return 支付结果
     */
    String WXPayNotify(HttpServletRequest request);

    /**
     * 支付宝支付结果通知
     *
     * @param request 支付宝回调请求
     * @return
     */
    String AliPayNotify(HttpServletRequest request);




}
