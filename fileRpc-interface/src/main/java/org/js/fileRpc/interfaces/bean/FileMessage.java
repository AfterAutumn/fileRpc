package org.js.fileRpc.interfaces.bean;

import java.io.Serializable;

/**
 * 文件传输封装类
 *  @Author jiangshang
 *  @Date created 上午 2023/3/21
 */
public class FileMessage implements Serializable {
    /**
     * 确保序列化和反序列化过程中类的版本一致性
     */
    private static final long serialVersionUID = 7784347014543708769L;

    /**
     * 传输的文件名
     */
    private String fileName;

    /**
     * 具体的数据
     */
    private byte[] fileData;

    /**
     * 数据的长度
     */
    private long fileLength;

    public FileMessage(String fileName, long fileLength) {
        this.fileName = fileName;
        this.fileLength = fileLength;
    }

    public FileMessage(String fileName, byte[] fileData, long fileLength) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
