package org.idea.irpc.framework.core.protocol;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 协议中请求数据的封装
 * @Author jiangshang
 */
public class RpcInvocation implements Serializable {

    private static final long serialVersionUID = -1611379458492006176L;
    //请求的方法名
    private String targetMethod;
    //请求的服务名
    private String targetServiceName;
    //请求的参数
    private Object[] args;
    //匹配请求线程和响应线程
    private String uuid;
    //请求相应的数据
    private Object response;
    //异常信息
    private Throwable exception;
    //接口重试
    private int retry;

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public Throwable getE() {
        return exception;
    }

    public void setE(Throwable e) {
        this.exception = e;
    }

    private Map<String, Object> attachments = new ConcurrentHashMap<>();

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetServiceName() {
        return targetServiceName;
    }

    public void setTargetServiceName(String targetServiceName) {
        this.targetServiceName = targetServiceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


    @Override
    public String toString() {
        return "RpcInvocation{" +
                "targetMethod='" + targetMethod + '\'' +
                ", targetServiceName='" + targetServiceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", response=" + response +
                ", e=" + exception +
                ", retry=" + retry +
                ", attachments=" + attachments +
                '}';
    }
}
