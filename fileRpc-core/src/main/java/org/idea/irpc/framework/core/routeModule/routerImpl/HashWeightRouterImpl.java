package org.idea.irpc.framework.core.routeModule.routerImpl;

import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.routeModule.IRouter;
import org.idea.irpc.framework.core.routeModule.Selector;

import java.util.ArrayList;
import java.util.List;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * 基于Hash运算的权重负载均衡策略
 * @Author jiangshang
 */
public class HashWeightRouterImpl implements IRouter {


    @Override
    public void refreshRouterArr(Selector selector) {
        //获取服务提供者节点数组
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureService[] channels = new ChannelFutureService[channelFutureServices.size()];
        List<ChannelFutureService> weightCoors = new ArrayList<>();
        for (int i = 0; i < channelFutureServices.size(); i++) {
            ChannelFutureService tempService = channelFutureServices.get(i);
            channels[i] = tempService;
            //根据服务者的权重构建一维坐标轴
            int loop = tempService.getWeight() / 100;
            for (int j = 0; j < loop; j++) {
                weightCoors.add(tempService);
            }
        }
        WEIGHT_COORS = weightCoors;
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), channels);
    }

    @Override
    public ChannelFutureService select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getHashWeightRouterService(selector);
    }

    @Override
    public void updateWeight(RegistryConfig registryConfig) {

    }
}
