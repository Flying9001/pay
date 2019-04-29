package com.ljq.demo.pay.common.util;

import com.ljq.demo.pay.common.constant.PayTypeConst;
import com.ljq.demo.pay.configure.WxPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 微信支付工具类
 * @Author: junqiang.lu
 * @Date: 2018/7/12
 */
@Slf4j
public class WxPayManager {

    /**
     * 创建微信 NATIVE 预支付订单
     *     需要请求微信统一下单接口
     *
     * @param wxPayConfig 微信支付配置信息
     * @param orderNo 订单号
     * @param amount 总金额(单位: 分)
     * @param ip 客户端实际公网 ip 地址
     * @return 微信预支付订单处理结果
     */
    public static Map<String,String> createNativeOrder(WxPayConfig wxPayConfig, String orderNo, int amount,
                                                       String ip) throws Exception {
        // 生成微信「统一下单」请求数据
        Map<String, String> dataMap = new HashMap<>(16);
        dataMap.put("appid",wxPayConfig.getAppId());
        dataMap.put("mch_id",wxPayConfig.getMchId());
        dataMap.put("nonce_str", UUIDUtil.getUUID());
        dataMap.put("body",wxPayConfig.getBody());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("total_fee",String.valueOf(amount));
        dataMap.put("spbill_create_ip",ip);
        dataMap.put("notify_url",wxPayConfig.getNotifyUrl());
        dataMap.put("trade_type",wxPayConfig.getTradeTypeNative());
        // 签名,请求「统一下单」接口，并解析返回结果
        Map<String, String> responseMap = signAndGetResponse(dataMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(),wxPayConfig.getUnifiedOrderUrl());
        if (responseMap == null || responseMap.isEmpty()) {
            return null;
        }
        // 添加预支付订单创建成功标识
        responseMap.put("pre_pay_order_status", wxPayConfig.getResponseSuccess());

        return responseMap;
    }

    /**
     * 创建微信 JSAPI 预支付订单
     *     需要请求微信统一下单接口
     *
     * @param wxPayConfig 微信支付配置信息
     * @param orderNo 订单号
     * @param amount 总金额(单位: 分)
     * @param ip 客户端实际 ip 地址
     * @param openId 微信用户识别码(openId)
     * @return 微信预支付订单处理结果
     */
    public static Map<String,String> createJsAPIOrder(WxPayConfig wxPayConfig, String orderNo, int amount,
                                                      String ip, String openId) throws Exception {
        // 生成微信「统一下单」请求数据
        Map<String, String> dataMap = new HashMap<>(16);
        dataMap.put("appid",wxPayConfig.getPublicAppId());
        dataMap.put("mch_id",wxPayConfig.getMchId());
        dataMap.put("nonce_str", UUIDUtil.getUUID());
        dataMap.put("body",wxPayConfig.getBody());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("total_fee",String.valueOf(amount));
        dataMap.put("spbill_create_ip",ip);
        dataMap.put("notify_url",wxPayConfig.getNotifyUrl());
        dataMap.put("trade_type",wxPayConfig.getTradeTypeJsApi());
        dataMap.put("openid", openId);
        // 签名,请求「统一下单」接口，并解析返回结果
        Map<String, String> responseMap = signAndGetResponse(dataMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(),wxPayConfig.getUnifiedOrderUrl());
        if (responseMap == null || responseMap.isEmpty()) {
            return null;
        }
        // 生成客户端「调起支付接口」的请求参数
        Map<String, String> resultMap = getJsApiPayOrder(responseMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(),"prepay_id=" + responseMap.get("prepay_id"),
                wxPayConfig.getSignType());
        // 添加预支付订单创建成功标识
        resultMap.put("pre_pay_order_status",wxPayConfig.getResponseSuccess());

        return resultMap;
    }

    /**
     * 创建微信 H5 预支付订单
     *     需要请求微信统一下单接口
     *
     * @param wxPayConfig 微信支付配置信息
     * @param orderNo 订单号
     * @param amount 总金额(单位: 分)
     * @param ip 客户端实际公网 ip 地址
     * @return 微信预支付订单处理结果
     */
    public static Map<String,String> createH5Order(WxPayConfig wxPayConfig, String orderNo, int amount,
                                                   String ip) throws Exception {
        // 生成微信「统一下单」请求数据
        Map<String, String> dataMap = new HashMap<>(16);
        dataMap.put("appid",wxPayConfig.getPublicAppId());
        dataMap.put("mch_id",wxPayConfig.getMchId());
        dataMap.put("nonce_str", UUIDUtil.getUUID());
        dataMap.put("body",wxPayConfig.getBody());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("total_fee",String.valueOf(amount));
        dataMap.put("spbill_create_ip",ip);
        dataMap.put("notify_url",wxPayConfig.getNotifyUrl());
        dataMap.put("trade_type",wxPayConfig.getTradeTypeH5());
        dataMap.put("scene_info","{\"h5_info\": {\"type\":\"WAP\",\"wap_url\": \"" + wxPayConfig.getWapUrl()
                + ",\"wap_name\": " + wxPayConfig.getBody() + "}}");
        // 签名,请求「统一下单」接口，并解析返回结果
        Map<String, String> responseMap = signAndGetResponse(dataMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(),wxPayConfig.getUnifiedOrderUrl());
        if (responseMap == null || responseMap.isEmpty()) {
            return null;
        }
        // 添加预支付订单创建成功标识
        responseMap.put("pre_pay_order_status", wxPayConfig.getResponseSuccess());

        return responseMap;
    }

