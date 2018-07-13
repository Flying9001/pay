package com.ljq.demo.pay.common.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 接口返回结果封装
 * @Author: junqiang.lu
 * @Date: 2018/7/11
 */
@Data
public class ApiResult implements Serializable {

    private static final long serialVersionUID = 5616477943476838995L;

    /**
     * return code,default 1000
     */
    private int code = 1000;

    /**
     * return message,default 'SUCCESS'
     */
    private String msg = "SUCCESS";

    /**
     * return data
     */
    private Object data;

    public ApiResult(){
        super();
    }

    public ApiResult(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public static ApiResult success(Object data){
        ApiResult apiResult = new ApiResult();
        apiResult.setData(data);
        return apiResult;
    }

    /**
     * 获取失败状态结果
     * @return
     */
    public static ApiResult failure() {
        return failure(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMsg(),null);
    }

    /**
     * 获取失败状态结果
     * @param msg 错误信息
     * @return
     */
    public static ApiResult failure(String msg) {
        return failure(ResponseCode.FAIL.getCode(), msg, null);
    }

    /**
     * 获取失败状态结果
     * @return
     * @param code
     * @param msg
     */
    public static ApiResult failure(int code, String msg) {
        return failure(code, msg, null);
    }

    /**
     * 获取失败状态结果
     * @param code 错误码
     * @param msg 错误信息
     * @param data 返回结果
     * @return
     **/
    public static ApiResult failure(int code, String msg, Object data) {
        ApiResult result = new ApiResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }







}
