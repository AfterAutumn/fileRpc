package org.idea.irpc.framework.core.serialize;

/**
 * 序列化工厂抽象接口
 * @Author jiangshang
 */
public interface SerializeFactory {


    /**
     * 序列化
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
