package com.timi.common.exception;


import com.timi.modules.user.constant.CommonCodeEnum;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 响应实体
 */
public final class ResponseBean implements Serializable {

    /**
     * 构建线程池
     */
    private transient ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1),
            r -> new Thread(r,"ResponseBean.then.executor"));

    private String code;
    private String message;
    private Object[] args;
    private Object content;

    private ResponseBean(Builder builder) {
        this.code = builder.code;
        this.args = builder.args;
        this.content = builder.content;
    }
    /**
     *
     * @Title: thenAsync
     * @Description: 后续处理 修改请求参数param，param作为对象传递过去
     * @Author hhh
     * @Date 2021/5/14 10:32
     * @Param [consumer, param]
     * @Return com.timi.common.exception.ResponseBean
     */
    public ResponseBean thenAsync(Consumer<Object> consumer, Object param) {
        //异步执行
        executor.execute(() -> {
            consumer.accept(param);
        });
        return this;
    }

    /**
     * 后续处理
     * 修改请求参数param，param作为对象传递过去
     *
     * @param consumer
     * @return
     */
    public ResponseBean then(Consumer<Object> consumer, Object param) {
        //同步执行
        consumer.accept(param);
        return this;
    }

    /**
     * 用于创建ResponseBean实例
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code = CommonCodeEnum.RESPONSE_SUCCESS.getCode();
        private Object content;
        private Object[] args;

        private Builder() {

        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder args(Object[] args) {
            this.args = args;
            return this;
        }

        public Builder content(Object content) {
            this.content = content;
            return this;
        }

        public ResponseBean build() {
            return new ResponseBean(this);
        }
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

	public String getMessage() {
		return message;
	}
}
