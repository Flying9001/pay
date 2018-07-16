package com.ljq.demo.pay.common.util;

import com.ljq.demo.pay.bean.PayBean;
import com.ljq.demo.pay.configure.WXPayConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 微信支付工具类
 * @Author: junqiang.lu
 * @Date: 2018/7/12
 */
public class WXPayManager {

    private static final Logger logger = LoggerFactory.getLogger(WXPayManager.class);


    /**
     * 创建微信预支付订单
     *     需要请求微信统一下单接口
     *
     * @param wxPayConfigure 微信APP支付配置信息
     * @param payBean 支付bean
     * @return 微信预支付订单处理结果
     */
    public static Map<String,String> createOrder(WXPayConfigure wxPayConfigure, PayBean payBean) throws Exception {
        /**
         * 生成微信「统一下单」请求数据
         */
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("appid",wxPayConfigure.getAPP_ID());
        dataMap.put("mch_id",wxPayConfigure.getMCH_ID());
        dataMap.put("nonce_str",UUIDUtil.getUUID());
        dataMap.put("body",wxPayConfigure.getBODY());
        dataMap.put("out_trade_no",payBean.getOrderNo());
        dataMap.put("total_fee",payBean.getAmount());
        dataMap.put("spbill_create_ip",payBean.getIp());
        dataMap.put("notify_url",wxPayConfigure.getNOTIFY_URL());
        dataMap.put("trade_type",wxPayConfigure.getTRADE_TYPE());
        /**
         * 生成签名(MD5 编码)
         */
        String md5Sign = SignUtil.getMD5Sign(dataMap,wxPayConfigure.getKEY(),wxPayConfigure.getFIELD_SIGN());
        dataMap.put("sign",md5Sign);

        /**
         * 发送请求数据
         */
        String respXml = HttpClientUtil.requestWithoutCert(wxPayConfigure.getUNIFIEDORDER_URL(),dataMap,
                5000,10000);
        /**
         * 解析微信返回数据
         */
        dataMap.clear();
        dataMap = processResponseXml(wxPayConfigure,respXml);
        logger.debug(dataMap.toString());
        /**
         * 没有生成预支付订单,直接返回
         */
        if(!StringUtils.hasLength(dataMap.get("prepay_id"))){
            return dataMap;
        }
        /**
         * 生成微信APP支付「调起支付接口」的请求参数
         */
        Map<String, String> resultMap = getPayOrder(dataMap,wxPayConfigure.getKEY(),
                wxPayConfigure.getFIELD_SIGN());
        /**
         * 添加预支付订单创建成功标识
         */
        resultMap.put("pre_pay_order_status","success");

        /**
         * 返回处理结果
         */
        return resultMap;
    }

    /**
     * 查询支付结果/订单状态
     * @param wxPayConfigure
     * @param orderNo
     * @return
     * @throws Exception
     */
    public static Map<String,String> getPayResult(WXPayConfigure wxPayConfigure, String orderNo)
            throws Exception {

        /**
         * 生成微信「订单状态查询」请求数据
         */
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("appid",wxPayConfigure.getAPP_ID());
        dataMap.put("mch_id",wxPayConfigure.getMCH_ID());
        dataMap.put("out_trade_no",orderNo);
        dataMap.put("nonce_str",UUIDUtil.getUUID());
        /**
         * 生成签名 MD5 编码
         */
        String md5Sign = SignUtil.getMD5Sign(dataMap,wxPayConfigure.getKEY(),wxPayConfigure.getFIELD_SIGN());
        dataMap.put("sign",md5Sign);

        /**
         * 发送请求数据
         */
        String resXml = HttpClientUtil.requestWithoutCert(wxPayConfigure.getORDER_QUERY(),dataMap,
                5000,10000);
        logger.debug(resXml);
        /**
         * 解析请求数据
         */
        dataMap.clear();
        dataMap = processResponseXml(wxPayConfigure,resXml);

        /**
         * 返回处理结果
         */
        return dataMap;
    }


    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     *
     * @param wxPayConfigure 微信支付配置信息
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    private static Map<String, String> processResponseXml(WXPayConfigure wxPayConfigure, String xmlStr)
            throws Exception {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = MapUtil.xml2Map(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals(wxPayConfigure.getFAIL())) {
            return respData;
        } else if (return_code.equals(wxPayConfigure.getSUCCESS())) {
            /**
             * 签名校验
             */
            if (SignUtil.signValidate(respData, wxPayConfigure.getKEY(),wxPayConfigure.getFIELD_SIGN())) {
                return respData;
            } else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }

    /**
     * 生成微信APP支付「调起支付接口」请求参数
     *
     * @param data 源数据
     * @param key 签名密钥
     * @param fieldSign 签名字段名(固定值 sign)
     * @return
     * @throws Exception
     */
    private static Map<String,String> getPayOrder(Map<String,String> data, String key, String fieldSign)
            throws Exception {

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("appid",data.get("appid"));
        resultMap.put("partnerid",data.get("mch_id"));
        resultMap.put("prepayid",data.get("prepay_id"));
        resultMap.put("package","Sign=WXPay");
        resultMap.put("noncestr",UUIDUtil.getUUID());
        resultMap.put("timestamp",DateUtil.getTimeStampSecond());
        /**
         * 生成签名
         */
        String sign = SignUtil.getMD5Sign(resultMap,key,fieldSign);
        resultMap.put("sign",sign);

        return resultMap;
    }

    /**
     * 获取支付订单号
     *
     * @param wxPayConfigure 微信支付配置信息
     * @param orderNo 外部订单号
     * @return 微信支付流水号
     * @throws Exception
     */
    public static String getPayNo(WXPayConfigure wxPayConfigure, String orderNo) throws Exception {
        Map<String, String> resultMap = getPayResult(wxPayConfigure,orderNo);
        if(resultMap.isEmpty()){
            return null;
        }
        if(wxPayConfigure.getSUCCESS().equalsIgnoreCase(resultMap.get("trade_state"))){
            return resultMap.get("transaction_id");
        }
        return null;
    }

}
