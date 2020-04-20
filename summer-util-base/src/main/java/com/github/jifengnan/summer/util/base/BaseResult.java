package com.github.jifengnan.summer.util.base;

import java.io.Serializable;

/**
 * 公司级别系统间通信的标准数据格式<p><p>
 * 包含一个标志是否成功的状态（<code>status</code>），一个表示具体执行情况的状态码（<code>code</code>），一个消息（<code>message</code>）及一个数据（<code>data</code>）。
 *
 * @author 纪凤楠 2019-02-13
 */
public class BaseResult<T> implements Serializable {

    /**
     * 请求是否成功。比如一次 HTTP 请求是否执行成功。
     */
    private boolean successful;

    /**
     * 状态（码）<p><p>
     * 对状态的细分。
     * 比如：200 表示请求成功，并且不需要进一步的操作。
     * 100 表示请求成功，但是需要进一步的操作。
     * 或者 400 表示请求失败，是参数有问题。500 表示请求失败，是服务器有问题。
     */
    private int status;

    /**
     * 消息。一般在出错时返回。
     */
    private String message;

    /**
     * 数据。一般在成功时返回。
     */
    private T data;

    public boolean isSuccessful() {
        return successful;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 执行成功的情况下调用。<p><p>
     * 1. 执行成功可以返回数据，也可以不返回<p>
     * 2. <code>data</code>可以是一个数据对象，可以是一个数据列表，也可以是一个提示信息<p>
     *
     * @param data 需要返回的数据
     */
    public static <T> BaseResult<T> generateForSuccess(T data) {
        return generateForSuccess(200, data, null);
    }

    /**
     * 执行成功的情况下调用。<p><p>
     * 1. 执行成功可以返回数据，也可以不返回<p>
     * 2. <code>data</code>可以是一个数据对象，可以是一个数据列表，也可以是一个提示信息<p>
     *
     * @param code    状态码
     * @param data    需要返回的数据
     * @param message 消息
     */
    public static <T> BaseResult<T> generateForSuccess(int code, T data, String message) {
        return new BaseResult<>(true, code, data, message);
    }

    /**
     * 执行失败的情况下调用。执行失败应该提供状态码和错误信息
     *
     * @param message 错误信息
     */
    public static <T> BaseResult<T> generateForFailure(String message) {
        return generateForFailure(400, null, message);
    }

    /**
     * 执行失败的情况下调用。执行失败应该提供状态码和错误信息
     *
     * @param code    状态
     * @param message 错误信息
     */
    public static <T> BaseResult<T> generateForFailure(int code, String message) {
        return generateForFailure(code, null, message);
    }

    /**
     * 执行失败的情况下调用。执行失败应该提供状态码和错误信息
     *
     * @param code    状态
     * @param data    数据
     * @param message 错误信息
     */
    public static <T> BaseResult<T> generateForFailure(int code, T data, String message) {
        if (message == null) {
            throw new IllegalArgumentException("错误消息不能为空");
        }
        return new BaseResult<>(false, code, data, message);
    }

    protected BaseResult(boolean successful, int status, T data, String message) {
        this.successful = successful;
        this.message = message;
        this.data = data;
        this.status = status;
    }

}
