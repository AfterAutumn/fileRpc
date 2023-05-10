package org.idea.irpc.framework.core.common.event.listener;

import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.common.event.IRpcNodeChangeEvent;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.registy.zookeeper.ProviderNodeInfo;

import java.util.List;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * @Author jiangshang
 * @Date created in 8:47 下午 2023/1/16
 */
public class ProviderNodeDataChangeListener implements IRpcListener<IRpcNodeChangeEvent> {

    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureService> channelFutureServices =  CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureService channelFutureService : channelFutureServices) {
            //重置分组信息
            String address = channelFutureService.getHost()+":"+ channelFutureService.getPort();
            if(address.equals(providerNodeInfo.getAddress())){
                channelFutureService.setGroup(providerNodeInfo.getGroup());
                //修改权重
                channelFutureService.setWeight(providerNodeInfo.getWeight());
                RegistryConfig registryConfig = new RegistryConfig();
                registryConfig.setServiceName(providerNodeInfo.getServiceName());
                //更新权重
                IROUTER.updateWeight(registryConfig);
                break;
            }
        }
    }
}
