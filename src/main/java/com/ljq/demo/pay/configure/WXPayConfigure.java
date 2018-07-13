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
public class WXPayConfigure {

    /**
     * 应用 id
     */
    @Value("${wxPay.appid}")
    private String APP_ID;

    /**
     * 商户号 mch id
     */
    @Value("${wxPay.mchid}")
    private String MCH_ID;

    /**
     * 商品描述
     */
    @Value("${wxPay.body}")
    private String BODY;

    /**
     * 密钥(key)
     */
    @Value("${wxPay.key}")
    private String KEY;

    /**
     * 通知地址
     */
    @Value("${wxPay.notifyUrl}")
    private String NOTIFY_URL;

    /**
     * 交易类型(app支付为 APP)
     */
    @Value("${wxPay.tradeType}")
    private String TRADE_TYPE;

    /**
     * 微信接口返回结果成功状态值(SUCCESS)
     */
    @Value("${wxPay.response.success}")
    private String SUCCESS;

    /**
     * 微信接口返回结果失败状态值(FAIL)
     */
    @Value("${wxPay.response.fall}")
    private String FAIL;

    /**
     * 微信签名字段名(sign)
     */
    @Value("${wxPay.field.sign}")
    private String FIELD_SIGN;

    /**
     * 微信「统一下单」接口地址
     */
    @Value("${wxPay.api.unifiedorder}")
    private String UNIFIEDORDER_URL;

    /**
     * 微信订单查询接口
     */
    @Value("${wxPay.api.orderQuery}")
    private String ORDER_QUERY;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
