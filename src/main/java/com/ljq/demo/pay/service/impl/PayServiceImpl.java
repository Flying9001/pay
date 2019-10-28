package com.ljq.demo.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ljq.demo.pay.bean.PayBean;
import com.ljq.demo.pay.common.api.ApiResult;
import com.ljq.demo.pay.common.api.ResponseCode;
import com.ljq.demo.pay.common.constant.PayTypeConst;
import com.ljq.demo.pay.common.util.*;
import com.ljq.demo.pay.configure.AliPayConfig;
import com.ljq.demo.pay.configure.WxPayConfig;
import com.ljq.demo.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 支付业务具体实现
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
@Service("payService")
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private WxPayConfig wxPayConfig;
    @Autowired
    private AliPayConfig aliPayConfig;


    /**
     * 创建支付订单
     *
     * @param payBean json 格式参数
     * @return
     */
    @Override
    public ApiResult createPayOrder(PayBean payBean) throws Exception {
        // 微信支付金额换算
        int amountWxPay = CalculateUtil.multiply(Double.valueOf(payBean.getAmount()), 100, 2).intValue();
        // 返回结果
        Map<String, String> resultMap = new HashMap<>(16);
        // 创建支付订单
        switch (payBean.getPayType()) {
            case PayTypeConst.ORDER_PAY_TYPE_ALIPAY_PC:
                // 支付宝电脑网站支付
                String aliPayPCForm = AliPayManager.createPCOrder(payBean.getOrderNo(),
                        String.valueOf(payBean.getAmount()), aliPayConfig);
                if (!StringUtils.isEmpty(aliPayPCForm)) {
                    resultMap.put("prePayOrderInfo",aliPayPCForm);
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_ALIPAY_WAP:
                // 支付宝手机网站支付
                String aliPayWapForm = AliPayManager.createWapOrder(payBean.getOrderNo(),
                        String.valueOf(payBean.getAmount()), aliPayConfig);
                if (!StringUtils.isEmpty(aliPayWapForm)) {
                    resultMap.put("prePayOrderInfo",aliPayWapForm);
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_ALIPAY_APP:
                // 支付宝 APP 支付
                String aliPayAppForm = AliPayManager.createAppOrder(payBean.getOrderNo(),
                        String.valueOf(payBean.getAmount()), aliPayConfig);
                if (!StringUtils.isEmpty(aliPayAppForm)) {
                    resultMap.put("prePayOrderInfo",aliPayAppForm);
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_NATIVE:
                // 微信 NATIVE 支付(二维码)
                Map<String,String> wxPayNativeMap = WxPayManager.createNativeOrder(wxPayConfig,
                        payBean.getOrderNo() + PayTypeConst.ORDER_PAY_TYPE_WX_NATIVE,
                        amountWxPay, payBean.getIp());
                if (wxPayNativeMap != null &&
                        Objects.equals(wxPayNativeMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    resultMap.put("prePayOrderInfo",wxPayNativeMap.get("code_url"));
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_JSAPI:
                // 微信 JsAPI 支付(公众号)
                if (StringUtils.isEmpty(payBean.getOpenId())) {
                    return ApiResult.failure(ResponseCode.PAY_SUBMIT_ERROR);
                }
                Map<String, String> wxPayJsAPIMap = WxPayManager.createJsAPIOrder(wxPayConfig,
                        payBean.getOrderNo() + PayTypeConst.ORDER_PAY_TYPE_WX_JSAPI,
                        amountWxPay, payBean.getIp(), payBean.getOpenId());
                if (wxPayJsAPIMap != null &&
                        Objects.equals(wxPayJsAPIMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    return ApiResult.success(wxPayJsAPIMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_H5:
                // 微信 H5 支付
                Map<String, String> wxPayH5Map = WxPayManager.createH5Order(wxPayConfig,
                        payBean.getOrderNo() + PayTypeConst.ORDER_PAY_TYPE_WX_H5,
                        amountWxPay, payBean.getIp());
                if (wxPayH5Map != null &&
                        Objects.equals(wxPayH5Map.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    resultMap.put("prePayOrderInfo",wxPayH5Map.get("mweb_url"));
                    return ApiResult.success(resultMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_APP:
                // 微信 APP 支付
                Map<String, String> wxPayAppMap = WxPayManager.createAppOrder(wxPayConfig,
                        payBean.getOrderNo() + PayTypeConst.ORDER_PAY_TYPE_WX_APP,
                        amountWxPay, payBean.getIp());
                if (wxPayAppMap != null &&
                        Objects.equals(wxPayAppMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    return ApiResult.success(wxPayAppMap);
                }
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_MINI:
                // 微信 小程序 支付
                if (StringUtils.isEmpty(payBean.getOpenId())) {
                    return ApiResult.failure(ResponseCode.PAY_SUBMIT_ERROR);
                }
                Map<String, String> wxPayMiniMap = WxPayManager.createJsAPIOrder(wxPayConfig,
                        payBean.getOrderNo() + PayTypeConst.ORDER_PAY_TYPE_WX_MINI,
                        amountWxPay, payBean.getIp(), payBean.getOpenId());
                if (wxPayMiniMap != null &&
                        Objects.equals(wxPayMiniMap.get("pre_pay_order_status"), wxPayConfig.getResponseSuccess())) {
                    return ApiResult.success(wxPayMiniMap);
                }
                break;
            default:
                return ApiResult.failure(ResponseCode.PAY_TYPE_ERROR);
        }
        return ApiResult.failure(ResponseCode.PAY_SUBMIT_ERROR);
    }

    /**
     * (主动)获取支付结果
     *
     * @param payBean 订单信息(json 格式参数)
     * @return
     */
    @Override
    public ApiResult getPayResult(PayBean payBean) throws Exception {
        // 返回结果
        Map<String, String> resultMap;
        switch (payBean.getPayType()) {
            case PayTypeConst.ORDER_PAY_TYPE_ALIPAY_PC:
            case PayTypeConst.ORDER_PAY_TYPE_ALIPAY_WAP:
            case PayTypeConst.ORDER_PAY_TYPE_ALIPAY_APP:
                resultMap = AliPayManager.getPayResult(aliPayConfig, payBean.getOrderNo());
                break;
            case PayTypeConst.ORDER_PAY_TYPE_WX_NATIVE:
            case PayTypeConst.ORDER_PAY_TYPE_WX_JSAPI:
            case PayTypeConst.ORDER_PAY_TYPE_WX_H5:
            case PayTypeConst.ORDER_PAY_TYPE_WX_APP:
            case PayTypeConst.ORDER_PAY_TYPE_WX_MINI:
                resultMap = WxPayManager.getPayResult(wxPayConfig, payBean.getOrderNo() + payBean.getPayType());
                break;
            default:
                return ApiResult.failure(ResponseCode.PAY_TYPE_ERROR);
        }
        if (MapUtil.isEmpty(resultMap)) {
            return ApiResult.failure(ResponseCode.PAY_STATUS_ERROR);
        }

        return ApiResult.success(resultMap);
    }

    /**
     * 微信支付结果通知
     *
     * @param request 微信支付回调请求
     * @return 支付结果
     */
    @Override
    public String wxPayNotify(HttpServletRequest request) {

        String result = null;
        try {
            InputStream inputStream = request.getInputStream();
            /**
             * 读取通知参数
             */
            String strXML = FileUtil.getStringFromStream(inputStream);
            Map<String,String> reqMap = MapUtil.xml2Map(strXML);
            if(MapUtil.isEmpty(reqMap)){
                log.warn("request param is null");
                return wxPayConfig.getResponseFail();
            }
            /**
             * 校验签名
             */
            if(!SignUtil.signValidate(reqMap, wxPayConfig.getKey(), wxPayConfig.getFieldSign())){
                log.warn("wxPay sign is error");
                return wxPayConfig.getResponseFail();
            }
            String orderNo = reqMap.get("out_trade_no").substring(0,reqMap.get("out_trade_no").length()-2);
            log.debug("微信支付回调,订单编号: {}", orderNo);
            // TODO 其他业务处理


            Map<String, String> resultMap = new HashMap<>(16);
            resultMap.put("return_code",wxPayConfig.getResponseSuccess());
            resultMap.put("return_msg","OK");
            result = MapUtil.map2Xml(resultMap);
        } catch (IOException e) {
            log.error("get request inputStream error",e);
            return wxPayConfig.getResponseFail();
        } catch (Exception e) {
            log.error("resolve request param error",e);
            return wxPayConfig.getResponseFail();
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
    public String aliPayNotify(HttpServletRequest request) {
        // 读取通知参数
        Map<String, String> params = AliPayManager.getNotifyParams(request.getParameterMap());
        if(MapUtil.isEmpty(params)){
            return aliPayConfig.getResponseFail();
        }
        try {
            // 签名校验
            if(!AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipayPublicKey(),
                    aliPayConfig.getCharset(), aliPayConfig.getSignType())){
                return aliPayConfig.getResponseFail();
            }
            String orderNo = params.get("out_trade_no");
            log.debug("支付宝回调,订单编号: {}",orderNo);
            // TODO 其他业务处理



        } catch (AlipayApiException e) {
            log.error("支付宝回调验证失败",e);
            return aliPayConfig.getResponseFail();
        }
        return aliPayConfig.getResponseSuccess();
    }

    /**
     * 支付宝支付同步通知返回地址
     * @param request
     * @return
     */
    @Override
    public String aliPayReturnUrl(HttpServletRequest request) {
        // 读取通知参数
        Map<String, String> params = AliPayManager.getNotifyParams(request.getParameterMap());
        if(MapUtil.isEmpty(params)){
            return "alipay_fail_url";
        }
        try {
            // 签名校验
            if(!AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipayPublicKey(),
                    aliPayConfig.getCharset(), aliPayConfig.getSignType())){
                return "alipay_fail_url";
            }

        } catch (AlipayApiException e) {
            log.error("支付宝回调验证失败",e);
            return aliPayConfig.getResponseFail();
        }
        return "alipay_success_url";
    }

}
