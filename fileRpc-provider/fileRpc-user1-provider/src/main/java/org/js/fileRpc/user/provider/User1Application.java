package org.js.fileRpc.user.provider;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author jiangshang
 * @Date created in 9:42 上午 2023/3/19
 */
@SpringBootApplication

@EnableNacosDiscovery //注册中心注解 使用nacos
@NacosPropertySource(dataId = "product_config",autoRefreshed = true) //配置中心注解：autoRefreshed 代表自动刷新注解
public class User1Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(User1Application.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
