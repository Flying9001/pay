package com.ljq.demo.pay.configure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * @Description: 微信支付配置信息
 * @Author: junqiang.lu
 * @Date: 2018/7/10
 */
@Component
@Configuration
@PropertySource(value = "classpath:wxPay.properties")
@Getter
public class WxPayConfig {

    /**
     * APP 应用 id
     */
    @Value("${wxPay.pay.appId}")
    private String appId;

    /**
     * 公众号应用 id
     */
    @Value("${wxPay.pay.publicAppId}")
    private String publicAppId;

    /**
     * 小程序应用 id
     */
    @Value("${wxPay.pay.miniAppId}")
    private String miniAppId;

    /**
     * 商户号 mch id
     */
    @Value("${wxPay.pay.mchId}")
    private String mchId;

    /**
     * 商户号支付密钥(key)
     */
    @Value("${wxPay.pay.key}")
    private String key;

    /**
     * 商品描述
     */
    @Value("${wxPay.pay.body}")
    private String body;

    /**
     * 通知地址
     */
    @Value("${wxPay.pay.notifyUrl}")
    private String notifyUrl;

    /**
     * H5 支付所需网址
     */
    @Value("${wxPay.pay.wapUrl}")
    private String wapUrl;

    /**
     * H5 交易类型
     */
    @Value("${wxPay.pay.tradeType.h5}")
    private String tradeTypeH5;

    /**
     * NATIVE 交易类型
     */
    @Value("${wxPay.pay.tradeType.native}")
    private String tradeTypeNative;

    /**
     * JSAPI 交易类型
     */
    @Value("${wxPay.pay.tradeType.jsAPI}")
    private String TradeTypeJsApi;

    /**
     * APP 交易类型
     */
    @Value("${wxPay.pay.tradeType.app}")
    private String tradeTypeApp;

    /**
     * 微信接口返回结果成功状态值(responseSuccess)
     */
    @Value("${wxPay.pay.response.success}")
    private String responseSuccess;

    /**
     * 微信接口返回结果失败状态值(responseFail)
     */
    @Value("${wxPay.pay.response.fail}")
    private String responseFail;

    /**
     * 微信签名字段名(sign)
     */
    @Value("${wxPay.pay.field.sign}")
    private String fieldSign;

    /**
     * 微信支付签名方式
     */
    @Value("${wxPay.pay.signType}")
    private String signType;

    /**
     * 微信 APP 支付扩展字段( APP 支付 package: Sign=WXPay)
     */
    @Value("${wxPay.pay.packageApp}")
    private String packageApp;

    /**
     * 微信「统一下单」接口地址
     */
    @Value("${wxPay.pay.api.unifiedOrderUrl}")
    private String unifiedOrderUrl;

    /**
     * 微信查询订单信息接口地址
     */
    @Value("${wxPay.pay.api.orderQueryUrl}")
    private String orderQueryUrl;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