    /**
     * 创建微信 APP 预支付订单
     *     需要请求微信统一下单接口
     *
     * @param wxPayConfig 微信支付配置信息
     * @param orderNo 订单号
     * @param amount 总金额(单位: 分)
     * @param ip 客户端实际 ip 地址
     * @return 微信预支付订单处理结果
     */
    public static Map<String,String> createAppOrder(WxPayConfig wxPayConfig, String orderNo, int amount,
                                                    String ip) throws Exception {
        // 生成微信「统一下单」请求数据
        Map<String, String> dataMap = new HashMap<>(16);
        dataMap.put("appid",wxPayConfig.getAppId());
        dataMap.put("mch_id",wxPayConfig.getMchId());
        dataMap.put("nonce_str", UUIDUtil.getUUID());
        dataMap.put("body",wxPayConfig.getBody());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("total_fee",String.valueOf(amount));
        dataMap.put("spbill_create_ip",ip);
        dataMap.put("notify_url",wxPayConfig.getNotifyUrl());
        dataMap.put("trade_type",wxPayConfig.getTradeTypeApp());
        // 签名,请求「统一下单」接口，并解析返回结果
        Map<String, String> responseMap = signAndGetResponse(dataMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(),wxPayConfig.getUnifiedOrderUrl());
        if (responseMap == null || responseMap.isEmpty()) {
            return null;
        }
        // 生成客户端「调起支付接口」的请求参数
        Map<String, String> resultMap = getAppPayOrder(responseMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(),wxPayConfig.getPackageApp());
        // 添加预支付订单创建成功标识
        resultMap.put("pre_pay_order_status",wxPayConfig.getResponseSuccess());

        return resultMap;
    }

    /**
     * 创建微信 小程序 预支付订单
     *     需要请求微信统一下单接口
     *
     * @param wxPayConfig 微信支付配置信息
     * @param orderNo 订单号
     * @param amount 总金额(单位: 分)
     * @param ip 客户端实际 ip 地址
     * @param openId 微信用户识别码(openId)
     * @return 微信预支付订单处理结果
     */
    public static Map<String,String> createMiniOrder(WxPayConfig wxPayConfig, String orderNo, int amount,
                                                     String ip, String openId) throws Exception {
        // 生成微信「统一下单」请求数据
        Map<String, String> dataMap = new HashMap<>(16);
        dataMap.put("appid",wxPayConfig.getMiniAppId());
        dataMap.put("mch_id",wxPayConfig.getMchId());
        dataMap.put("nonce_str", UUIDUtil.getUUID());
        dataMap.put("sign_type",wxPayConfig.getSignType());
        dataMap.put("body",wxPayConfig.getBody());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("total_fee",String.valueOf(amount));
        dataMap.put("spbill_create_ip",ip);
        dataMap.put("notify_url",wxPayConfig.getNotifyUrl());
        dataMap.put("trade_type",wxPayConfig.getTradeTypeJsApi());
        dataMap.put("openid", openId);
        // 签名,请求「统一下单」接口，并解析返回结果
        Map<String, String> responseMap = signAndGetResponse(dataMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(), wxPayConfig.getUnifiedOrderUrl());
        if (responseMap == null || responseMap.isEmpty()) {
            return null;
        }
        // 生成客户端「调起支付接口」的请求参数
        Map<String, String> resultMap = getJsApiPayOrder(responseMap,wxPayConfig.getKey(),
                wxPayConfig.getFieldSign(), "prepay_id=" + responseMap.get("prepay_id"),
                wxPayConfig.getSignType());
        // 添加预支付订单创建成功标识
        resultMap.put("pre_pay_order_status",wxPayConfig.getResponseSuccess());

        return resultMap;
    }

