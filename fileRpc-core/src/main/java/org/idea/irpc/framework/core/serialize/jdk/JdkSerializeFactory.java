package org.idea.irpc.framework.core.serialize.jdk;

import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * * JDK 序列化工厂
 * @Author jiangshang
 */
public class JdkSerializeFactory implements SerializeFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdkSerializeFactory.class);

    @Override
    public <T> byte[] serialize(T t) {
        if (t == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(os)) {
            output.writeObject(t);
            // 解决 readObject 时候出现的 EOF 异常
            output.writeObject(null);
            output.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to serialize object: " + t.toString(), e);
            throw new RuntimeException(e);
        } finally {
            closeStream(os);
        }
        return os.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (data == null || data.length == 0) {
            return null;
        }

        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try (ObjectInputStream input = new ObjectInputStream(is)) {
            Object result = input.readObject();
            if (result != null) {
                return clazz.cast(result);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Failed to deserialize data: " + data.toString(), e);
            throw new RuntimeException(e);
        } finally {
            closeStream(is);
        }
        return null;
    }

    private void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close stream", e);
            }
        }
    }

}
