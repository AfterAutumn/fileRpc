package org.js.fileRpc.jmh.serialize;

import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.idea.irpc.framework.core.serialize.hessian.HessianSerializeFactory;
import org.idea.irpc.framework.core.serialize.jdk.JdkSerializeFactory;
import org.idea.irpc.framework.core.serialize.kryo.KryoSerializeFactory;
import org.idea.irpc.framework.core.serialize.protostuff.ProtostuffSerializeFactory;
import org.js.fileRpc.interfaces.bean.FileMessage;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 序列化性能比对测试,测试不同序列化的吞吐性
 *
 * @Author jiangshang
 */
public class SerializeCompareTest {

    //生成测试的文件对象
    private static FileMessage buildFileData() throws IOException {
        // 读取本地文件内容
        Path filePath = Paths.get("D:\\test\\test2.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        byte[] fileData = outputStream.toByteArray();
        return new FileMessage("test2.txt", fileData, fileData.length);
    }

    @Benchmark
    public void hessianSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new HessianSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        //FileMessage deserialize = serializeFactory.deserialize(result,FileMessage.class);
    }

    @Benchmark
    public void jdkSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new JdkSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        //FileMessage deserialize = serializeFactory.deserialize(result,FileMessage.class);
    }

    @Benchmark
    public void kryoSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new KryoSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        //FileMessage deserialize = serializeFactory.deserialize(result,FileMessage.class);
    }

    @Benchmark
    public void protostuffSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new ProtostuffSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        //FileMessage deserialize = serializeFactory.deserialize(result,FileMessage.class);
    }


    public static void main(String[] args) throws RunnerException {
        //配置进行1轮热数 测试2轮 2个线程
        //预热的原因 是JVM在代码执行多次会有优化
        Options options = new OptionsBuilder().warmupIterations(1).measurementBatchSize(2)
                .forks(2).build();
        new Runner(options).run();
    }
}
