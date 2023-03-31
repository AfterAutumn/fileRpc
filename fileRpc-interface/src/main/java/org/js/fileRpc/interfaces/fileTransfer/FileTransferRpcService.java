package org.js.fileRpc.interfaces.fileTransfer;

import org.js.fileRpc.interfaces.bean.FileMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author jiangshang
 * @Date created in 10:08 上午 2023/3/19
 */
public interface FileTransferRpcService {

    String getUserId();

    List<Map<String, String>> findMyGoods(String userId);

    void uploadFile(FileMessage message, byte[] in) throws IOException;

    FileMessage downloadFile(String fileName) throws IOException;

    String testThread(FileMessage message);

    String testNormalThread(FileMessage message);
}
