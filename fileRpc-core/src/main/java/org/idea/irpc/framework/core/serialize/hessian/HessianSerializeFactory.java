package org.idea.irpc.framework.core.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.idea.irpc.framework.core.serialize.SerializeFactory;
import org.idea.irpc.framework.core.serialize.jdk.JdkSerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * @Author jiangshang
 */
public class HessianSerializeFactory implements SerializeFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkSerializeFactory.class);

    @Override
    public <T> byte[] serialize(T object) {
        byte[] data = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Hessian2Output output = new Hessian2Output(outputStream);
            output.writeObject(object);
            output.getBytesOutputStream().flush();
            output.completeMessage();
            output.close();
            data = outputStream.toByteArray();
        } catch (Exception e) {
            LOGGER.error("序列化对象失败！失败的原因是: " + object.toString(), e);
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (Objects.isNull(data)) {
            return null;
        }
        Object result = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            Hessian2Input input = new Hessian2Input(inputStream);
            result = input.readObject();
        } catch (Exception e) {
            LOGGER.error("反序列化数据失败！失败的原因是： " + data.toString(), e);
            throw new RuntimeException(e);
        }
        return (T) result;
    }

}
