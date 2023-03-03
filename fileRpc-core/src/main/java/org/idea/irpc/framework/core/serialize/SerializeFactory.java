package org.idea.irpc.framework.core.serialize;

/**
 * @Author jiangshang
 * @Date created in 6:20 下午 2023/1/17
 */
public interface SerializeFactory {


    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
