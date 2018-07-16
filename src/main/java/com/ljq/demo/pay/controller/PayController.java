package com.ljq.demo.pay.controller;

import com.ljq.demo.pay.common.api.ApiResult;
import com.ljq.demo.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 支付控制中心
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
@Controller
@RequestMapping("api/pay")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private PayService payService;


    /**
     * 创建支付订单
     *
     * @param params 订单信息( json 格式数据)
     * @return
     */
    @RequestMapping(value = "createPayOrder",method = RequestMethod.POST)
    @ResponseBody
    protected ApiResult createPayOrder(@RequestBody String params){

        ApiResult apiResult = null;

        try {
            apiResult = payService.createPayOrder(params);
        } catch (Exception e) {
            logger.error("支付订单创建失败",e);
            return apiResult.failure();
        }

        return apiResult;
    }

    /**
     * 查询订单支付结果
     *
     * @param params 订单信息( json 格式数据)
     * @return
     */
    @RequestMapping(value = "getPayResult",method = RequestMethod.POST)
    @ResponseBody
    protected ApiResult getPayResult(@RequestBody String params){

        ApiResult apiResult = null;

        try {
            apiResult = payService.getPayResult(params);
        } catch (Exception e) {
            logger.error("订单支付结果查询失败",e);
            return apiResult.failure();
        }

        return apiResult;
    }





}
