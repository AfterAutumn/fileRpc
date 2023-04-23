package org.idea.irpc.framework.core.server;

import io.netty.channel.ChannelHandlerContext;
import org.idea.irpc.framework.core.protocol.fileRpcProtocol;

/**
 * 服务端读取数据封装类
 * @Author jiangshang
 */
public class ServerChannelReadData {

    private fileRpcProtocol fileRpcProtocol;

    private ChannelHandlerContext channelHandlerContext;

    public fileRpcProtocol getRpcProtocol() {
        return fileRpcProtocol;
    }

    public void setRpcProtocol(fileRpcProtocol fileRpcProtocol) {
        this.fileRpcProtocol = fileRpcProtocol;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
