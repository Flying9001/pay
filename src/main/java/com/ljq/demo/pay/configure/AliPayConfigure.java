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

    @Value("${alipay.server.url}")
    private String SERVER_URL;

    @Value("${alipay.app.id}")
    private String APP_ID;

    @Value("${alipay.charset}")
    private String CHARSET;

    @Value("${alipay.sign.type}")
    private String SIGN_TYPE;

    @Value("${alipay.app.key.private}")
    private String APP_PRIVATE_KEY;

    @Value("${alipay.app.key.public}")
    private String APP_PUBLIC_KEY;

    @Value("${alipay.key.public}")
    private String ALIPAY_PUBLIC_KEY;

    @Value("${alipay.notify.url}")
    private String NOTIFY_URL;

    @Value("${alipay.product.code}")
    private String PRODUCT_CODE;

    @Value("${alipay.goods.body}")
    private String BODY;

    @Value("${alipay.goods.subject}")
    private String SUBJECT;



    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
