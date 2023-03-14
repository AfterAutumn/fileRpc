package org.js.fileRpc.user.provider.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigController 配置控制器
 **/
@RestController
@RequestMapping("/test")
public class ConfigController {

    @NacosValue(value = "${productName}",autoRefreshed = true)
    private  String productName;

    @RequestMapping("/productName")
    public String getProductName(){

        return productName;
    }
}
 