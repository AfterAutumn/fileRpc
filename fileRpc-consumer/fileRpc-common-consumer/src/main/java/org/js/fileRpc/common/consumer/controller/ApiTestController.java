package org.js.fileRpc.common.consumer.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.apache.http.client.utils.HttpClientUtils;
import org.js.fileRpc.interfaces.bean.FileMessage;
import org.js.fileRpc.interfaces.good.GoodRpcService;
import org.js.fileRpc.interfaces.pay.PayRpcService;
import org.js.fileRpc.interfaces.fileTransfer.FileTransferRpcService;
import org.idea.irpc.framework.spring.starter.common.IRpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 10:13 上午 2023/3/19
 */
@RestController
@RequestMapping(value = "/api-test")

public class ApiTestController {

    @IRpcReference
    private FileTransferRpcService fileTransferService;
    @IRpcReference
    private GoodRpcService goodRpcService;
    @IRpcReference
    private PayRpcService payRpcService;

/*    @NacosValue(value = "${productName}",autoRefreshed = true)
    private  String productName;*/

 /*   @RequestMapping("/productName")
    public String getProductName(){

        return productName;
    }*/


    @GetMapping(value = "/do-test")
    public boolean doTest() {
        long begin1 = System.currentTimeMillis();
        fileTransferService.getUserId();
        long end1 = System.currentTimeMillis();
        System.out.println("userRpc--->" + (end1 - begin1) + "ms");
        long begin2 = System.currentTimeMillis();
        goodRpcService.decreaseStock();
        long end2 = System.currentTimeMillis();
        System.out.println("goodRpc--->" + (end2 - begin2) + "ms");
        long begin3 = System.currentTimeMillis();
        payRpcService.doPay();
        long end3 = System.currentTimeMillis();
        System.out.println("payRpc--->" + (end3 - begin3) + "ms");
        return true;
    }


    @GetMapping(value = "/do-test-2")
    public void doTest2() {
        String userId = fileTransferService.getUserId();
        System.out.println("userRpcService result: " + userId);
        boolean goodResult = goodRpcService.decreaseStock();
        System.out.println("goodRpcService result: " + goodResult);
    }

    /**
     * 测试购买商品
     */
    @GetMapping(value = "/buyGood")
    public String buyGood() {
        String userId = fileTransferService.getUserId();
        System.out.println("userRpcService result: " + userId);
        List<String> goods = goodRpcService.selectGoodsNoByUserId(userId);
        return userId + goods.toString();
    }

    /**
     * 测试用户服务
     */
    @GetMapping(value = "/findUser")
    public String findUser() {
        String userId = fileTransferService.getUserId();
        System.out.println("userRpcService result: " + userId);
        return userId;
    }

    @GetMapping(value = "/uploadFile")
    public void testFile() throws IOException {
        // 通过RPC框架获取FileTransferService服务对象
        // 读取本地文件内容
        Path filePath = Paths.get("D:\\test\\test.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = Files.newInputStream(filePath)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        byte[] fileData = outputStream.toByteArray();
        FileMessage message = new FileMessage("test.txt", fileData.length);

        // 调用远程方法上传文件
        fileTransferService.uploadFile(message, fileData);
    }


    @GetMapping(value = "/downloadFile")
    public void downloadFile() throws IOException {
        // 通过RPC框架获取FileTransferService服务对象
        // 调用远程方法下载文件
        FileMessage message = fileTransferService.downloadFile("test.txt");
        byte[] fileData = message.getFileData();

        // 将文件数据写入本地磁盘中
        String filePath = "D:/" + message.getFileName();
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(fileData);
        }
    }


    /**
     * 测试动态线程池
     */
    @GetMapping(value = "/testThread")
    public String testThread() throws IOException {

        String result = fileTransferService.testThread(readFile());
        System.out.println("上传文件结果: " + result);
        return result;
    }

    /**
     * 测试普通线程池的执行效率
     */
    @GetMapping(value = "/testNormalThread")
    public String testNormalThread() throws IOException {

        String result = fileTransferService.testNormalThread(readFile());
        System.out.println("上传文件结果: " + result);
        return result;
    }


    public FileMessage readFile() throws IOException {
        // 读取本地文件内容
        Path filePath = Paths.get("D:\\test\\test.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = Files.newInputStream(filePath)) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        byte[] fileData = outputStream.toByteArray();
        return new FileMessage("test.txt", fileData, fileData.length);
    }


    @GetMapping(value = "/HTTPTest")
    public String HTTPTest() throws IOException {

        String result = fileTransferService.testNormalThread(readFile());
        System.out.println("上传文件结果: " + result);
        return result;
    }

}
