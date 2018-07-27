package com.ljq.demo.pay.configure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * @Description: aliPay 支付宝支付配置信息
 * @Author: junqiang.lu
 * @Date: 2018/7/26
 */
@Component
@Configuration
@PropertySource(value = "classpath:aliPay.properties")
@Getter
public class AliPayConfigure {

    /**
     * 支付宝网关地址
     */
    @Value("${alipay.server.url}")
    private String SERVER_URL;

    /**
     * APP ID
     */
    @Value("${alipay.app.id}")
    private String APP_ID;

    /**
     * 请求数据编码格式
     */
    @Value("${alipay.charset}")
    private String CHARSET;

    /**
     * 密钥加密方式
     */
    @Value("${alipay.sign.type}")
    private String SIGN_TYPE;

    /**
     * APP 应用密钥
     */
    @Value("${alipay.app.key.private}")
    private String APP_PRIVATE_KEY;

    /**
     * APP 应用公钥
     */
    @Value("${alipay.app.key.public}")
    private String APP_PUBLIC_KEY;

    /**
     * 支付宝公钥
     */
    @Value("${alipay.key.public}")
    private String ALIPAY_PUBLIC_KEY;

    /**
     * 回到通知地址
     */
    @Value("${alipay.notify.url}")
    private String NOTIFY_URL;

    /**
     * 固定交易字段
     */
    @Value("${alipay.product.code}")
    private String PRODUCT_CODE;

    /**
     * 订单内容
     */
    @Value("${alipay.goods.body}")
    private String BODY;

    /**
     * 订单主题,标题
     */
    @Value("${alipay.goods.subject}")
    private String SUBJECT;



    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
