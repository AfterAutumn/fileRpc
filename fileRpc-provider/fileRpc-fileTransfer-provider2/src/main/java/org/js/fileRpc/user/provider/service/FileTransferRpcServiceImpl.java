package org.js.fileRpc.user.provider.service;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import org.js.fileRpc.interfaces.bean.FileMessage;
import org.js.fileRpc.interfaces.bean.UploadTask;
import org.js.fileRpc.interfaces.good.GoodRpcService;
import org.js.fileRpc.interfaces.pay.PayRpcService;
import org.js.fileRpc.interfaces.fileTransfer.FileTransferRpcService;
import org.idea.irpc.framework.spring.starter.common.IRpcReference;
import org.idea.irpc.framework.spring.starter.common.IRpcService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author jiangshang
 * @Date created in 10:07 上午 2023/3/19
 */
@IRpcService
public class FileTransferRpcServiceImpl implements FileTransferRpcService {

    @IRpcReference
    private GoodRpcService goodRpcService;
    @IRpcReference
    private PayRpcService payRpcService;

    //名字要和远程配置的线程池名称一致
    @Resource
    private ThreadPoolExecutor myDtpExecutor;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public String getUserId() {
        return "i am user1 service"+UUID.randomUUID().toString();
    }


    public static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            4, 10,
            1000, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(200),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public List<Map<String, String>> findMyGoods(String userId) {
        List<String> goodsNoList = goodRpcService.selectGoodsNoByUserId(userId);
        List<Map<String, String>> finalResult = new ArrayList<>();
        goodsNoList.forEach(goodsNo -> {
            Map<String, String> item = new HashMap<>(2);
            List<String> payHistory = payRpcService.getPayHistoryByGoodNo(goodsNo);
            item.put(goodsNo, payHistory.toString());
            finalResult.add(item);
        });
        return finalResult;
    }

    /**
     * 客户端上传文件到服务端
     * @param message
     * @param fileData
     * @throws IOException
     */
    public void uploadFile(FileMessage message, byte[] fileData) throws IOException {
        String directoryPath = "D:\\test\\upload";
        File directory = new File(directoryPath);

        // 如果目录不存在，则创建它
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "\\" + message.getFileName();

        // 创建文件，并写入数据
        try (OutputStream out = Files.newOutputStream(Paths.get(filePath), StandardOpenOption.CREATE_NEW)) {
            out.write(fileData);
        }

        System.out.println("文件接收完成：" + filePath);
    }


    /**
     * 从服务端获取指定文件下载到本地
     * @param fileName
     * @return
     * @throws IOException
     */
    @Override
    public FileMessage downloadFile(String fileName) throws IOException {
        // 读取文件数据
        String filePath = "D:/test/" + fileName;
        byte[] fileData = Files.readAllBytes(Paths.get(filePath));

        // 封装数据到 FileMessage 对象中
        FileMessage message = new FileMessage(fileName, fileData.length);
        message.setFileData(fileData);

        return message;
    }

    @Override
    public String testThread(FileMessage message) {
        DtpExecutor dtpExecutor = DtpRegistry.getDtpExecutor("myDtpExecutor");
        System.out.println(" 核心线程数：" + dtpExecutor.getCorePoolSize() + " " +"最大线程数：" + dtpExecutor.getMaximumPoolSize()
                +" " + "阻塞队列数：" + dtpExecutor.getQueue().size()  +" " + "活跃线程数：" + dtpExecutor.getActiveCount());

        dtpExecutor.execute(new UploadTask(message));
        return "success   服务的节点为：" + appName;
    }


    //测试注解方式引入
/*
    @Override
    public String testThread(FileMessage message) {
        System.out.println(" 核心线程数：" + myDtpExecutor.getCorePoolSize() + " " +"最大线程数：" + myDtpExecutor.getMaximumPoolSize()
                +" " + "阻塞队列数：" + myDtpExecutor.getQueue().size()  +" " + "活跃线程数：" + myDtpExecutor.getActiveCount());

        myDtpExecutor.execute(new UploadTask(message));
        return "success";
    }
*/

    @Override
    public String testNormalThread(FileMessage message) {
        System.out.println(" 核心线程数：" + threadPool.getCorePoolSize() + " " +"最大线程数：" + threadPool.getMaximumPoolSize()
                +" " + "阻塞队列数：" + threadPool.getQueue().size()  +" " + "活跃线程数：" + threadPool.getActiveCount());

        threadPool.execute(new UploadTask(message));
        return "success   服务的节点为：" + appName;
    }

    @Override
    public String HTTPTest(FileMessage message) {
        return null;
    }


}
