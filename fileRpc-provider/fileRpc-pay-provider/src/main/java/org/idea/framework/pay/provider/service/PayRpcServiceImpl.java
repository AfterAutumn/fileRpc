package org.idea.framework.pay.provider.service;

import org.idea.irpc.framework.interfaces.pay.PayRpcService;
import org.idea.irpc.framework.spring.starter.common.IRpcService;

import java.util.Arrays;
import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 10:58 上午 2022/3/19
 */
@IRpcService
public class PayRpcServiceImpl implements PayRpcService {

    @Override
    public boolean doPay() {
        System.out.println("pay1");
        return true;
    }

    @Override
    public List<String> getPayHistoryByGoodNo(String goodNo) {
        System.out.println("getPayHistoryByGoodNo");
        return Arrays.asList(goodNo + "-pay-001", goodNo + "-pay-002");
    }

}
