package org.idea.irpc.framework.core.common.cache;

import org.idea.irpc.framework.core.routeModule.ChannelFuturePollingRef;
import org.idea.irpc.framework.core.routeModule.ChannelFutureService;
import org.idea.irpc.framework.core.protocol.RpcInvocation;
import org.idea.irpc.framework.core.common.config.ClientConfig;
import org.idea.irpc.framework.core.filter.client.ClientFilterChain;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.registy.AbstractRegister;
import org.idea.irpc.framework.core.routeModule.IRouter;
import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.idea.irpc.framework.core.spi.ExtensionLoader;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端公用缓存 存储请求队列等公共信息
 *
 * @Author jiangshang
 */
public class CommonClientCache {

    //请求发送队列
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(5000);
    //结果返回队列
    public static Map<String, Object> RESPONSE_QUEUE = new ConcurrentHashMap<>();
    //provider名称 --> 该服务有哪些集群URL
    public static List<RegistryConfig> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    //com.sise.test.service -> <<ip:host,urlString>,<ip:host,urlString>,<ip:host,urlString>>
    public static Map<String, Map<String,String>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    //每次进行远程调用的时候都是从这里面去选择服务提供者
    public static Map<String, List<ChannelFutureService>> CONNECT_MAP = new ConcurrentHashMap<>();

    //存储着服务路由信息的公共缓存
    public static Map<String, ChannelFutureService[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();

    //根据服务者的权重构建的一维坐标缓存
    public static List<ChannelFutureService> WEIGHT_COORS=new ArrayList<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    public static IRouter IROUTER;
    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;
    public static ClientConfig CLIENT_CONFIG;
    public static ClientFilterChain CLIENT_FILTER_CHAIN;
    public static AbstractRegister ABSTRACT_REGISTER;
    public static ExtensionLoader EXTENSION_LOADER = new ExtensionLoader();
    public static AtomicLong addReqTime = new AtomicLong(0);

}
