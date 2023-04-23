package org.idea.irpc.framework.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static org.idea.irpc.framework.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * RPC解码器   实现解码的过程中需要考虑是否会有粘包拆包的问题；
 *
 * @Author jiangshang
 */
public class fileRpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议的开头部分的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out)  {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            //校验协议的合法性
            if (!(byteBuf.readShort() == MAGIC_NUMBER)) {
                ctx.close();
                return;
            }
            int length = byteBuf.readInt();
            if (byteBuf.readableBytes() < length) {
                //数据包有异常
                ctx.close();
                return;
            }
            byte[] body = new byte[length];
            byteBuf.readBytes(body);
            fileRpcProtocol fileRpcProtocol = new fileRpcProtocol(body);
            out.add(fileRpcProtocol);
        }
    }
}
