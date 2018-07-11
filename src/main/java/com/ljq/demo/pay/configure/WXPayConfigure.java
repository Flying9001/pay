package com.ljq.demo.pay.configure;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
}
