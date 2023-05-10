package org.idea.irpc.framework.core.common.event.listener;

import io.netty.channel.ChannelFuture;
import org.idea.irpc.framework.core.client.ConnectionHandler;
import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.common.event.IRpcUpdateEvent;
import org.idea.irpc.framework.core.common.event.data.URLChangeWrapper;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.registy.zookeeper.ProviderNodeInfo;
import org.idea.irpc.framework.core.routeModule.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * @Author jiangshang
 * @Date created in 10:35 下午 2021/12/18
 */
public class ServiceUpdateListener implements IRpcListener<IRpcUpdateEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUpdateListener.class);

    @Override
    public void callBack(Object t) {
        //获取到子节点的数据信息
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(urlChangeWrapper.getServiceName());
        List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
        Set<String> finalUrl = new HashSet<>();
        List<ChannelFutureService> finalChannelFutureServices = new ArrayList<>();
        for (ChannelFutureService channelFutureService : channelFutureServices) {
            String oldServerAddress = channelFutureService.getHost() + ":" + channelFutureService.getPort();
            //如果老的url没有，说明已经被移除了
            if (!matchProviderUrl.contains(oldServerAddress)) {
                continue;
            } else {
                finalChannelFutureServices.add(channelFutureService);
                finalUrl.add(oldServerAddress);
            }
        }
        //此时老的url已经被移除了，开始检查是否有新的url
        List<ChannelFutureService> newChannelFutureService = new ArrayList<>();
        for (String newProviderUrl : matchProviderUrl) {
            //新增的节点数据
            if (!finalUrl.contains(newProviderUrl)) {
                ChannelFutureService channelFutureService = new ChannelFutureService();
                String host = newProviderUrl.split(":")[0];
                Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);
                channelFutureService.setPort(port);
                channelFutureService.setHost(host);
                String urlStr = urlChangeWrapper.getNodeDataUrl().get(newProviderUrl);
                ProviderNodeInfo providerNodeInfo = RegistryConfig.buildURLFromUrlStr(urlStr);
                channelFutureService.setWeight(providerNodeInfo.getWeight());
                channelFutureService.setGroup(providerNodeInfo.getGroup());
                ChannelFuture channelFuture = null;
                try {
                    channelFuture = ConnectionHandler.createChannelFuture(host, port);
                    LOGGER.debug("channelFuture reconnect,server is {}:{}",host,port);
                    channelFutureService.setChannelFuture(channelFuture);
                    newChannelFutureService.add(channelFutureService);
                    finalUrl.add(newProviderUrl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        finalChannelFutureServices.addAll(newChannelFutureService);
        //最终更新服务在这里
        CONNECT_MAP.put(urlChangeWrapper.getServiceName(), finalChannelFutureServices);
        Selector selector = new Selector();
        selector.setProviderServiceName(urlChangeWrapper.getServiceName());
        IROUTER.refreshRouterArr(selector);
    }
}