    /**
     * 查询支付结果/订单状态
     * @param wxPayConfig
     * @param orderNo
     * @return
     * @throws Exception
     */
    public static Map<String,String> getPayResult(WxPayConfig wxPayConfig, String orderNo)
            throws Exception {
        // 生成微信「订单状态查询」请求数据
        Map<String,String> dataMap = new HashMap<>(16);
        dataMap.put("appid",wxPayConfig.getAppId());
        dataMap.put("mch_id",wxPayConfig.getMchId());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("nonce_str",UUIDUtil.getUUID());

        // 生成签名 MD5 编码
        String md5Sign = SignUtil.getMD5Sign(dataMap,wxPayConfig.getKey(),wxPayConfig.getFieldSign());
        dataMap.put("sign",md5Sign);
        // 发送请求数据
        String respXml = HttpClientUtil.requestWithoutCert(wxPayConfig.getOrderQueryUrl(),dataMap,
                5000,10000);
        // 解析请求数据
        dataMap.clear();
        dataMap = processResponseXml(respXml,wxPayConfig.getKey(),wxPayConfig.getFieldSign());
        log.info(dataMap.toString());
        // 返回结果
        Map<String, String> resultMap = new HashMap<>(16);
        // 支付状态
        if (!StringUtils.isEmpty(dataMap.get("trade_state"))) {
            resultMap.put("tradeState",dataMap.get("trade_state"));
        }
        // 支付流水号
        if (!StringUtils.isEmpty(dataMap.get("transaction_id"))) {
            resultMap.put("payNo",dataMap.get("transaction_id"));
        }
        resultMap.put("payType", PayTypeConst.ORDER_PAY_TYPE_WX_NOTE);
        return resultMap;
    }

    /**
     * 生成签名,调用微信「统一下单」接口，并解析接口返回结果
     * @param dataMap 请求参数
     * @param key app Key
     * @param signField 签名字段
     * @param apiUrl 微信接口地址
     * @return
     */
    private static Map<String, String> signAndGetResponse(Map<String, String>dataMap, String key,
                                                          String signField, String apiUrl) throws Exception {
        // 生成签名 MD5 编码
        String md5Sign = SignUtil.getMD5Sign(dataMap,key,signField);
        dataMap.put("sign",md5Sign);
        // 发送请求数据
        String respXml = HttpClientUtil.requestWithoutCert(apiUrl,dataMap,
                5000,10000);
        // 解析请求数据
        dataMap.clear();
        dataMap = processResponseXml(respXml,key,signField);
        log.info(dataMap.toString());
        // 没有生成预支付订单,返回空
        if(StringUtils.isEmpty(dataMap.get("prepay_id"))){
            return null;
        }
        return dataMap;
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名
     *
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    private static Map<String, String> processResponseXml(String xmlStr, String key, String signField)
            throws Exception {
        String returnCodeField = "return_code";
        String resultFail = "FAIL";
        String resultSuccess = "SUCCESS";
        String returnCode;
        Map<String, String> respData = MapUtil.xml2Map(xmlStr);
        if (respData.containsKey(returnCodeField)) {
            returnCode = respData.get(returnCodeField);
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }
        if (returnCode.equalsIgnoreCase(resultFail)) {
            return respData;
        } else if (returnCode.equalsIgnoreCase(resultSuccess)) {
            /**
             * 签名校验
             */
            if (SignUtil.signValidate(respData, key, signField)) {
                return respData;
            } else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", returnCode, xmlStr));
        }
    }

    /**
     * 生成微信 APP 支付「调起支付接口」请求参数
     *
     * @param data 源数据
     * @param key 签名密钥
     * @param fieldSign 签名字段名(固定值 sign)
     * @param packageStr 微信支付扩展字段(package: Sign=WXPay)
     * @return
     * @throws Exception
     */
    private static Map<String,String> getAppPayOrder(Map<String,String> data, String key, String fieldSign,
                                                     String packageStr) throws Exception {
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("appid",data.get("appid"));
        resultMap.put("partnerid", data.get("mch_id"));
        resultMap.put("prepayid", data.get("prepay_id"));
        resultMap.put("package",packageStr);
        resultMap.put("noncestr",UUIDUtil.getUUID());
        resultMap.put("timestamp",DateUtil.getTimeStampSecond());

        // 生成签名
        String sign = SignUtil.getMD5Sign(resultMap,key,fieldSign);
        resultMap.put("sign",sign);

        return resultMap;
    }

    /**
     * 生成微信 JSAPI/小程序 支付「调起支付接口」请求参数
     *
     * @param data 接口参数
     * @param key 商户号支付秘钥 key
     * @param fieldSign 签名字段
     * @param packageStr 微信支付扩展字段(prepay_id=wx111....)
     * @param signType 签名类型
     * @return
     */
    private static Map<String, String> getJsApiPayOrder(Map<String,String> data, String key, String fieldSign,
                                                        String packageStr, String signType) throws Exception{
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("appId",data.get("appid"));
        resultMap.put("timeStamp",DateUtil.getTimeStampSecond());
        resultMap.put("nonceStr",UUIDUtil.getUUID());
        resultMap.put("package",packageStr);
        resultMap.put("signType",signType);
        // 生成签名
        String sign = SignUtil.getMD5Sign(resultMap,key,fieldSign);
        resultMap.put("paySign",sign);

        return resultMap;
    }



}
