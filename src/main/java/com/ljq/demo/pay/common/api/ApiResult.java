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
     * 返回码，200 正常
     */
    private int code = 200;

    /**
     * 返回信息
     */
    private String msg = "成功";

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 系统当前时间
     */
    private Long timestamp = System.currentTimeMillis();

    /**
     * 获取成功状态结果
     *
     * @return
     */
    public static ApiResult success() {
        return success(null);
    }

    /**
     * 获取成功状态结果
     *
     * @param data 返回数据
     * @return
     */
    public static ApiResult success(Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(ResponseCode.SUCCESS.getCode());
        apiResult.setMsg(ResponseCode.SUCCESS.getMsg());
        apiResult.setData(data);
        return apiResult;
    }

    /**
     * 获取失败状态结果
     *
     * @return
     */
    public static ApiResult failure() {
        return failure(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMsg(), null);
    }

    /**
     * 获取失败状态结果
     *
     * @param msg (自定义)失败消息
     * @return
     */
    public static ApiResult failure(String msg) {
        return failure(ResponseCode.FAIL.getCode(), msg, null);
    }

    /**
     * 获取失败状态结果
     *
     * @param responseCode 返回状态码
     * @return
     */
    public static ApiResult failure(ResponseCode responseCode) {
        return failure(responseCode.getCode(), responseCode.getMsg(), null);
    }

    /**
     * 获取失败状态结果
     *
     * @param responseCode 返回状态码
     * @param data         返回数据
     * @return
     */
    public static ApiResult failure(ResponseCode responseCode, Object data) {
        return failure(responseCode.getCode(), responseCode.getMsg(), data);
    }

    /**
     * 获取失败返回结果
     *
     * @param code 错误码
     * @param msg  错误信息
     * @param data 返回结果
     * @return
     */
    public static ApiResult failure(int code, String msg, Object data) {
        ApiResult result = new ApiResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        if (data instanceof String) {
            String m = (String) data;
            if (!m.matches("^.*error$")) {
                m += "error";
            }
        }
        return result;
    }

}
