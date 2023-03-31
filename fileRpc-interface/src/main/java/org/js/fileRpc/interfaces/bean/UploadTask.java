package org.js.fileRpc.interfaces.bean;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * @author jiangshang
 * @date 2023/3/31 11:07
 */

public class UploadTask implements Runnable {
    private final FileMessage message;

    public UploadTask(FileMessage message) {
        this.message = message;
    }

    public void run() {
        try {
            System.out.println("开始上传文件，线程名：" + Thread.currentThread().getName());
            String directoryPath = "D:\\test\\upload";
            File directory = new File(directoryPath);
            // 如果目录不存在，则创建它
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = directoryPath + "\\" + UUID.randomUUID() + message.getFileName();

            // 创建文件，并写入数据
            try (OutputStream out = Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE_NEW)) {
                out.write(message.getFileData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}