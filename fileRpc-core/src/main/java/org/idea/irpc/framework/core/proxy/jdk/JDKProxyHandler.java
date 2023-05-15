package org.idea.irpc.framework.core.proxy.jdk;

import org.idea.irpc.framework.core.client.RpcReferenceWrapper;
import org.idea.irpc.framework.core.protocol.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.RESPONSE_QUEUE;
import static org.idea.irpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;
import static org.idea.irpc.framework.core.common.constance.Constance.DEFAULT_TIMEOUT;

/**
 * JDK动态代理具体实现类
 * @Author jiangshang

 */
public class JDKProxyHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private int timeOut = DEFAULT_TIMEOUT;

    public JDKProxyHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        timeOut = Integer.valueOf(String.valueOf(rpcReferenceWrapper.getAttatchments().get("timeOut")));
    }

    private RpcReferenceWrapper rpcReferenceWrapper;

    /**
     * 封装请求参数
     * @return
     */
    private RpcInvocation processParams(Method method, Object[] args) {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        //根据uuid来关联请求和响应
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttatchments());
        return rpcInvocation;
    }

    /**
     * 实现动态代理 对对象进行增强
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = processParams(method, args);
        //把请求放到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        RESPONSE_QUEUE.put(rpcInvocation.getUuid(), OBJECT);
        long beginTime = System.currentTimeMillis();
        //默认接口最大等待时间
        long endTime = beginTime + timeOut;
        int retryTimes = 0;
        //请求中设置的超时次数
        int retryCounts = rpcInvocation.getRetry();
        //判断是否出现了超时异常 或者 是否设置了重置次数
        while (System.currentTimeMillis() < endTime || retryCounts > 0) {
            Object object = RESPONSE_QUEUE.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                RpcInvocation rpcInvocationResp = (RpcInvocation) object;
                //正常结果
                if (rpcInvocationResp.getRetry() == 0 && rpcInvocationResp.getE() == null) {
                    return rpcInvocationResp.getResponse();
                } else if (rpcInvocationResp.getE() != null) {
                    //每次重试之后都会将retry值扣减1
                    if (rpcInvocationResp.getRetry() == 0) {
                        return rpcInvocationResp.getResponse();
                    }
                    //如果是发生了异常或者超时 则会进行重试
                    retryTimes++;
                    //重新请求 把请求放到发送队列中
                    rpcInvocation.setResponse(null);
                    rpcInvocation.setRetry(rpcInvocationResp.getRetry() - 1);
                    RESPONSE_QUEUE.put(rpcInvocation.getUuid(), OBJECT);
                    SEND_QUEUE.add(rpcInvocation);

                }
            }
        }
        //防止key一直存在于map集合中
        RESPONSE_QUEUE.remove(rpcInvocation.getUuid());
        throw new TimeoutException("Wait for response from server on client " + timeOut + "ms,retry times is " + retryTimes + ",service's name is " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}


