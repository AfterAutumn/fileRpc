package org.idea.irpc.framework.core.routeModule;

import org.idea.irpc.framework.core.common.ChannelFutureWrapper;
import org.idea.irpc.framework.core.registy.RegistryConfig;

/**
 * 路由层抽象类
 * @Author jiangshang
 * @Date created in 8:08 下午 2023/1/5
 */
public interface IRouter {


    /**
     * 刷新路由数组
     *
     * @param selector
     */
    void refreshRouterArr(Selector selector);

    /**
     * 获取到请求到连接通道
     *
     * @return
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新权重信息
     *
     * @param registryConfig
     */
    void updateWeight(RegistryConfig registryConfig);
}
