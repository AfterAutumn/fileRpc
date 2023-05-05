package org.idea.irpc.framework.core.common.event.listener;

import org.idea.irpc.framework.core.common.event.IRpcDestroyEvent;
import org.idea.irpc.framework.core.registy.RegistryConfig;

import static org.idea.irpc.framework.core.common.cache.CommonServerCache.PROVIDER_REGISTRY_CONFIG_SET;
import static org.idea.irpc.framework.core.common.cache.CommonServerCache.REGISTRY_SERVICE;

/**
 * 服务注销 监听器
 *
 * @Author jiangshang
 * @Date created in 3:18 下午 2023/1/8
 */
public class ServiceDestroyListener implements IRpcListener<IRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (RegistryConfig registryConfig : PROVIDER_REGISTRY_CONFIG_SET) {
            REGISTRY_SERVICE.unRegister(registryConfig);
        }
    }
}
