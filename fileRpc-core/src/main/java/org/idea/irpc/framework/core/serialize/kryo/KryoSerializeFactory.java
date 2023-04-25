package org.idea.irpc.framework.core.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.idea.irpc.framework.core.serialize.jdk.JdkSerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * kryo序列化工厂
 *
 * @Author jiangshang
 * @Date created in 9:41 下午 2023/1/17
 */
public class KryoSerializeFactory implements SerializeFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdkSerializeFactory.class);

    private static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T t) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryos.get();
            kryo.writeClassAndObject(output, t);
            return output.toBytes();
        } catch (IOException e) {
            LOGGER.error("Failed to serialize object: " + t.toString(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryos.get();
            return clazz.cast(kryo.readClassAndObject(input));
        } catch (IOException e) {
            LOGGER.error("Failed to deserialize data: " + data.toString(), e);
            throw new RuntimeException(e);
        }
    }
}

