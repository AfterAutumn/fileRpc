package org.idea.irpc.framework.core.registy.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.idea.irpc.framework.core.common.event.IRpcEvent;
import org.idea.irpc.framework.core.common.event.IRpcListenerLoader;
import org.idea.irpc.framework.core.common.event.IRpcNodeChangeEvent;
import org.idea.irpc.framework.core.common.event.IRpcUpdateEvent;
import org.idea.irpc.framework.core.common.event.data.URLChangeWrapper;
import org.idea.irpc.framework.core.common.utils.CommonUtils;
import org.idea.irpc.framework.core.registy.AbstractRegister;
import org.idea.irpc.framework.core.registy.RegistryConfig;
import org.idea.irpc.framework.core.registy.RegistryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.idea.irpc.framework.core.common.cache.CommonClientCache.CLIENT_CONFIG;
import static org.idea.irpc.framework.core.common.cache.CommonServerCache.IS_STARTED;
import static org.idea.irpc.framework.core.common.cache.CommonServerCache.SERVER_CONFIG;

/**
 * @Author jiangshang
 */
public class ZookeeperRegister extends AbstractRegister implements RegistryService {

    private AbstractZookeeperClient zkClient;

    //设置根节点名称
    private String ROOT = "/fileRpc";

    public AbstractZookeeperClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(AbstractZookeeperClient zkClient) {
        this.zkClient = zkClient;
    }

    //获取服务提供者的路径
    private String getProviderPath(RegistryConfig registryConfig) {
        return ROOT + "/" + registryConfig.getServiceName() + "/provider/" + registryConfig.getParameters().get("host") + ":" + registryConfig.getParameters().get("port");
    }

    private String getConsumerPath(RegistryConfig registryConfig) {
        return ROOT + "/" + registryConfig.getServiceName() + "/consumer/" + registryConfig.getApplicationName() + ":" + registryConfig.getParameters().get("host") + ":";
    }

    public ZookeeperRegister() {
        String registryAddr = CLIENT_CONFIG!= null ? CLIENT_CONFIG.getRegisterAddr() : SERVER_CONFIG.getRegisterAddr();
        this.zkClient = new CuratorZookeeperClient(registryAddr);
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }


    @Override
    public List<String> getProviderIps(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        return nodeDataList;
    }

    @Override
    public Map<String, String> getServiceWeightMap(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        Map<String, String> result = new HashMap<>();
        for (String ipAndHost : nodeDataList) {
            String childData = this.zkClient.getNodeData(ROOT + "/" + serviceName + "/provider/" + ipAndHost);
            result.put(ipAndHost, childData);
        }
        return result;
    }

    @Override
    public void register(RegistryConfig registryConfig) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = RegistryConfig.buildProviderUrlStr(registryConfig);
        if (!zkClient.existNode(getProviderPath(registryConfig))) {
            zkClient.createTemporaryData(getProviderPath(registryConfig), urlStr);
        } else {
            zkClient.deleteNode(getProviderPath(registryConfig));
            zkClient.createTemporaryData(getProviderPath(registryConfig), urlStr);
        }
        super.register(registryConfig);
    }

    @Override
    public void unRegister(RegistryConfig registryConfig) {
        if (!IS_STARTED) {
            return;
        }
        zkClient.deleteNode(getProviderPath(registryConfig));
        super.unRegister(registryConfig);
    }

    @Override
    public void subscribe(RegistryConfig registryConfig) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = RegistryConfig.buildConsumerUrlStr(registryConfig);
        if (!zkClient.existNode(getConsumerPath(registryConfig))) {
            zkClient.createTemporarySeqData(getConsumerPath(registryConfig), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(registryConfig));
            zkClient.createTemporarySeqData(getConsumerPath(registryConfig), urlStr);
        }
        super.subscribe(registryConfig);
    }

    @Override
    public void doAfterSubscribe(RegistryConfig registryConfig) {
        //监听是否有新的服务注册
        String servicePath = registryConfig.getParameters().get("servicePath");
        String newServerNodePath = ROOT + "/" + servicePath;
        //订阅节点地址为：/irpc/com.sise.test.UserService/provider
        this.watchChildNodeData(newServerNodePath);
        String providerIpStrJson = registryConfig.getParameters().get("providerIps");
        List<String> providerIpList = JSON.parseObject(providerIpStrJson, List.class);
        for (String providerIp : providerIpList) {
            //启动环节会触发订阅订阅节点详情地址为：/irpc/com.sise.test.UserService/provider/192.11.11.101:9090
            this.watchNodeDataChange(ROOT + "/" + servicePath + "/" + providerIp);
        }
    }

    /**
     * 订阅服务节点内部的数据变化
     *
     * @param newServerNodePath
     */
    public void watchNodeDataChange(String newServerNodePath) {
        zkClient.watchNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                System.out.println("[watchNodeDataChange] 监听到zk节点下的" + path + "节点数据发生变更");
                String nodeData = zkClient.getNodeData(path);
                ProviderNodeInfo providerNodeInfo = RegistryConfig.buildURLFromUrlStr(nodeData);
                IRpcEvent iRpcEvent = new IRpcNodeChangeEvent(providerNodeInfo);
                IRpcListenerLoader.sendEvent(iRpcEvent);
                watchNodeDataChange(newServerNodePath);
            }
        });
    }

    public void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                String servicePath = watchedEvent.getPath();
                System.out.println("收到子节点" + servicePath + "数据变化");
                List<String> childrenDataList = zkClient.getChildrenData(servicePath);
                if (CommonUtils.isEmptyList(childrenDataList)) {
                    watchChildNodeData(servicePath);
                    return;
                }
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                Map<String, String> nodeDetailInfoMap = new HashMap<>();
                for (String providerAddress : childrenDataList) {
                    String nodeDetailInfo = zkClient.getNodeData(servicePath + "/" + providerAddress);
                    nodeDetailInfoMap.put(providerAddress, nodeDetailInfo);
                }
                urlChangeWrapper.setNodeDataUrl(nodeDetailInfoMap);
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(servicePath.split("/")[2]);
                IRpcEvent iRpcEvent = new IRpcUpdateEvent(urlChangeWrapper);
                IRpcListenerLoader.sendEvent(iRpcEvent);
                //收到回调之后再注册一次监听，这样能保证一直都收到消息
                watchChildNodeData(servicePath);
                for (String providerAddress : childrenDataList) {
                    watchNodeDataChange(servicePath + "/" + providerAddress);
                }
            }
        });
    }

    @Override
    public void doBeforeSubscribe(RegistryConfig registryConfig) {

    }

    @Override
    public void doUnSubscribe(RegistryConfig registryConfig) {
        this.zkClient.deleteNode(getConsumerPath(registryConfig));
        super.doUnSubscribe(registryConfig);
    }

}
