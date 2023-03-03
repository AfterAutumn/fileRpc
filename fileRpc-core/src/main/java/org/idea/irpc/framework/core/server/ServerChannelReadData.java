package org.idea.irpc.framework.core.server;

import io.netty.channel.ChannelHandlerContext;
import org.idea.irpc.framework.core.common.RpcProtocol;

/**
 * @Author jiangshang
 * @Date created in 11:11 上午 2023/2/13
 */
public class ServerChannelReadData {

    private RpcProtocol rpcProtocol;

    private ChannelHandlerContext channelHandlerContext;

    public RpcProtocol getRpcProtocol() {
        return rpcProtocol;
    }

    public void setRpcProtocol(RpcProtocol rpcProtocol) {
        this.rpcProtocol = rpcProtocol;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
