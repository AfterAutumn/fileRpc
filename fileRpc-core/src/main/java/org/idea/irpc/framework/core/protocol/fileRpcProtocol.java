package org.idea.irpc.framework.core.protocol;

import java.io.Serializable;
import java.util.Arrays;

import static org.idea.irpc.framework.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * 文件传输框架的自定义协议
 * @Author jiangshang
 */
public class fileRpcProtocol implements Serializable {

    private static final long serialVersionUID = 5359096060555795690L;

    /**
     * 魔法数，用于安全检测，确认请求的协议是否合法。
     */
    private short magicNumber = MAGIC_NUMBER;

    /**
     *议传输核心数据的长度
     */
    private int contentLength;

    /**
     * 具体的传输数据  封装到fileRpcInvocation。
     */
    private byte[] content;

    public fileRpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
