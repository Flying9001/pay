package com.ljq.demo.pay.common.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ljq.demo.pay.configure.AliPayConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: aliPay 支付宝支付工具类
 * @Author: junqiang.lu
 * @Date: 2018/7/26
 */
public class AliPayManager {

    private static final Logger logger =LoggerFactory.getLogger(AliPayManager.class);

    /**
     * 创建支付订单
     * 支付宝订单信息在本地生成,订单信息生成之后再提交至支付宝服务器(区别与微信)
     *
     * @param orderNo
     * @param amount
     * @param alipayConfigure
     * @return
     */
    public static String createOrder(String orderNo, String amount, AliPayConfigure alipayConfigure) {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfigure.getSERVER_URL(), alipayConfigure.getAPP_ID(),
                alipayConfigure.getAPP_PRIVATE_KEY(), "json", alipayConfigure.getCHARSET(),
                alipayConfigure.getALIPAY_PUBLIC_KEY(), alipayConfigure.getSIGN_TYPE());
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(alipayConfigure.getBODY());
        model.setSubject(alipayConfigure.getSUBJECT());
        model.setOutTradeNo(orderNo);
        // 超时关闭该订单时间
//        model.setTimeoutExpress("30m");
        model.setTotalAmount(amount);
        model.setProductCode(alipayConfigure.getPRODUCT_CODE());
        request.setBizModel(model);
        request.setNotifyUrl(alipayConfigure.getNOTIFY_URL());

        String orderInfo = null;
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            orderInfo = response.getBody();
            logger.info("aliPay orderInfo: {}",orderInfo);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return orderInfo;
    }

    /**
     * 查询订单支付结果(主动获取)
     *
     * @param orderNo 订单编号
     * @param alipayConfigure 支付宝支付配置信息
     * @return
     */
    public static String getPayResult(String orderNo,AliPayConfigure alipayConfigure){
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfigure.getSERVER_URL(), alipayConfigure.getAPP_ID(),
                alipayConfigure.getAPP_PRIVATE_KEY(), "json", alipayConfigure.getCHARSET(),
                alipayConfigure.getALIPAY_PUBLIC_KEY(), alipayConfigure.getSIGN_TYPE());
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        alipayTradeQueryRequest.setBizContent("{" + "\"out_trade_no\":\"" + orderNo + "\"" + "}");

        try {
            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
            if (!alipayTradeQueryResponse.isSuccess()) {
                logger.info("支付宝支付交易不成功");
                return null;
            }
            if("TRADE_SUCCESS".equals(alipayTradeQueryResponse.getTradeStatus())) {
                return alipayTradeQueryResponse.getTradeNo();
            }
        } catch (Exception e) {
            logger.error("支付宝支付结果查询失败",e);
        }
        return null;
    }

    /**
     * 订单结果通知(被动获取)
     *
     * @param requestParams 支付宝回调请求信息
     * @return
     */
    public static Map orderNotify(Map requestParams) {
        // 获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return  params;
    }



}
