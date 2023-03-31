package org.js.fileRpc.jmh.DynamicThreadTest;

import org.js.fileRpc.interfaces.bean.FileMessage;

import java.io.ByteArrayOutputStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangshang
 * 线程池测试
 * @date 2023/3/31 10:19
 */

public class DynamicThreadTest2 {

    public FileMessage readFile() throws IOException {
        Path filePath = Paths.get("D:\\test\\test.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = Files.newInputStream(filePath)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        byte[] fileData = outputStream.toByteArray();
        FileMessage message = new FileMessage("test.txt", fileData.length);
        message.setFileData(fileData);
        return message;
    }

    public void testThread() throws IOException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                6, 10,
                1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(200),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        // 读取文件
        FileMessage message = readFile();

        for (int i = 0; i < 50; i++) {
            // 提交任务
            threadPool.execute(new UploadTask(message, String.valueOf(i)));
        }
        // 关闭线程池
        threadPool.shutdown();
    }

    private class UploadTask implements Runnable {
        private final FileMessage message;
        private final String fileId;

        public UploadTask(FileMessage message, String fileId) {
            this.message = message;
            this.fileId = fileId;
        }

        @Override
        public void run() {
            try {
                String directoryPath = "D:\\test\\upload";
                File directory = new File(directoryPath);

                // 如果目录不存在，则创建它
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String filePath = directoryPath + "\\" + message.getFileName() + fileId;

                // 创建文件，并写入数据
                try (OutputStream out = Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE_NEW)) {
                    out.write(message.getFileData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
