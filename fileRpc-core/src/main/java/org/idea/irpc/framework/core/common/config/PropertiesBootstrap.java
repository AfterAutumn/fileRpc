package org.idea.irpc.framework.core.common.config;

import org.idea.irpc.framework.core.common.constance.PropertiesConstance;
import java.io.IOException;
import static org.idea.irpc.framework.core.common.constance.Constance.*;

/**
 * 服务配置初始化
 * @Author jiangshang
 */
public class PropertiesBootstrap {

    /**
     * 加载服务端专属配置
     * @return ServerConfig
     */
    public static ServerConfig bulidServerConfiguration() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("构建服务端配置项出错", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(Integer.valueOf(PropertiesLoader.getPropertiesStr(PropertiesConstance.SERVER_PORT)));
        serverConfig.setWeight(Integer.valueOf(PropertiesLoader.getPropertiesStr(PropertiesConstance.SERVER_WEIGHT)));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStr(PropertiesConstance.APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(PropertiesConstance.REGISTER_ADDRESS));
        serverConfig.setRegisterType(PropertiesLoader.getPropertiesStr(PropertiesConstance.REGISTER_TYPE));
        serverConfig.setServerSerialize(PropertiesLoader.getPropertiesStrDefault(PropertiesConstance.SERVER_SERIALIZE_TYPE,JDK_SERIALIZE_TYPE));
        serverConfig.setServerBizThreadNums(PropertiesLoader.getPropertiesIntegerDefault(PropertiesConstance.SERVER_BIZ_THREAD_NUMS,DEFAULT_THREAD_NUMS));
        serverConfig.setServerQueueSize(PropertiesLoader.getPropertiesIntegerDefault(PropertiesConstance.SERVER_QUEUE_SIZE,DEFAULT_QUEUE_SIZE));
        serverConfig.setMaxConnections(PropertiesLoader.getPropertiesIntegerDefault(PropertiesConstance.SERVER_MAX_CONNECTION,DEFAULT_MAX_CONNECTION_NUMS));
        serverConfig.setMaxServerRequestData(PropertiesLoader.getPropertiesIntegerDefault(PropertiesConstance.SERVER_MAX_DATA_SIZE,SERVER_DEFAULT_MSG_LENGTH));
        return serverConfig;
    }

    /**
     * 加载客户端专属配置
     * @return ClientConfig
     */
    public static ClientConfig buildClientConfiguration(){
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("构建客户端配置项出错", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesNotBlank(PropertiesConstance.APPLICATION_NAME));
        clientConfig.setWeight(Integer.valueOf(PropertiesLoader.getPropertiesNotBlank(PropertiesConstance.SERVER_WEIGHT)));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesNotBlank(PropertiesConstance.REGISTER_ADDRESS));
        clientConfig.setRegisterType(PropertiesLoader.getPropertiesNotBlank(PropertiesConstance.REGISTER_TYPE));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStrDefault(PropertiesConstance.PROXY_TYPE, JDK_PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStrDefault(PropertiesConstance.ROUTER_TYPE, RANDOM_ROUTER_TYPE));
        clientConfig.setClientSerialize(PropertiesLoader.getPropertiesStrDefault(PropertiesConstance.CLIENT_SERIALIZE_TYPE, JDK_SERIALIZE_TYPE));
        clientConfig.setTimeOut(PropertiesLoader.getPropertiesIntegerDefault(PropertiesConstance.CLIENT_DEFAULT_TIME_OUT, DEFAULT_TIMEOUT));
        clientConfig.setMaxServerRespDataSize(PropertiesLoader.getPropertiesIntegerDefault(PropertiesConstance.CLIENT_MAX_DATA_SIZE, CLIENT_DEFAULT_MSG_LENGTH));
        return clientConfig;
    }

}
