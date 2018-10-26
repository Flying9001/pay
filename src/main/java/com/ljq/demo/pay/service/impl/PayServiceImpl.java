package com.ljq.demo.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljq.demo.pay.bean.PayBean;
import com.ljq.demo.pay.common.api.ApiResult;
import com.ljq.demo.pay.common.api.ResponseCode;
import com.ljq.demo.pay.common.util.*;
import com.ljq.demo.pay.configure.AliPayConfigure;
import com.ljq.demo.pay.configure.WXPayConfigure;
import com.ljq.demo.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 支付业务具体实现
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
@Service("payService")
public class PayServiceImpl implements PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private WXPayConfigure wxPayConfigure;
    @Autowired
    private AliPayConfigure aliPayConfigure;


    /**
     * 创建支付订单
     *
     * @param params json 格式参数
     * @return
     */
    @Override
    public ApiResult createPayOrder(String params) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PayBean payBean = objectMapper.readValue(params, PayBean.class);

            switch (payBean.getPayType()) {
                case 1:
                    resultMap = WXPayManager.createOrder(wxPayConfigure, payBean);
                    break;
                case 2 :
                    resultMap.put("orderPayInfo",AliPayManager.createOrder(payBean.getOrderNo(),
                                payBean.getAmount(), aliPayConfigure));
                    resultMap.put("pre_pay_order_status","success"); // 预支付订单创建成功标识
                    break;
                default: return new ApiResult(ResponseCode.PAY_TYPE_ERROR);
            }

            /**
             * 生成失败,返回失败信息(微信支付)
             */
            if(!StringUtils.hasLength(resultMap.get("pre_pay_order_status"))){
                return  ApiResult.failure(ResponseCode.FAIL.getCode(),ResponseCode.FAIL.getMsg(),resultMap);
            }
        } catch (Exception e) {
            logger.error("pay Error",e);
            return ApiResult.failure();
        }

        return ApiResult.success(resultMap);
    }


    /**
     * (主动)获取支付结果
     *
     * @param params 订单信息(json 格式参数)
     * @return
     */
    @Override
    public ApiResult getPayResult(String params) {
        if(!StringUtils.hasLength(params)){
            return new ApiResult(ResponseCode.PARAMS_ERROR);
        }
        String payNo = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PayBean payBean = objectMapper.readValue(params, PayBean.class);

            switch (payBean.getPayType()) {
                case 1:
                    payNo = WXPayManager.getPayNo(wxPayConfigure,payBean.getOrderNo());
                    break;
                case 2:
                    payNo = AliPayManager.getPayResult(payBean.getOrderNo(), aliPayConfigure);
                    break;
                default: return new ApiResult(ResponseCode.PARAMS_ERROR);
            }

            if(!StringUtils.hasLength(payNo)){
                return new ApiResult(ResponseCode.PAY_ERROR);
            }
        } catch (IOException e) {
            logger.error("get payResult Error",e);
            return ApiResult.failure();
        } catch (Exception e) {
            logger.error("get payNo Error",e);
            return ApiResult.failure();
        }

        return ApiResult.success(payNo);
    }

    /**
     * 微信支付结果通知
     *
     * @param request 微信支付回调请求
     * @return 支付结果
     */
    @Override
    public String WXPayNotify(HttpServletRequest request) {

        String result = null;
        try {
            InputStream inputStream = request.getInputStream();
            /**
             * 读取通知参数
             */
            String strXML = FileUtil.getStringFromStream(inputStream);
            Map<String,String> reqMap = MapUtil.xml2Map(strXML);
            if(MapUtil.isEmpty(reqMap)){
                logger.debug("request param is null");
                return "FAIL";
            }
            /**
             * 校验签名
             */
            if(!SignUtil.signValidate(reqMap,wxPayConfigure.getKEY(),wxPayConfigure.getFIELD_SIGN())){
                logger.info("wxPay sign is error");
                return "FAIL";
            }
            logger.debug("out_trade_no: {}",reqMap.get("out_trade_no"));
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("return_code","SUCCESS");
            resultMap.put("return_msg","OK");
            result = MapUtil.map2Xml(resultMap);
        } catch (IOException e) {
            logger.error("get request inputStream error",e);
            return "FAIL";
        } catch (Exception e) {
            logger.error("resolve request param error",e);
            return "FAIL";
        }
        return result;
    }

    /**
     * 支付宝支付结果通知
     *
     * @param request 支付宝回调请求
     * @return
     */
    @Override
    public String AliPayNotify(HttpServletRequest request) {
        /**
         * 读取通知参数
         */
        Map<String, String> params = AliPayManager.orderNotify(request.getParameterMap());
        if(MapUtil.isEmpty(params)){
            return "FAIL";
        }
        try {
            /**
             * 签名校验
             */
            if(!AlipaySignature.rsaCheckV1(params, aliPayConfigure.getALIPAY_PUBLIC_KEY(),
                    aliPayConfigure.getCHARSET(), aliPayConfigure.getSIGN_TYPE())){
                return "FAIL";
            }
            if (!"TRADE_SUCCESS".equalsIgnoreCase(params.get("trade_status"))) {
                return "FAIL";
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调验证失败",e);
            return "FAIL";
        }
        return "success";



    }

}
