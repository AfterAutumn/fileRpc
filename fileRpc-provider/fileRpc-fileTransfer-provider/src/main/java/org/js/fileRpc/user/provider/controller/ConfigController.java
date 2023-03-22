package org.js.fileRpc.user.provider.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigController 配置控制器
 **/
@RestController
@RequestMapping(value = "/test")
public class ConfigController {

    @NacosValue(value = "${productName}",autoRefreshed = true)
    private  String productName;

    @GetMapping ("/productName")
    public String getProductName(){
        return productName;
    }

}
 