package org.idea.irpc.framework.core.routeModule.routerImpl;

import org.idea.irpc.framework.core.common.ChannelFutureWrapper;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.routeModule.IRouter;
import org.idea.irpc.framework.core.routeModule.Selector;

import java.util.List;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * 基于Hash运算的权重负载均衡测量
 * @Author jiangshang
 */
public class HashWeightRouterImpl implements IRouter {


    @Override
    public void refreshRouterArr(Selector selector) {
        //获取服务提供者节点数组
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] channels = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i=0;i<channelFutureWrappers.size();i++) {
            channels[i]=channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(),channels);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector);
    }

    @Override
    public void updateWeight(RegistryConfig registryConfig) {

    }
}