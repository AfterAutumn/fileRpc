package org.js.fileRpc.user.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author jiangshang
 * @Date created in 9:42 上午 2023/3/19
 */
@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
