package org.idea.irpc.framework.core.client;


import com.esotericsoftware.minlog.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.idea.irpc.framework.core.common.constance.Constance;
import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.protocol.RpcInvocation;
import org.idea.irpc.framework.core.common.utils.CommonUtils;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.registy.zookeeper.ProviderNodeInfo;
import org.idea.irpc.framework.core.routeModule.Selector;

import java.util.*;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.*;

/**
 * 职责： 当注册中心的节点新增或者移除或者权重变化的时候，这个类主要负责对内存中的url做变更
 * @Author jiangshang
 */
public class ConnectionHandler {

    /**
     * 核心的连接处理器
     * 专门用于负责和服务端构建连接通信
     */
    private static Bootstrap bootstrap;


    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionHandler.bootstrap = bootstrap;
    }

    /**
     * 构建单个连接通道 元操作，既要处理连接，还要统一将连接进行内存存储管理
     *
     * @param providerIp
     * @return
     * @throws InterruptedException
     */
    public static void connect(String providerServiceName, String providerIp) throws InterruptedException {
        if (bootstrap == null) {
            throw new RuntimeException("bootstrap can not be null");
        }
        //格式错误类型的信息
        if (!providerIp.contains(":")) {
            return;
        }
        String[] providerAddress = providerIp.split(":");
        String ip = providerAddress[0];
        Integer port = Integer.parseInt(providerAddress[1]);
        //到底这个channelFuture里面是什么
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        String providerURLInfo = URL_MAP.get(providerServiceName).get(providerIp);
        ProviderNodeInfo providerNodeInfo = RegistryConfig.buildURLFromUrlStr(providerURLInfo);
        //todo 缺少一个将url进行转换的组件
        ChannelFutureService channelFutureService = new ChannelFutureService();
        channelFutureService.setChannelFuture(channelFuture);
        channelFutureService.setHost(ip);
        channelFutureService.setPort(port);
        channelFutureService.setWeight(providerNodeInfo.getWeight());
        channelFutureService.setGroup(providerNodeInfo.getGroup());
        SERVER_ADDRESS.add(providerIp);
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(providerServiceName);
        if (CommonUtils.isEmptyList(channelFutureServices)) {
            channelFutureServices = new ArrayList<>();
        }
        channelFutureServices.add(channelFutureService);
        //例如com.sise.test.UserService会被放入到一个Map集合中，key是服务的名字，value是对应的channel通道的List集合
        CONNECT_MAP.put(providerServiceName, channelFutureServices);
        Selector selector = new Selector();
        selector.setProviderServiceName(providerServiceName);
        IROUTER.refreshRouterArr(selector);
    }

    /**
     * 构建ChannelFuture
     *
     * @param ip
     * @param port
     * @return
     * @throws InterruptedException
     */
    public static ChannelFuture createChannelFuture(String ip, Integer port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        return channelFuture;
    }

    /**
     * 断开连接
     *
     * @param providerServiceName
     * @param providerIp
     */
    public static void disConnect(String providerServiceName, String providerIp) {
        SERVER_ADDRESS.remove(providerIp);
        List<ChannelFutureService> channelFutureServices = CONNECT_MAP.get(providerServiceName);
        if (CommonUtils.isNotEmptyList(channelFutureServices)) {
            Iterator<ChannelFutureService> iterator = channelFutureServices.iterator();
            while (iterator.hasNext()) {
                ChannelFutureService channelFutureService = iterator.next();
                if (providerIp.equals(channelFutureService.getHost() + ":" + channelFutureService.getPort())) {
                    iterator.remove();
                }
            }
        }
    }


    /**
     * 默认走随机策略获取ChannelFuture
     *
     * @param rpcInvocation
     * @return
     */
    public static ChannelFuture getChannelFuture(RpcInvocation rpcInvocation) {
        String providerServiceName = rpcInvocation.getTargetServiceName();
        ChannelFutureService[] channelFutureServices = SERVICE_ROUTER_MAP.get(providerServiceName);
        if (channelFutureServices == null || channelFutureServices.length == 0) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider exist for " + providerServiceName));
            rpcInvocation.setResponse(null);
            //直接交给响应线程那边处理（响应线程在代理类内部的invoke函数中，那边会取出对应的uuid的值，然后判断）
            RESPONSE_QUEUE.put(rpcInvocation.getUuid(),rpcInvocation);
            Log.error("channelFutureWrapper is null");
            return null;
        }
        List<ChannelFutureService> channelFutureWrappersList = new ArrayList<>(channelFutureServices.length);
        for (int i = 0; i < channelFutureServices.length; i++) {
            channelFutureWrappersList.add(channelFutureServices[i]);
        }
        //在客户端会做分组的过滤操作
        //这里不能用Arrays.asList 因为它所生成的list是一个不可修改的list
        CLIENT_FILTER_CHAIN.doFilter(channelFutureWrappersList, rpcInvocation);
        if (Objects.equals(CLIENT_CONFIG.getRouterStrategy(), Constance.ROTATE_HASH_WEIGHT)) {
            CLIENT_FILTER_CHAIN.doFilter(WEIGHT_COORS, rpcInvocation);
        }
        Selector selector = new Selector();
        selector.setProviderServiceName(providerServiceName);
        //设置过滤链路过滤后的服务提供者数组
        selector.setChannelFutureWrappers(channelFutureServices);
        ChannelFuture channelFuture = IROUTER.select(selector).getChannelFuture();
        return channelFuture;
    }


}
