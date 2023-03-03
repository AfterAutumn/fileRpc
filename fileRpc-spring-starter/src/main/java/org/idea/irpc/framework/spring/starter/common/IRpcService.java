package org.idea.irpc.framework.spring.starter.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author jiangshang
 * @Date created in 7:27 下午 2023/3/7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface IRpcService {

    int limit() default 0;

    String group() default "default";

    String serviceToken() default "";

}
