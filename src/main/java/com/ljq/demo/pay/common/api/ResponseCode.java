package com.ljq.demo.pay.common.api;

/**
 * @Description: response code enum
 * @Author: junqiang.lu
 * @Date: 2018/7/11
 */
public enum ResponseCode {

    SUCCESS(1000, "SUCCESS"),


    PAY_TYPE_ERROR(1001,"支付方式有误"),
    PARAMS_ERROR(1002,"请求参数错误"),
    PAY_ERROR(1003,"订单未支付或其他状态"),


    FAIL(-1, "FAIL");

    // 返回码
    private int code;

    // 返回信息
    private String msg;



    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    // setter getter
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
