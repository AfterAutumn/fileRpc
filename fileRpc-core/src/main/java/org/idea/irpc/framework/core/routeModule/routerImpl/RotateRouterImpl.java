package org.idea.irpc.framework.core.routeModule.routerImpl;

import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.routeModule.IRouter;
import org.idea.irpc.framework.core.routeModule.Selector;

import java.util.List;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * 轮训策略
 *
 * @Author jiangshang
 */
public class RotateRouterImpl implements IRouter {


    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureService[] channels = new ChannelFutureService[channelFutureServices.size()];
        for (int i = 0; i< channelFutureServices.size(); i++) {
            channels[i]= channelFutureServices.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(),channels);
    }

    @Override
    public ChannelFutureService select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureService(selector);
    }

    @Override
    public void updateWeight(RegistryConfig registryConfig) {

    }
}
