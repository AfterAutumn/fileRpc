package org.idea.irpc.framework.interfaces.good;

import java.util.List;

/**
 * @Author jiangshang
 * @Date created in 10:08 上午 2023/3/19
 */
public interface GoodRpcService {

    /**
     * 扣减库存
     */
    boolean decreaseStock();

    /**
     * 根据用户id查询购买过的商品信息
     *
     * @return
     */
    List<String> selectGoodsNoByUserId(String userId);
}
