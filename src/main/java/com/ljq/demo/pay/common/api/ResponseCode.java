package com.ljq.demo.pay.common.api;

import lombok.Getter;

/**
 * @Description: response code enum
 * @Author: junqiang.lu
 * @Date: 2018/7/11
 */
@Getter
public enum ResponseCode {

    /**
     * 基本返回结果
     */
    SUCCESS(200, "成功"),
    FAIL(-1, "失败"),


    /**
     * 支付
     */
    PAY_TYPE_ERROR(1001,"支付方式有误"),
    PARAMS_ERROR(1002,"请求参数错误"),
    PAY_STATUS_ERROR(1003,"订单未支付或其他状态"),
    PAY_SUBMIT_ERROR(1004, "支付提交失败，请稍后重试"),


    /**
     * 未知异常
     */
    UNKNOWN_ERROR(-1000,"系统异常");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
