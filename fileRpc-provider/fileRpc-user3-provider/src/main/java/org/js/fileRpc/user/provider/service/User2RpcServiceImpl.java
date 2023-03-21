package org.js.fileRpc.user.provider.service;

import org.js.fileRpc.interfaces.good.GoodRpcService;
import org.js.fileRpc.interfaces.pay.PayRpcService;
import org.js.fileRpc.interfaces.fileTransfer.UserRpcService;
import org.idea.irpc.framework.spring.starter.common.IRpcReference;
import org.idea.irpc.framework.spring.starter.common.IRpcService;

import java.util.*;

/**
 * @Author jiangshang
 * @Date created in 10:07 上午 2023/3/19
 */
@IRpcService
public class User2RpcServiceImpl implements UserRpcService {

    @IRpcReference
    private GoodRpcService goodRpcService;
    @IRpcReference
    private PayRpcService payRpcService;

    @Override
    public String getUserId() {
        return "i am user2 service"+UUID.randomUUID().toString();
    }

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
}
