package org.idea.irpc.framework.core.filter.client;

import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.protocol.RpcInvocation;
import org.idea.irpc.framework.core.common.utils.CommonUtils;
import org.idea.irpc.framework.core.filter.IClientFilter;

import java.util.Iterator;
import java.util.List;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.RESPONSE_QUEUE;

/**
 * 直连过滤器
 *
 * @Author jiangshang
 * @Date created in 9:04 上午 2023/2/1
 */
public class DirectInvokeFilterImpl implements IClientFilter {

    @Override
    public void doFilter(List<ChannelFutureService> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if(CommonUtils.isEmpty(url)){
            return;
        }
        Iterator<ChannelFutureService> channelFutureWrapperIterator = src.iterator();
        while (channelFutureWrapperIterator.hasNext()){
            ChannelFutureService channelFutureService = channelFutureWrapperIterator.next();
            if(!(channelFutureService.getHost()+":"+ channelFutureService.getPort()).equals(url)){
                channelFutureWrapperIterator.remove();
            }
        }
        if(CommonUtils.isEmptyList(src)){
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName()  + " in url " + url));
            rpcInvocation.setResponse(null);
            //直接交给响应线程那边处理（响应线程在代理类内部的invoke函数中，那边会取出对应的uuid的值，然后判断）
            RESPONSE_QUEUE.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in url " + url);
        }
    }
}
