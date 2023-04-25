package org.js.fileRpc.jmh.serialize;

import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.idea.irpc.framework.core.serialize.hessian.HessianSerializeFactory;
import org.idea.irpc.framework.core.serialize.jdk.JdkSerializeFactory;
import org.idea.irpc.framework.core.serialize.kryo.KryoSerializeFactory;
import org.js.fileRpc.interfaces.bean.FileMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 不同序列化产生的码流大小测试
 * @Author jiangshang
 */
public class SerializeByteSizeCompareTest {

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

    public void jdkSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new JdkSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        System.out.println("'JDK' 序列化生成的码流大小是：" + result.length);
    }

    public void hessianSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new HessianSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        System.out.println("HESSIAN'序列化生成的码流大小是：" + result.length);
    }


    public void kryoSerializeTest() throws IOException {
        SerializeFactory serializeFactory = new KryoSerializeFactory();
        FileMessage file = buildFileData();
        byte[] result = serializeFactory.serialize(file);
        System.out.println("KRYO' 序列化生成的码流大小是：" + result.length);
    }

    public static void main(String[] args) throws IOException {
        SerializeByteSizeCompareTest serializeByteSizeCompareTest = new SerializeByteSizeCompareTest();
        serializeByteSizeCompareTest.jdkSerializeTest();
        serializeByteSizeCompareTest.hessianSerializeTest();
        serializeByteSizeCompareTest.kryoSerializeTest();
    }
}
