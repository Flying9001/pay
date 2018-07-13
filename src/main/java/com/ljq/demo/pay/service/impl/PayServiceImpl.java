package com.ljq.demo.pay.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljq.demo.pay.bean.PayBean;
import com.ljq.demo.pay.common.api.ApiResult;
import com.ljq.demo.pay.common.api.ResponseCode;
import com.ljq.demo.pay.common.util.WXPayManager;
import com.ljq.demo.pay.configure.WXPayConfigure;
import com.ljq.demo.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
                case 2 : // TODO aliPay

                default: return new ApiResult(ResponseCode.PAY_TYPE_ERROR);
            }

            /**
             * 支付类型 payType 不是 1 或者 2
             * 返回参数错误
             */
            if (resultMap.isEmpty()) {
                return new ApiResult(ResponseCode.PAY_TYPE_ERROR);
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
}
