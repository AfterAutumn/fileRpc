package org.idea.irpc.framework.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import org.idea.irpc.framework.core.serialize.SerializeFactory;

/**
 * @Author jiangshang
 * @Date created in 9:51 下午 2023/1/17
 */
public class FastJsonSerializeFactory implements SerializeFactory {

    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }

}
