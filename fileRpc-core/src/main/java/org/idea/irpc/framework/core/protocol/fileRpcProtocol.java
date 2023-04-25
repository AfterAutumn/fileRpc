package org.idea.irpc.framework.core.protocol;

import java.io.Serializable;
import java.util.Arrays;

import static org.idea.irpc.framework.core.common.constance.Constance.MAGIC_NUMBER;

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
     *数据包序号  用于大文件切片重传
     */
    private int packetNumber;

    /**
     * 重传标识位
     */
    private boolean retransmission; // 重传标识位
    private long position; // 数据包位置
    /**
     * 剩余数据包数量
     */
    private int remainingPackets;
    /**
     * 数据包发送速率
     */
    private int sendRate;

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

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    public boolean isRetransmission() {
        return retransmission;
    }

    public void setRetransmission(boolean retransmission) {
        this.retransmission = retransmission;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getRemainingPackets() {
        return remainingPackets;
    }

    public void setRemainingPackets(int remainingPackets) {
        this.remainingPackets = remainingPackets;
    }

    public int getSendRate() {
        return sendRate;
    }

    public void setSendRate(int sendRate) {
        this.sendRate = sendRate;
    }

    @Override
    public String toString() {
        return "fileRpcProtocol{" +
                "magicNumber=" + magicNumber +
                ", contentLength=" + contentLength +
                ", packetNumber=" + packetNumber +
                ", retransmission=" + retransmission +
                ", position=" + position +
                ", remainingPackets=" + remainingPackets +
                ", sendRate=" + sendRate +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
