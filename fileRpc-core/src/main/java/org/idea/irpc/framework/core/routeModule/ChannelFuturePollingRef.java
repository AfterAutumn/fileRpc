package org.idea.irpc.framework.core.routeModule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;


/**
 * @Author jiangshang
 */
public class ChannelFuturePollingRef {


    private Map<String, AtomicLong> referenceMap = new ConcurrentHashMap<>();

    public ChannelFutureService getChannelFutureService(Selector selector) {
        AtomicLong referCount = referenceMap.get(selector.getProviderServiceName());
        if (referCount == null) {
            referCount = new AtomicLong(0);
            referenceMap.put(selector.getProviderServiceName(), referCount);
        }
        ChannelFutureService[] arr = selector.getChannelFutureWrappers();
        long i = referCount.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }

    //基于Hash运算的权重负载均衡策略
    public ChannelFutureService getHashWeightRouterService(Selector selector) {
        //获取当前时间并且只保留最后6位
        long currentTime = System.currentTimeMillis() % 1000000L;
        //获取过滤后的服务提供者
        int size = WEIGHT_COORS.size();
        int index = (int) (currentTime % size);
        return WEIGHT_COORS.get(index);
    }

}
