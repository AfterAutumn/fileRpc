package org.js.fileRpc.interfaces.pay;

import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 10:08 上午 2023/3/19
 */
public interface PayRpcService {

    boolean doPay();

    List<String> getPayHistoryByGoodNo(String goodNo);
}
