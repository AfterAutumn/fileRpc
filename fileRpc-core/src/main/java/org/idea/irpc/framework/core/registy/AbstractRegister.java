package org.idea.irpc.framework.core.registy;

import java.util.List;
import java.util.Map;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static org.idea.irpc.framework.core.common.cache.CommonServerCache.PROVIDER_REGISTRY_CONFIG_SET;

/**
 * @Author jiangshang
 * @Date created in 3:57 下午 2021/12/11
 */
public abstract class AbstractRegister implements RegistryService {


    @Override
    public void register(RegistryConfig registryConfig) {
        PROVIDER_REGISTRY_CONFIG_SET.add(registryConfig);
    }

    @Override
    public void unRegister(RegistryConfig registryConfig) {
        PROVIDER_REGISTRY_CONFIG_SET.remove(registryConfig);
    }

    @Override
    public void subscribe(RegistryConfig registryConfig) {
        SUBSCRIBE_SERVICE_LIST.add(registryConfig);
    }

    @Override
    public void doUnSubscribe(RegistryConfig registryConfig) {
        SUBSCRIBE_SERVICE_LIST.remove(registryConfig.getServiceName());
    }

    /**
     *订阅服务后可自定义做些扩展
     * @param registryConfig
     */
    public abstract void doAfterSubscribe(RegistryConfig registryConfig);

    /**
     * 订阅服务前可自定义做些扩展
     *
     * @param registryConfig
     */
    public abstract void doBeforeSubscribe(RegistryConfig registryConfig);

    /**
     * 留给子类扩展
     *
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);

    /**
     * 获取服务的权重信息
     *
     * @param serviceName
     * @return <ip:port --> urlString>,<ip:port --> urlString>,<ip:port --> urlString>,<ip:port --> urlString>
     */
    public abstract Map<String, String> getServiceWeightMap(String serviceName);

}
