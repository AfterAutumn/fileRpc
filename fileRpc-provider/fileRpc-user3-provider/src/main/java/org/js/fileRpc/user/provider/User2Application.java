package org.js.fileRpc.user.provider;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @Author jiangshang
 * @Date created in 9:42 上午 2023/3/19
 */
@SpringBootApplication
@NacosPropertySource(dataId = "mos", autoRefreshed = true)
public class CoreApplication {

    @NacosInjected
    private NamingService namingService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer serverPort;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @PostConstruct
    public void registerService() throws NacosException {
        namingService.registerInstance(applicationName, "127.0.0.1", serverPort);
    }
}