package com.ljq.demo.pay.common.constant;

import java.io.Serializable;

/**
 * @Description: 支付方式常量
 * @Author: junqiang.lu
 * @Date: 2019/4/17
 */
public class PayTypeConst implements Serializable {

    private static final long serialVersionUID = -1264963498451527381L;

    /**
     * 支付方式
     * 1 : 支付宝支付数字标识
     * 11: 支付宝电脑网站支付
     * 12: 支付宝手机网站支付
     * 13: 支付宝 APP 支付
     * AliPay: 支付宝支付文字说明
     *
     * 2: 微信支付标识
     * 21: 微信 NATIVE 支付(二维码支付)
     * 22: 微信 JSAPI 支付
     * 23: 微信 H5 支付
     * 24: 微信 APP 支付
     * 25: 微信 小程序 支付
     * WxPay: 微信支付文字说明
     */
    public static final int ORDER_PAY_TYPE_ALIPAY = 1;
    public static final int ORDER_PAY_TYPE_ALIPAY_PC = 11;
    public static final int ORDER_PAY_TYPE_ALIPAY_WAP = 12;
    public static final int ORDER_PAY_TYPE_ALIPAY_APP = 13;
    public static final String ORDER_PAY_TYPE_ALIPAY_NOTE = "AliPay";
    public static final int ORDER_PAY_TYPE_WX = 2;
    public static final int ORDER_PAY_TYPE_WX_NATIVE = 21;
    public static final int ORDER_PAY_TYPE_WX_JSAPI = 22;
    public static final int ORDER_PAY_TYPE_WX_H5 = 23;
    public static final int ORDER_PAY_TYPE_WX_APP = 24;
    public static final int ORDER_PAY_TYPE_WX_MINI = 25;
    public static final String ORDER_PAY_TYPE_WX_NOTE = "WxPay";

}
