package com.ljq.demo.pay.controller;

import com.ljq.demo.pay.bean.PayBean;
import com.ljq.demo.pay.common.api.ApiResult;
import com.ljq.demo.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 支付控制中心
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
@Controller
@RequestMapping("api/pay")
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;


    /**
     * 创建支付订单
     *
     * @param payBean 订单信息( json 格式数据)
     * @return
     */
    @RequestMapping(value = "createPayOrder",method = RequestMethod.POST)
    @ResponseBody
    public ApiResult createPayOrder(@RequestBody PayBean payBean){

        ApiResult apiResult = null;

        try {
            apiResult = payService.createPayOrder(payBean);
        } catch (Exception e) {
            log.error("支付订单创建失败",e);
        }

        return apiResult;
    }

    /**
     * 查询订单支付结果
     *
     * @param payBean 订单信息( json 格式数据)
     * @return
     */
    @RequestMapping(value = "getPayResult",method = RequestMethod.POST)
    @ResponseBody
    public ApiResult getPayResult(@RequestBody PayBean payBean){

        ApiResult apiResult = null;

        try {
            apiResult = payService.getPayResult(payBean);
        } catch (Exception e) {
            log.error("订单支付结果查询失败",e);
        }

        return apiResult;
    }

    /**
     * 微信支付结果异步通知
     *
     * @param request 微信支付回调请求
     * @return
     */
    @RequestMapping(value = "wxPayNotifyUrl",method = {RequestMethod.POST})
    @ResponseBody
    public String WXPayNotify(HttpServletRequest request){

        log.debug("WxPay notify");
        String result = null;
        try {
            result = payService.wxPayNotify(request);
        } catch (Exception e) {
            log.error("微信支付结果通知解析失败",e);
            return "FAIL";
        }

        log.debug(result);
        return result;
    }

    /**
     * 支付宝支付结果异步通知
     *
     * @param request 支付宝回调请求
     * @return
     */
    @RequestMapping(value = "aliPayNotifyUrl",method = RequestMethod.POST)
    @ResponseBody
    public String aliPayNotify(HttpServletRequest request){

        log.debug("AliPay notify");
        String result = null;
        try {
            result = payService.aliPayNotify(request);
        } catch (Exception e) {
            log.error("支付宝结果异步通知解析失败",e);
            return "FAIL";
        }

        log.debug(result);
        return result;
    }

    /**
     * 支付宝支付同步通知返回地址
     *
     * @param request 支付宝回调请求
     * @return
     */
    @RequestMapping(value = "aliPayReturnUrl",method = RequestMethod.GET)
    public String aliPayReturn(HttpServletRequest request){

        log.debug("AliPay return");
        String result = null;
        try {
            result = payService.aliPayReturnUrl(request);
        } catch (Exception e) {
            log.error("支付宝同步通知解析失败",e);
            return "alipay_fail_url";
        }

        log.debug(result);
        return result;
    }


}
