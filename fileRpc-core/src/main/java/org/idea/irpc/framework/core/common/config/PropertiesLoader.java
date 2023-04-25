package org.idea.irpc.framework.core.common.config;

import org.idea.irpc.framework.core.common.constance.Constance;
import org.idea.irpc.framework.core.common.utils.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 配置加载器
 * @Author jiangshang
 */
public class PropertiesLoader {

    //使用volatile修饰属性，保证线程之间对变量的可见性。
    private static volatile Properties properties;

    //使用volatile修饰属性，保证线程之间对变量的可见性。
    private static volatile Map<String, String> propertiesMap = new HashMap<>();

    public static void loadConfiguration() throws IOException {
        //只有当配置不存在的时候才会重新从文件中加载
        if (Objects.isNull(properties)) {
            properties = new Properties();
            //加载默认的配置文件中的项目
            InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(Constance.DEFAULT_PROPERTIES_FILE);
            properties.load(in);
        }
    }

    /**
     * 根据键值获取配置属性（返回String类型）
     * @param key
     * @return
     */
    public static String getPropertiesStr(String key) {
        if (properties != null && !CommonUtils.isEmpty(key)) {
            if (!propertiesMap.containsKey(key)) {
                String value = properties.getProperty(key);
                propertiesMap.put(key, value);
            }
            return String.valueOf(propertiesMap.get(key));
        }
        return null;
    }

    /**
     * 根据键值获取配置属性，可以设置默认值（返回Integer的值）
     */
    public static Integer getPropertiesIntegerDefault(String key, Integer defaultVal) {
        if (properties != null && !CommonUtils.isEmpty(key)) {
            String value = properties.getProperty(key);
            if (value == null) {
                value = String.valueOf(defaultVal);
                propertiesMap.put(key, value);
            } else if (!propertiesMap.containsKey(key)) {
                propertiesMap.put(key, value);
            }
            return Integer.valueOf(value);
        }
        return defaultVal;
    }

    public static String getPropertiesNotBlank(String key) {
        String val = getPropertiesStr(key);
        if (val == null || val.equals("")) {
            throw new IllegalArgumentException(key + " 配置为空异常");
        }
        return val;
    }

    public static String getPropertiesStrDefault(String key, String defaultVal) {
        String val = getPropertiesStr(key);
        return val == null || val.equals("") ? defaultVal : val;
    }
}

