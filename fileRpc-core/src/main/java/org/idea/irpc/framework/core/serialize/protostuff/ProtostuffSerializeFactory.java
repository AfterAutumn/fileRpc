package org.idea.irpc.framework.core.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

/**
 * Protostuff序列化工厂
 * @Author jiangshang
 */
public class ProtostuffSerializeFactory implements SerializeFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtostuffSerializeFactory.class);

    //DEFAULT_BUFFER_SIZE：512
    //每次序列化时使用缓冲区
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public <T> byte[] serialize(T t) {
        if (Objects.isNull(t)) {
            LOGGER.error("序列化失败，传输的对象为空!");
            return null;
        }
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        byte[] bytes;
        try {
            //序列化
            bytes = ProtostuffIOUtil.toByteArray(t, schema, BUFFER);
        } catch (Exception e) {
            LOGGER.error("序列化对象失败 " + t.toString(), e);
            throw new RuntimeException(e);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (Objects.isNull(data) || data.length == 0) {
            LOGGER.error("反序列化失败，要反序列化的数据为空!");
            return null;
        }

        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        //反序列化
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;

    }


}
