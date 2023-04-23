package org.idea.irpc.framework.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static org.idea.irpc.framework.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;

/**
 * RPC编码器   客户端发起请求的时候会通过此类进行编码
 * @Author jiangshang
 */
public class fileRpcEncoder extends MessageToByteEncoder<fileRpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, fileRpcProtocol msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
        out.writeBytes(DEFAULT_DECODE_CHAR.getBytes());
    }
}
