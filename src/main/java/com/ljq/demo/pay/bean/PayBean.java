package com.ljq.demo.pay.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 支付 bean
 * @Author: junqiang.lu
 * @Date: 2018/7/12
 */
@Data
public class PayBean implements Serializable {
    private static final long serialVersionUID = -7551908500227408235L;

    /**
     * 用户订单号
     */
    private String orderNo;
    /**
     * 订单总金额
     */
    private String amount;
    /**
     * 用户实际 ip 地址
     */
    private String ip;

    /**
     * 支付方式
     * 0 未选择
     * 1 微信支付
     * 2 支付宝支付
     */
    private int payType;

}
