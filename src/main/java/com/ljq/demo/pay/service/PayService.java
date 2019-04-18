package com.ljq.demo.pay.service;

import com.ljq.demo.pay.bean.PayBean;
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
     * @param payBean json 订单信息(json 格式参数)
     * @return
     * @throws Exception
     */
    ApiResult createPayOrder(PayBean payBean) throws Exception;

    /**
     * (主动)获取支付结果
     *
     * @param payBean 订单信息(json 格式参数)
     * @return
     * @throws Exception
     */
    ApiResult getPayResult(PayBean payBean) throws Exception;

    /**
     * 微信支付结果异步通知
     *
     * @param request 微信支付回调请求
     * @return 支付结果
     */
    String wxPayNotify(HttpServletRequest request);

    /**
     * 支付宝支付结果异步通知
     *
     * @param request 支付宝回调请求
     * @return
     */
    String aliPayNotify(HttpServletRequest request);

    /**
     * 支付宝支付同步通知返回地址
     * @param request
     * @return
     */
    String aliPayReturnUrl(HttpServletRequest request);




}
