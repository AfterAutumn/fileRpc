package org.idea.irpc.framework.core.routeModule;

import io.netty.channel.ChannelFuture;

/**
 * 路由层的属性包装类
 * @Author jiangshang
 */
public class ChannelFutureService {

    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    private Integer weight;

    private String group;

    public ChannelFutureService(String host, Integer port, Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }


    public ChannelFutureService() {
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "ChannelFutureWrapper{" +
                "channelFuture=" + channelFuture +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                ", group='" + group + '\'' +
                '}';
    }
}