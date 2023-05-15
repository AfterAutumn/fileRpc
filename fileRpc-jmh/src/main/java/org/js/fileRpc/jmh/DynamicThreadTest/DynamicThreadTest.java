package org.js.fileRpc.jmh.DynamicThreadTest;

import org.js.fileRpc.interfaces.bean.FileMessage;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

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
 * @date 2023/3/31 10:40
 */

@State(Scope.Benchmark)
public class DynamicThreadTest {

    /*private static final int CORE_POOL_SIZE = 6;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_SIZE = 200;

    private ThreadPoolExecutor threadPool;
    private FileMessage message;

    @Setup
    public void prepare() throws IOException {
        // 初始化线程池和数据
        threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAX_POOL_SIZE,
                1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(QUEUE_SIZE),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        message = readFile();
    }

    @TearDown
    public void cleanUp() {
        // 关闭线程池
        threadPool.shutdown();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Threads(1)
    public void testThreadPool() {
        // 提交任务
        threadPool.execute(new UploadTask(message, String.valueOf(Thread.currentThread().getId())));
    }

    private FileMessage readFile() throws IOException {
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

    private static class UploadTask implements Runnable {
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

                String filePath = directoryPath + "\\" + fileId + message.getFileName();

                // 创建文件，并写入数据
                try (OutputStream out = Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE_NEW)) {
                    out.write(message.getFileData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(DynamicThreadTest.class.getSimpleName())
                .build();
        new Runner(options).run();
    }*/

}
