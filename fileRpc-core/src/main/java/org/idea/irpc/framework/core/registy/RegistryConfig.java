package org.idea.irpc.framework.core.registy;

import org.idea.irpc.framework.core.common.cache.CommonServerCache;
import org.idea.irpc.framework.core.common.constance.Constance;
import org.idea.irpc.framework.core.registy.zookeeper.ProviderNodeInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 注册中心配置类（配置属性
 * @Author jiangshang
 */
public class RegistryConfig {

    /**
     * 服务应用名称
     */
    private String applicationName;

    /**
     * 注册到节点到服务名称，例如：com.sise.test.UserService
     */
    private String serviceName;
    /**
     * 这里面可以自定义不限进行扩展
     * 权重
     * 服务提供者的地址
     * 服务提供者的端口
     */
    private Map<String, String> params = new HashMap<>();

    public void addParameter(String key, String value) {
        this.params.putIfAbsent(key, value);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, String> getParameters() {
        return params;
    }

    public void setParameters(Map<String, String> parameters) {
        this.params = parameters;
    }


    /**
     * 将URL转换为写入zk的provider节点下的一段字符串
     *
     * @param registryConfig
     * @return
     */
    public static String buildProviderUrlStr(RegistryConfig registryConfig) {
        String host = registryConfig.getParameters().get("host");
        String port = registryConfig.getParameters().get("port");
        String group = registryConfig.getParameters().get("group");
        String weight = String.valueOf(CommonServerCache.SERVER_CONFIG.getWeight());
        return new String((registryConfig.getApplicationName() + ";" + registryConfig.getServiceName() + ";" + host + ":" + port + ";" + System.currentTimeMillis() + ";" + weight + ";" + group).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将URL转换为写入zk的consumer节点下的一段字符串
     *
     * @param registryConfig
     * @return
     */
    public static String buildConsumerUrlStr(RegistryConfig registryConfig) {
        String host = registryConfig.getParameters().get("host");
        return new String((registryConfig.getApplicationName() + ";" + registryConfig.getServiceName() + ";" + host + ";" + System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }


    /**
     * 将某个节点下的信息转换为一个Provider节点对象
     *
     * @param providerNodeStr
     * @return
     */
    public static ProviderNodeInfo buildURLFromUrlStr(String providerNodeStr) {
        String[] items = providerNodeStr.split(";");
        ProviderNodeInfo providerNodeInfo = new ProviderNodeInfo();
        providerNodeInfo.setApplicationName(items[0]);
        providerNodeInfo.setServiceName(items[1]);
        providerNodeInfo.setAddress(items[2]);
        providerNodeInfo.setRegistryTime(items[3]);
        providerNodeInfo.setWeight(Objects.isNull(CommonServerCache.SERVER_CONFIG)
                ? 100 : CommonServerCache.SERVER_CONFIG.getWeight());
        providerNodeInfo.setGroup(String.valueOf(items[5]));
        return providerNodeInfo;
    }


}
