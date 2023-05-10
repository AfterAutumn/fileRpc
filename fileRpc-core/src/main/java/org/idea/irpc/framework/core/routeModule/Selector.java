package org.idea.irpc.framework.core.routeModule;

/**
 * @Author jiangshang
 * @Date created in 8:13 下午 2023/1/5
 */
public class Selector {

    /**
     * 服务命名
     * eg: com.sise.test.DataService
     */
    private String providerServiceName;

    /**
     * 经过二次筛选之后的future集合
     */
    private ChannelFutureService[] channelFutureServices;

    public String getProviderServiceName() {
        return providerServiceName;
    }

    public void setProviderServiceName(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }

    public ChannelFutureService[] getChannelFutureWrappers() {
        return channelFutureServices;
    }

    public void setChannelFutureWrappers(ChannelFutureService[] channelFutureServices) {
        this.channelFutureServices = channelFutureServices;
    }
}
