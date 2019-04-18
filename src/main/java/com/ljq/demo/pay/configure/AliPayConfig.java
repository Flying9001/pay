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
public class AliPayConfig {

    /**
     * 支付宝网关
     */
    @Value("${alipay.server.url}")
    private String alipayUrl;

    /**
     * 绑定支付宝的应用 Id
     */
    @Value("${alipay.app.id}")
    private String appId;

    /**
     * 开发者应用私钥
     */
    @Value("${alipay.app.key.private}")
    private String appPrivateKey;

    /**
     * 支付宝公钥
     */
    @Value("${alipay.key.public}")
    private String alipayPublicKey;

    /**
     * 参数返回格式，只支持 json
     */
    @Value("${alipay.format}")
    private String format;

    /**
     * 字符编码格式
     */
    @Value("${alipay.charset}")
    private String charset;

    /**
     * 签名算法,支持 RSA2 和 RSA，推荐使用 RSA2
     */
    @Value("${alipay.sign.type}")
    private String signType;

    /**
     * 支付宝回调通知地址(POST 请求)
     */
    @Value("${alipay.notify.url}")
    private String notifyUrl;

    /**
     * 支付宝支付成功后返回地址(GET 请求)
     */
    @Value("${alipay.return.url}")
    private String returnUrl;

    /**
     * 支付宝电脑网站支付下单并支付接口方法
     */
    @Value("${alipay.method.pc.submit}")
    private String methodPCSubject;

    /**
     * 支付宝手机网站支付下单并支付接口方法
     */
    @Value("${alipay.method.wap.submit}")
    private String methodWapSubmit;

    /**
     * 支付宝电脑网站支付销售产品码
     */
    @Value("${alipay.productCode.pc}")
    private String productCodePC;

    /**
     * 支付宝手机网站支付销售产品码
     */
    @Value("${alipay.productCode.wap}")
    private String productCodeWap;

    /**
     * 支付宝 APP 支付销售产品码
     */
    @Value("${alipay.productCode.app}")
    private String productCodeApp;

    /**
     * 支付宝支付订单标题
     */
    @Value("${alipay.subject}")
    private String subject;

    /**
     * 支付宝异步回调通知成功时返回结果
     */
    @Value("${alipay.response.success}")
    private String responseSuccess;

    /**
     * 支付宝异步回调通知失败时返回结果
     */
    @Value("${alipay.response.fail}")
    private String responseFail;


    /**
     * 支付宝交易状态: 交易创建，等待买家付款
     */
    @Value("${alipay.tradeStatus.pay}")
    private String waitBuyerPay;

    /**
     * 支付宝交易状态: 未付款交易超时关闭，或支付完成后全额退款
     */
    @Value("${alipay.tradeStatus.closed}")
    private String tradeClosed;

    /**
     * 支付宝交易状态: 交易支付成功
     */
    @Value("${alipay.tradeStatus.success}")
    private String tradeSuccess;

    /**
     * 支付宝交易状态: 交易结束，不可退款
     */
    @Value("${alipay.tradeStatus.finished}")
    private String tradeFinished;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
