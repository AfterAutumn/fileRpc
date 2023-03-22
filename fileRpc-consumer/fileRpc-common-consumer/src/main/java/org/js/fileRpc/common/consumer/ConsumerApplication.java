package org.js.fileRpc.common.consumer;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author jiangshang
 * @Date created in 10:13 上午 2023/3/19
 */
@SpringBootApplication
@EnableNacosDiscovery //注册中心注解 使用nacos
@NacosPropertySource(dataId = "product_config",autoRefreshed = true) //配置中心注解：autoRefreshed 代表自动刷新注解
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class,args);
    }
}
