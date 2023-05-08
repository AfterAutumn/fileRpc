package org.idea.irpc.framework.core.registy.Nacos;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import org.idea.irpc.framework.core.common.constance.RegistryConstance;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.registy.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NacosRegister implements RegistryService {
    private static final Logger logger = LoggerFactory.getLogger(NacosRegister.class);

    private NamingService namingService;
    private String nacosNameSpace;


    public NacosRegister(String registryAddress, String applicationName) {
        try {
            this.namingService = NamingFactory.createNamingService(registryAddress);
        } catch (Exception e) {
            logger.error("Nacos namingService creation failed. exception: {}", e.getMessage());
        }

    }

    public NacosRegister(String registryAddress) {
        try {
            this.namingService = NamingFactory.createNamingService(registryAddress);
        } catch (Exception e) {
            logger.error("Nacos namingService creation failed. exception: {}", e.getMessage());
        }
    }



    @Override
    public void register(RegistryConfig registryConfig) {
        String host = registryConfig.getParameters().get("host");
        String port = registryConfig.getParameters().get("port");
        String applicationName = registryConfig.getApplicationName();

        try {
            String serviceData = JSON.toJSONString(registryConfig);
            this.nacosNameSpace = RegistryConstance.NACOS_REGISTRY_PATH.concat(applicationName);
            //构建Nacos服务实例
            Instance serviceInstance = new Instance();
            serviceInstance.setIp(host);
            serviceInstance.setPort(Integer.parseInt(port));
            Map<String, String> instanceMeta = new HashMap<>();
            instanceMeta.put("rpcProtocol", serviceData);
            serviceInstance.setMetadata(instanceMeta);
            namingService.registerInstance(nacosNameSpace, serviceInstance);
            logger.info("Register {} new service, host: {}, port: {}.", applicationName, host, port);
        } catch (Exception e) {
            logger.error("Register service fail, exception: {}.", e.getMessage());
        }

    }

    @Override
    public void unRegister(RegistryConfig registryConfig) {
        String host = registryConfig.getParameters().get("host");
        String port = registryConfig.getParameters().get("port");
        String applicationName = registryConfig.getApplicationName();
        logger.info("注销服务，服务地址为： host:" + host + "  port:" + port);
        try {
            this.namingService.deregisterInstance(nacosNameSpace, host, Integer.parseInt(port));
        } catch (Exception e) {
            logger.error("注销Nacos服务失败: {}.", e.getMessage());
        } finally {
            try {
                this.namingService.shutDown();
            } catch (Exception ex) {
                logger.error("NamingService shutDown error: {}.", ex.getMessage());
            }
        }

    }

    @Override
    public void subscribe(RegistryConfig registryConfig) {

    }

    @Override
    public void doUnSubscribe(RegistryConfig registryConfig) {

    }
}
